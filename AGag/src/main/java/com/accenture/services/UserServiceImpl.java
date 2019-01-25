package com.accenture.services;

import com.accenture.entities.BindingModels.UserBindingModel;
import com.accenture.entities.BindingModels.UserEditModel;
import com.accenture.entities.Role;
import com.accenture.entities.User;
import com.accenture.repositories.GagRepository;
import com.accenture.repositories.RoleRepository;
import com.accenture.repositories.UserRepository;
import com.accenture.services.Base.UserService;
import com.accenture.utils.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private GagRepository gagRepository;

    private ModelMapper mapper;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           GagRepository gagRepository,
                           ModelMapper mapper,
                           BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.gagRepository = gagRepository;
        this.mapper = mapper;
        this.encoder = encoder;
        checkIfMainRolesExist();
    }

    private void checkIfMainRolesExist() {
        List<Role> roles = roleRepository.findAll();
        if (roles.size() < 2){
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean registerUser(UserBindingModel u, BindingResult errors) {
        //TODO add decent validation
        if(errors.hasErrors() && userRepository.findByUsername(u.getUsername()) != null){
            return false;
        }
        User user = mapper.map(u, User.class);
        Role role = roleRepository.findByAuthority("ROLE_USER");
        String encryptedPassword = encoder.encode(u.getPassword());
        user.setPassword(encryptedPassword);
        user.getAuthorities().add(role);

        userRepository.save(user);
        return true;
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User getCurrentUser() {
        if (Util.isAnonymous()) {
            return null;
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(user.getUsername());
    }

    @Override
    public boolean editUser(User u, UserEditModel edit) {
        u.setNickname(edit.getNickname());
        u.setEmail(edit.getEmail());
        u.setProfilePic(edit.getProfilePic());

        if(!edit.getOldPassword().isEmpty()){
            if(!encoder.matches(u.getPassword(), edit.getOldPassword())){
                return false;
            }
            u.setPassword(encoder.encode(edit.getPassword()));

        }
        return true;
    }

    @Override
    public boolean banUser(int id) {
        //TODO IMPLEMENT
        return false;
    }

    @Override
    public boolean unbanUser(int id) {

        //TODO IMPLEMENT
        return false;
    }


    @Override
    public boolean addRoleToUser(int id, String role) {
        if(!role.startsWith("ROLE_")){
            role = "ROLE_" + role;
        }
        User u = findById(id);
        Role r = roleRepository.findByAuthority(role);
        boolean success = u.getAuthorities().add(r);
        if (!success) return false;
        userRepository.save(u);
        return true;
    }

    @Override
    public boolean removeRoleFromUser(int id, String role) {
        if(!role.startsWith("ROLE_")){
            role = "ROLE_" + role;
        }
        User u = findById(id);
        Role r = roleRepository.findByAuthority(role);
        boolean success = u.getAuthorities().remove(r);
        if (!success) return false;
        userRepository.save(u);
        return true;
    }
}
