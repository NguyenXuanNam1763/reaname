package com.example.renamefile.model;

public class Images {
    private int id;
    private String newUri;
    private String newName;
    private String originalUri;
    private String originalName;


    public Images() {
    }

    public Images(int id, String newUri, String newName, String originalUri, String originalName) {
        this.id = id;
        this.newUri = newUri;
        this.newName = newName;
        this.originalUri = originalUri;
        this.originalName = originalName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewUri() {
        return newUri;
    }

    public void setNewUri(String newUri) {
        this.newUri = newUri;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String originalUri) {
        this.originalUri = originalUri;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
