package com.example.ahmed.reze1.api.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mona Abdallh on 4/11/2018.
 */

public class CommentReplyResponse implements Serializable {

    @SerializedName("replayId")
    @Expose
    private int replayId;

    @SerializedName("replierId")
    @Expose
    private int replierId;

    @SerializedName("replayText")
    @Expose
    private String replayText;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;


    public int getReplayId() {
        return replayId;
    }

    public void setReplayId(int replayId) {
        this.replayId = replayId;
    }

    public int getReplierId() {
        return replierId;
    }

    public void setReplierId(int replierId) {
        this.replierId = replierId;
    }

    public String getReplayText() {
        return replayText;
    }

    public void setReplayText(String replayText) {
        this.replayText = replayText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
