package com.edu.virtuallab.resource.service;

public class LikeFavoriteStatus {
    private boolean liked;
    private boolean favorited;

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }
    public boolean isFavorited() { return favorited; }
    public void setFavorited(boolean favorited) { this.favorited = favorited; }
} 