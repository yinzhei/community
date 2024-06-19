package com.nowcoder.community.entity;

import java.util.Date;

public class DiscussPost {
    public int id;
    public int userId;
    public String title;
    public String content;
    public int type;
    public int status;
    public Date createTime;
    public int commentCount;
    public int score;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public Date getCreate_time() {
        return createTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreate_time(Date create_time) {
        this.createTime = create_time;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", create_time=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
