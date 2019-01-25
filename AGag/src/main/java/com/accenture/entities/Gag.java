package com.accenture.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Gag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable=false, columnDefinition="mediumblob")
    private byte[] content;

    @ManyToOne
    private User author;

    @OneToMany
    private List<Comment> comments;

    @ManyToMany
    private List<Tag> tags;

    private int upvotes;

    @OneToMany
    private List<User> upvotedBy;

    private Timestamp createdOn;



    public Gag(){
        tags = new ArrayList<>();
        upvotedBy = new ArrayList<>();
        createdOn = new Timestamp(System.currentTimeMillis());
        upvotes = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Gag(String name,
               byte[] content,
               User author,
               List<Comment> comments,
               List<Tag> tags,
               int upvotes,
               List<User> upvotedBy,
               Timestamp createdOn) {
        this.name = name;
        this.content = content;
        this.author = author;
        this.comments = comments;
        this.tags = tags;
        this.upvotes = upvotes;
        this.upvotedBy = upvotedBy;
        this.createdOn = createdOn;


    }
}