package com.accenture.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Gag> taggedGags;

    public Tag(){
        taggedGags = new ArrayList<>();
    }

    public Tag(String name, List<Gag> taggedGags) {
        this.name = name;
        this.taggedGags = taggedGags;
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
