package com.example.ahmed.reze1.api.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mona Abdallh on 4/11/2018.
 */

public class CommentResponse implements Serializable{

    @SerializedName("commentId")
    @Expose
    private int commentId;

    @SerializedName("commenterId")
    @Expose
    private int commenterId;

    @SerializedName("commentText")
    @Expose
    private String commentText;

    @SerializedName("replies")
    @Expose
    private CommentReplyResponse[] replies;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public CommentReplyResponse[] getReplies() {
        return replies;
    }

    public void setReplies(CommentReplyResponse[] replies) {
        this.replies = replies;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
