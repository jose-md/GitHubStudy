package com.pepe.githubstudy.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "TRACE".
 */
@Entity
public class Trace {

    @Id
    @NotNull
    private String id;
    private String type;
    private String userId;
    private Long repoId;
    private java.util.Date startTime;
    private java.util.Date latestTime;
    private Integer traceNum;

    @Generated
    public Trace() {
    }

    public Trace(String id) {
        this.id = id;
    }

    @Generated
    public Trace(String id, String type, String userId, Long repoId, java.util.Date startTime, java.util.Date latestTime, Integer traceNum) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.repoId = repoId;
        this.startTime = startTime;
        this.latestTime = latestTime;
        this.traceNum = traceNum;
    }

    @NotNull
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(java.util.Date latestTime) {
        this.latestTime = latestTime;
    }

    public Integer getTraceNum() {
        return traceNum;
    }

    public void setTraceNum(Integer traceNum) {
        this.traceNum = traceNum;
    }

}