package com.digag.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Yuicon on 2017/7/8.
 * https://github.com/Yuicon
 */
@Entity
public class Entry {

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String title;

    private String type;

    private Date updatedAt;

    private Date createdAt;

    private int viewsCount;

    private int collectionCount;

    private int commentsCount;

    // 简介
    private String content;

    private boolean english;

    private String author;

    // 是否原创
    private boolean original;

    @Column(nullable = false, unique = true)
    private String originalUrl;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="article_id")
    private Article article;

    public Entry() {
    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", viewsCount=" + viewsCount +
                ", collectionCount=" + collectionCount +
                ", commentsCount=" + commentsCount +
                ", content='" + content + '\'' +
                ", english=" + english +
                ", author='" + author + '\'' +
                ", original=" + original +
                ", originalUrl='" + originalUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english = english;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
