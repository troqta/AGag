package com.accenture.services;

import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.BindingModels.GagEditModel;
import com.accenture.entities.Gag;
import com.accenture.entities.User;
import com.accenture.repositories.CommentRepository;
import com.accenture.repositories.GagRepository;
import com.accenture.repositories.TagRepository;
import com.accenture.repositories.UserRepository;
import com.accenture.services.Base.GagService;
import com.accenture.services.Base.Storage;
import com.accenture.utils.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class GagServiceImpl implements GagService {

    private GagRepository gagRepository;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private TagRepository tagRepository;

    private Storage storage;

    private ModelMapper mapper;

    @Autowired
    public GagServiceImpl(GagRepository gagRepository,
                          UserRepository userRepository,
                          CommentRepository commentRepository,
                          TagRepository tagRepository,
                          Storage storage,
                          ModelMapper mapper) {
        this.gagRepository = gagRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.storage = storage;
        this.mapper = mapper;
    }

    @Override
    public Gag getById(int id) {
        return gagRepository.getOne(id);
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
        User author = userRepository.findByUsername(Util.currentUser().getUsername());
        gag.setAuthor(author);


        gagRepository.save(gag);
        return true;
    }

    @Override
    public boolean editGag(GagEditModel model, MultipartFile file) {
        return  false;
    }
}
