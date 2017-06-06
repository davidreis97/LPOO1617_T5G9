package com.drfl.twinstickshooter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TSSScore {

    private String name;
    private int score;
    private String date;

    public TSSScore(String name, int score) {
        this.name = name;
        this.score = score;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.date = dateFormat.format(new Date());
    }

    //Needed for JSON serialization
    public TSSScore() {
    }

    @Override
    public String toString() {
        return new String(this.name + "\n" + this.score + "\n" + this.date);
    }
}