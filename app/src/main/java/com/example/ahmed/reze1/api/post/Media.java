package com.example.ahmed.reze1.api.post;

/**
 * Created by Mona Abdallh on 3/29/2018.
 */

public class Media {

    private static final int IMAGE_TYPE = 0;
    private static final int VEDIO_TYPE = 1;

    private byte[] media;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }
}
