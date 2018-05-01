package com.example.ahmed.reze1.api.news_feed;

import com.example.ahmed.reze1.api.post.AttachmentResponse;
import com.example.ahmed.reze1.api.post.CommentResponse;

/**
 * Created by Mona Abdallh on 4/30/2018.
 */

public class NewsFeedItem {

    public static final int PRODUCT_TYPE = 0;
    public static final int POST_TYPE = 1;

    private int type;
    private int id;
    private int ownerId;
    private String postText;
    private AttachmentResponse postAttachment;
    private CommentResponse[] postComments;
    private String createdAt;
    private int[] likes;
    private String ownerName;
    private String productTitle;
    private float productPrice;
    private int productAmount;
    private String productImageUrl;
    private String productDescription;
    private int productSale;
    private int productSoldAmount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public AttachmentResponse getPostAttachment() {
        return postAttachment;
    }

    public void setPostAttachment(AttachmentResponse postAttachment) {
        this.postAttachment = postAttachment;
    }

    public CommentResponse[] getPostComments() {
        return postComments;
    }

    public void setPostComments(CommentResponse[] postComments) {
        this.postComments = postComments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductSale() {
        return productSale;
    }

    public void setProductSale(int productSale) {
        this.productSale = productSale;
    }

    public int getProductSoldAmount() {
        return productSoldAmount;
    }

    public void setProductSoldAmount(int productSoldAmount) {
        this.productSoldAmount = productSoldAmount;
    }
}
