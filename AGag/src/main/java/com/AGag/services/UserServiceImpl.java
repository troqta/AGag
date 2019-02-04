package com.AGag.services;

import com.AGag.entities.BindingModels.UserBindingModel;
import com.AGag.entities.BindingModels.UserEditModel;
import com.AGag.entities.Role;
import com.AGag.entities.User;
import com.AGag.repositories.GagRepository;
import com.AGag.repositories.RoleRepository;
import com.AGag.repositories.UserRepository;
import com.AGag.custom.Storage;
import com.AGag.services.Base.UserService;
import com.AGag.utils.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private GagRepository gagRepository;

    private ModelMapper mapper;

    private BCryptPasswordEncoder encoder;

    private Storage storage;



    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           GagRepository gagRepository,
                           ModelMapper mapper,
                           BCryptPasswordEncoder encoder,
                           Storage storage) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.gagRepository = gagRepository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.storage = storage;
        checkIfMainRolesExist();
    }

    private void checkIfMainRolesExist() {
        List<Role> roles = roleRepository.findAll();
        if (roles.size() < 2) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));

        }
        User user = userRepository.findByUsername("admin");

    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean registerUser(UserBindingModel u, BindingResult errors) {
        //TODO add decent validation
        if (userRepository.findByUsername(u.getUsername()) != null) {
            errors.addError(new ObjectError("Username", "Username already taken"));
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
        return userRepository.findByUsername(Util.currentUser().getUsername());
    }

    @Override
    public boolean editUser(User u, UserEditModel edit, MultipartFile file) {
        if(!edit.getNickname().equals("")){
            u.setNickname(edit.getNickname());
        }
        if (!edit.getEmail().equals("")){
            u.setEmail(edit.getEmail());
        }
        if (!file.isEmpty()) {
            String filePath = storage.storeWithCustomLocation(u.getUsername(), file, u.getProfilePic().substring(u.getProfilePic().lastIndexOf("/")));
            u.setProfilePic(filePath);
        }

        if (!edit.getOldPassword().isEmpty()) {
            if (!encoder.matches(edit.getOldPassword(), u.getPassword())) {
                return false;
            }
            u.setPassword(encoder.encode(edit.getPassword()));

        }
        userRepository.save(u);
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
        if (!role.startsWith("ROLE_")) {
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
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        User u = findById(id);
        Role r = roleRepository.findByAuthority(role);
        boolean success = u.getAuthorities().remove(r);
        if (!success) return false;
        userRepository.save(u);
        return true;
    }

    @Override
    public String ban(String username) {
        Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");

        if (Util.isAnonymous() || !getCurrentUser().getAuthorities().contains(adminRole)) {
            return "You are not an admin!";
        }
        User user = userRepository.findByUsername(username);
        if(user == null){
            return "User does not exist!";
        }
        if (!user.isEnabled()) {
            return username + " is already Banned!";
        }
        if (user.isAdmin()) {
            return "You cannot ban another admin!";
        }
        user.setEnabled(false);
        userRepository.save(user);


        return "Successfully banned " + username;
    }

    @Override
    public String unBan(String username) {
        Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");

        if (Util.isAnonymous() || !getCurrentUser().getAuthorities().contains(adminRole)) {
            return "You are not an admin!";
        }
        User user = userRepository.findByUsername(username);
        if(user == null){
            return "User does not exist!";
        }
        if (user.isEnabled()) {
            return username + " is not banned!";
        }
        if (user.isAdmin()) {

            return "You cannot ban another admin!";
        }
        user.setEnabled(true);
        userRepository.save(user);


        return "Successfully unbanned " + username;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s);
    }
    @Override
    public String checkIfUserExists(String username) {
        if (username.length()<5 || username.length() > 20){
            return "Username must be between 5 and 20 symbols long";
        }
        if (userRepository.findByUsername(username) != null){
            return "Username is already taken!";
        }
        return "Username is ok!";
    }
}
