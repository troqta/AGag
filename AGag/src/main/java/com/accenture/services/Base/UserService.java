package com.accenture.services.Base;

import com.accenture.entities.BindingModels.UserBindingModel;
import com.accenture.entities.BindingModels.UserEditModel;
import com.accenture.entities.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAll();
    boolean registerUser(UserBindingModel u, BindingResult errors);
    User findById(int id);
    User getCurrentUser();
    boolean editUser(User u, UserEditModel edit);
    boolean banUser(int id);
    boolean unbanUser(int id);
    boolean addRoleToUser(int id, String role);
    boolean removeRoleFromUser(int id, String role);
}
