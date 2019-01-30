package com.accenture.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Expose
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Gag> taggedGags;

    public Tag(){
        taggedGags = new ArrayList<>();
    }

    public Tag(String name) {
        this.name = name;
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

    public List<Gag> getTaggedGags() {
        return taggedGags;
    }

    public void setTaggedGags(List<Gag> taggedGags) {
        this.taggedGags = taggedGags;
    }
}
