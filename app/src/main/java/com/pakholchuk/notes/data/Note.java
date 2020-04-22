package com.pakholchuk.notes.data;

public class Note {
    public static final String NAME = "name";
    public static final String BODY = "body";
    public static final String IMAGE = "image";
    public static final String CREATION = "creation";
    public static final String EDIT = "edit";
    private String name;
    private String body;
    private String imgPath;
    private String creationDate;
    private String lastEditDate;

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getImgPath() {
        return imgPath;
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

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Note(String name, String body, String imgPath) {
        this.name = name;
        this.body = body;
        this.imgPath = imgPath;
    }
}
