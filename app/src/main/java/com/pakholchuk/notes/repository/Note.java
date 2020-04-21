package com.pakholchuk.notes.repository;

public class Note {
    private String name;
    private String text;
    private String imgUri;
    private String creationDate;
    private String lastEditDate;

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Note(String name, String text, String imgUri, String creationDate) {
        this.name = name;
        this.text = text;
        this.imgUri = imgUri;
        this.creationDate = creationDate;
    }
}
