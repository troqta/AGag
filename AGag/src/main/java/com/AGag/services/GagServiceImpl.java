package com.AGag.services;

import com.AGag.entities.BindingModels.CommentBindingModel;
import com.AGag.entities.BindingModels.GagBindingModel;
import com.AGag.entities.Comment;
import com.AGag.entities.Gag;
import com.AGag.entities.Tag;
import com.AGag.entities.User;
import com.AGag.repositories.CommentRepository;
import com.AGag.repositories.GagRepository;
import com.AGag.repositories.TagRepository;
import com.AGag.repositories.UserRepository;
import com.AGag.services.Base.GagService;
import com.AGag.custom.Storage;
import com.AGag.utils.Util;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GagServiceImpl implements GagService {

    private GagRepository gagRepository;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private TagRepository tagRepository;

    private Storage storage;

    private ModelMapper mapper;

    private Gson parser;

    private final int limit = Util.GAGS_PER_PAGE;

    @Autowired
    public GagServiceImpl(GagRepository gagRepository,
                          UserRepository userRepository,
                          CommentRepository commentRepository,
                          TagRepository tagRepository,
                          Storage storage,
                          ModelMapper mapper,
                          Gson parser) {
        this.gagRepository = gagRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.storage = storage;
        this.mapper = mapper;
        this.parser = parser;
    }

    @Override
    public Gag getById(int id) {
        return gagRepository.findById(id);
    }

    @Override
    public List<Gag> getAll() {
        return gagRepository.findAll();
    }

    @Override
    public boolean createGag(GagBindingModel model, MultipartFile file) {
        Gag gag = mapper.map(model, Gag.class);

        if (file.isEmpty()){
            return false;
        }
        String path = storage.storeWithCustomLocation(gag.getName(), file);
        gag.setContent(path);
        User author = getCurrentUser();
        gag.setAuthor(author);
        if(!model.getTagString().isEmpty()){
            Set<Tag> tags = handleTags(model.getTagString(), gag);
            gag.getTags().clear();
            gag.getTags().addAll(tags);
        }

        gagRepository.save(gag);

        author.getPosts().add(gag);
        userRepository.save(author);
        return true;
    }

    private Set<Tag> handleTags(String tagString, Gag gag) {
        String[] tagArray = tagString.split(" ");
        Set<Tag> tags = new HashSet<>();

        for (String tag : tagArray) {
            Tag t = tagRepository.findByName(tag);

            if (t == null) {
                Tag newTag = new Tag(tag);
                tagRepository.save(newTag);
                tags.add(newTag);
            } else {
                t.getTaggedGags().add(gag);

                tags.add(t);
                tagRepository.save(t);
            }
        }
        return tags;
    }
    @Override
    public boolean editGag(int id, MultipartFile file) {
        Gag gag = getById(id);
        User user = getCurrentUser();
        if(Util.isAnonymous()){
            return false;
        }
        if (!user.isAdmin()){
            if(!user.isAuthor(gag)){
                return false;
            }
        }

        if (!file.isEmpty()) {
            String path = storage.storeWithCustomLocation(gag.getName(), file, gag.getContent().substring(gag.getContent().lastIndexOf("/")));
            gag.setContent(path);
        }
        gagRepository.save(gag);
        return  true;
    }

    @Override
    public void likeById(int id) {
        Gag gag = gagRepository.findById(id);

        User user = getCurrentUser();

        if(!gag.getUpvotedBy().contains(user) && !user.getLikedGags().contains(gag)){
            gag.setUpvotes(gag.getUpvotes() + 1);
            gag.getUpvotedBy().add(user);
            gagRepository.save(gag);
            user.getLikedGags().add(gag);
            userRepository.save(user);
        }
    }

    @Override
    public User getCurrentUser() {
        if(Util.currentUser() == null){
            return null;
        }
        return userRepository.findByUsername(Util.currentUser().getUsername());
    }

    @Override
    public String getFresh(int number) {
        number *= limit;
        List<Gag> gags = getAll().stream().sorted(Comparator.comparing(Gag::getCreatedOn)
                .reversed())
                .skip(number)
                .limit(limit)
                .collect(Collectors.toList());
        if(gags.isEmpty()){
            return "end";
        }
        return parser.toJson(gags);
    }

    @Override
    public String getHot(int number) {
        number *= limit;
        List<Gag> gags = getAll().stream().sorted(Comparator.comparing(Gag::getUpvotes)
                .reversed())
                .skip(number)
                .limit(limit)
                .collect(Collectors.toList());
        if(gags.isEmpty()){
            return "end";
        }
        return parser.toJson(gags);
    }

    @Override
    public void postComment(int id, CommentBindingModel model) {
        Gag gag = gagRepository.findById(id);
        User user = getCurrentUser();

        Comment comment = new Comment(model.getContent(), user);

        user.getComments().add(comment);
        gag.getComments().add(comment);

        commentRepository.save(comment);
        gagRepository.save(gag);
        userRepository.save(user);
    }

    @Override
    public boolean deleteGag(int id) {
        if (Util.isAnonymous()){
            return false;
        }
        Gag gag = gagRepository.findById(id);
        if (gag == null){
            return false;
        }
        User user = getCurrentUser();
        if (!user.isAdmin()){
            if (!user.isAuthor(gag)){
                return false;
            }
        }

        user.getPosts().remove(gag);
        gag.getAuthor().getPosts().remove(gag);
        userRepository.save(user);
        userRepository.save(gag.getAuthor());
        List<Comment> comments = gag.getComments();
        Set<User> likers = gag.getUpvotedBy();
        Set<Tag> tags = gag.getTags();

        tags.forEach(t -> {
            t.getTaggedGags().remove(gag);
            tagRepository.save(t);
        });
        comments.forEach(c -> commentRepository.delete(c));
        likers.forEach(l -> {
            l.getLikedGags().remove(gag);
            userRepository.save(l);
        });
        userRepository.findAll().forEach(u -> {
            u.getLikedGags().remove(gag);
            userRepository.save(u);
});
        comments.clear();
        likers.clear();
        tags.clear();
        gagRepository.save(gag);
        storage.delete(gag.getContent());
        gagRepository.delete(gag);


//        user.getLikedGags().remove(gag);
//        user.getPosts().remove(gag);
//        userRepository.save(user);
//        List<Comment> comments = gag.getComments();
//        user.getComments().removeAll(comments);
//        gagRepository.save(gag);
//        for (Tag tag : gag.getTags()) {
//            tag.getTaggedGags().remove(gag);
//            tagRepository.save(tag);
//        }
//        for (User u : gag.getUpvotedBy()) {
//            u.getLikedGags().remove(gag);
//            userRepository.save(u);
//        }
//        gag.getUpvotedBy().clear();
//        gagRepository.save(gag);
//        storage.delete(gag.getContent());
//        for(Comment c : comments){
//            commentRepository.delete(c);
//        }
//        comments.clear();
//        gagRepository.delete(gag);
        return true;
        }

@Override
public String likeRest(int id) {
        if (Util.isAnonymous()){
        return "You are not logged in!";
        }
        Gag gag = gagRepository.findById(id);

        User user = getCurrentUser();
        if(!gag.getUpvotedBy().contains(user) && !user.getLikedGags().contains(gag)){
        gag.setUpvotes(gag.getUpvotes() + 1);
        gag.getUpvotedBy().add(user);
        gagRepository.save(gag);
        user.getLikedGags().add(gag);
        userRepository.save(user);
        return "Like successful!";
        }
        else return "You have already liked this gag!";
        }

@Override
public String checkIfGagExists(String name) {
        Gag gag = gagRepository.findByName(name);
        if (gag != null){
        return "Gag with name " + name + " already exists!";
        }
        return "Valid name!";
        }


        }
