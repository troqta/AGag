package com.accenture.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Expose
    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User author;

    private Timestamp postDate;

    public Comment(){
        postDate = new Timestamp(System.currentTimeMillis());
    }

    public Comment(String content, User author) {
        this.content = content;
        this.author = author;
        postDate = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }
}
