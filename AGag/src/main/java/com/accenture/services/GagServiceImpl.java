package com.accenture.services;

import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.BindingModels.GagEditModel;
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
        storage.storeWithCustomLocation(gag.getName(), file);
        gag.setContent("/" + Util.DEFAULT_UPLOAD_DIR + "/" + gag.getName() + "/" + file.getOriginalFilename());
        User author = getCurrentUser();
        gag.setAuthor(author);
        Set<Tag> tags = handleTags(model.getTagString(), gag);
        gag.getTags().clear();
        gag.getTags().addAll(tags);
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
    public boolean editGag(GagEditModel model, MultipartFile file) {
        return  false;
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
        System.out.println(gags.size());
        if(gags.isEmpty()){
            return "end";
        }
        return parser.toJson(gags);
    }
}
