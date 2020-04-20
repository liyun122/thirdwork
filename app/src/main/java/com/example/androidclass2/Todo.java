package com.example.androidclass2;

import java.io.Serializable;


public class Todo implements Comparable<Todo>, Serializable {
    private String date;//开始日期
    private String date_ex;//截止日期
    private String content;//事项内容
    private Integer date_l;//开启日期(int类型，用于排序)
    private int progress;//进度
    private byte[] image;//图片字节数组

    public Todo(String date, String date_ex, String content, int progress, byte[] image) {
        this.date = date;
        this.content = content;
        this.date_ex=date_ex;
        this.image=image;
        this.date_l=Integer.parseInt(date.replaceAll("-",""));
        this.progress=0;
        this.progress=progress;
    }

    public Todo(String date, String date_ex, String content, byte[] image) {
        this.date = date;
        this.content = content;
        this.date_ex=date_ex;
        this.image=image;
        this.date_l=Integer.parseInt(date.replaceAll("-",""));
        this.progress=0;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Integer getDate_l() {
        return date_l;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getDate_ex() {
        return date_ex;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public int compareTo(Todo o) {
        return this.date_l.compareTo(o.getDate_l());
    }
}
