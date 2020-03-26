package com.example.androidclass2;

import java.io.Serializable;

/**
 * Created by xing on 2020/3/14.
 */
public class Todo implements Comparable<Todo>, Serializable {
    private String date;
    private String content;
    private Integer date_l;
    private int progress;

    public Todo(String date, String content) {
        this.date = date;
        this.content = content;
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

    @Override
    public int compareTo(Todo o) {
        return this.date_l.compareTo(o.getDate_l());
    }
}
