package com.accenture.services;

import com.accenture.entities.BindingModels.CommentBindingModel;
import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.Comment;
import com.accenture.entities.Gag;
import com.accenture.entities.Tag;
import com.accenture.entities.User;
import com.accenture.repositories.CommentRepository;
import com.accenture.repositories.GagRepository;
import com.accenture.repositories.TagRepository;
import com.accenture.repositories.UserRepository;
import com.accenture.services.Base.GagService;
import com.accenture.custom.Storage;
import com.accenture.utils.Util;
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
        System.out.println("IM HERE");
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
        System.out.println(gags.size());
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
        user.getLikedGags().remove(gag);
        user.getPosts().remove(gag);
        List<Comment> comments = gag.getComments();
        user.getComments().removeAll(comments);
        for (Tag tag : gag.getTags()) {
            tag.getTaggedGags().remove(gag);
            tagRepository.save(tag);
        }
        storage.delete(gag.getContent());
        gagRepository.delete(gag);
        commentRepository.deleteAll(comments);
        userRepository.save(user);
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
}
