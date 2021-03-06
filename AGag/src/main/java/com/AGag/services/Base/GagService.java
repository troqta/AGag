package com.AGag.services.Base;

import com.AGag.entities.BindingModels.CommentBindingModel;
import com.AGag.entities.BindingModels.GagBindingModel;
import com.AGag.entities.Gag;
import com.AGag.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GagService {

    Gag getById(int id);

    List<Gag> getAll();

    boolean createGag(GagBindingModel model, MultipartFile file);

    boolean editGag(int id, MultipartFile file);

    void likeById(int id);

    User getCurrentUser();
    String getFresh(int number);
    String getHot(int number);

    void postComment(int id, CommentBindingModel model);

    boolean deleteGag(int id);

    String likeRest(int id);

    String checkIfGagExists(String name);
}
