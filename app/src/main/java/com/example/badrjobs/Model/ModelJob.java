package com.example.badrjobs.Model;

public class ModelJob {
    private String id;
    private String title;
    private String ownerName;
    private String ownerNiceName;
    private String ownerImage;
    private boolean isLiked;
    private String isActive;

    public String isActive() {
        return isActive;
    }

    public void setActive(String active) {
        isActive = active;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNiceName() {
        return ownerNiceName;
    }

    public void setOwnerNiceName(String ownerNiceName) {
        this.ownerNiceName = ownerNiceName;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
