package com.example.diary;

import java.text.SimpleDateFormat;
import java.util.Date;

//Model class

public class Content {
    private String title;
    private String text;
    private String dateandtime;
    private String edate;

    public Content() {
    }

    public Content(String title, String text,String dateandtime,String edate) {
        this.title = title;
        this.text = text;
        this.dateandtime=dateandtime;
        this.edate=edate;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
        String currentDateandTime = sdf.format(new Date());
        this.dateandtime = currentDateandTime;
    }

    public String getTitle() {
        return title;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
