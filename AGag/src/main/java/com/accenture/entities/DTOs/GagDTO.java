package com.accenture.entities.DTOs;

import com.accenture.entities.User;
import com.accenture.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GagDTO {

    private String name;

    private String content;

    private int upvotes;

    private List<User> upvotedBy;

    private UserRepository userRepository;

    private User author;

    public GagDTO(){

    }
    @Autowired
    public GagDTO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GagDTO(String name, String content, int upvotes, List<User> upvotedBy, User author) {
        this.name = name;
        this.content = content;
        this.upvotes = upvotes;
        this.upvotedBy = upvotedBy;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public List<User> getUpvotedBy() {
        return upvotedBy;
    }

    public void setUpvotedBy(List<User> upvotedBy) {
        this.upvotedBy = upvotedBy;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
