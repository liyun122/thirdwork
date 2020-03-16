package com.example.androidclass2;

/**
 * Created by xing on 2020/3/14.
 */
public class Todo implements Comparable<Todo>{
    private String date;
    private String content;
    private Integer date_l;
    public Todo(){

    }
    public Todo(String date, String content) {
        this.date = date;
        this.content = content;
        this.date_l=Integer.parseInt(date.replaceAll("-",""));
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
