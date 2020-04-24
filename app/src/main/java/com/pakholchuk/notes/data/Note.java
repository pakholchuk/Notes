package com.pakholchuk.notes.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey
    private long id;
    private String name;
    private String body;
    private String imgPath;
    private String creationDate;
    private String lastEditDate;

    public Note(long id, String name, String body, String imgPath, String creationDate, String lastEditDate) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.imgPath = imgPath;
        this.creationDate = creationDate;
        this.lastEditDate = lastEditDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public long getId() {
        return id;
    }

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

}
