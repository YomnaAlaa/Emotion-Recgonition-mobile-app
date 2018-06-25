package com.affectiva.imagedetectordemo;


public class UserInfo {

    private String usertime;
    private int userid, score;
    DatabaseHelper db;
    public UserInfo()
    {
        this.userid=-1;
        this.score=0;
    }

    public UserInfo(int userid,int score)
    {
  this.userid=userid;
  this.score=score;
    }

    public UserInfo(int userid)
    {
        this.userid=userid;
        this.score=0;
    }

    public String getUsertime() {
        return usertime;
    }

    public void setUsertime(String usertime) {
        this.usertime = usertime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
