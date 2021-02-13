package com.geektech.noteapp.ui.board;

public class BoardModel {

    private String title, titleBelow;
    private int images;

    public BoardModel(String title, String titleBelow, int images) {
        this.title = title;
        this.titleBelow = titleBelow;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleBelow() {
        return titleBelow;
    }

    public int getImages() {
        return images;
    }
}
