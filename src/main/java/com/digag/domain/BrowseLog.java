package com.digag.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Yuicon on 2017/8/9.
 * https://github.com/Yuicon
 */
@Entity
public class BrowseLog{

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String id;

    private String ip;

    private String uid;

    private int count;

    private Date lastTime;

    public BrowseLog() {
    }

    public BrowseLog(String ip, int count, Date lastTime) {
        this.ip = ip;
        this.count = count;
        this.lastTime = lastTime;
    }

    public BrowseLog(String ip, String uid, int count, Date lastTime) {
        this.ip = ip;
        this.uid = uid;
        this.count = count;
        this.lastTime = lastTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
