package com.AGag.services.Base;

import com.AGag.entities.BindingModels.UserBindingModel;
import com.AGag.entities.BindingModels.UserEditModel;
import com.AGag.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAll();
    boolean registerUser(UserBindingModel u, BindingResult errors);
    User findById(int id);
    User getCurrentUser();
    boolean editUser(User u, UserEditModel edit, MultipartFile file);
    boolean banUser(int id);
    boolean unbanUser(int id);
    boolean addRoleToUser(int id, String role);
    boolean removeRoleFromUser(int id, String role);
    String ban(String username);
    String unBan(String username);

    String checkIfUserExists(String username);
}
