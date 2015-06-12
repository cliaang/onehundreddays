package com.action.onehundred.onehundreddays;

import java.util.Date;

/**
 * Created by Administrator on 2015/5/28 0028.
 */
public class DailyRecord{
    private Date date;
    private boolean completed;
    private String record;
    private boolean first;
    private boolean last;

    public DailyRecord(Date date){
        this.date = date;
        completed = false;
        first = false;
        last = false;
        record = "";
    }

    public DailyRecord(Date date, String record, boolean completed, boolean first, boolean last){
        this.date = date;
        this.record = record;
        this.completed = completed;
        this.first = first;
        this.last = last;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
