package com.example.mynotes;

import java.util.Date;

import io.realm.RealmObject;

public class Note extends RealmObject {
    private int id;
    private String noteTitle;
    private String noteContent;
    private Date tiestamp;
    private int imageId;

    public int getId() {
        return id;
    }

    public Date getTiestamp() {
        return tiestamp;
    }

    public void setTiestamp(Date tiestamp) {
        this.tiestamp = tiestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    public String getNoteContent(){return noteContent;}
    public void setNoteContent(String noteContent){this.noteContent=noteContent;}

    public int getImageId(int position){return imageId;}
    public void setImageId(int imageId){this.imageId=imageId;}

}
