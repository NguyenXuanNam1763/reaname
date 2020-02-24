package com.example.renamefile.model;

public class Images {
    private int id;
    private String uri;
    private byte[] image;

    public Images(int id, String uri, byte[] image) {
        this.id = id;
        this.uri = uri;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
