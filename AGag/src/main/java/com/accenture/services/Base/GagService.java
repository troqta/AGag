package com.accenture.services.Base;

import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.BindingModels.GagEditModel;
import com.accenture.entities.Gag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GagService {

    Gag getById(int id);

    List<Gag> getAll();

    boolean createGag(GagBindingModel model, MultipartFile file);

    boolean editGag(GagEditModel model, MultipartFile file);
}
