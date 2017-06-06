package com.drfl.twinstickshooter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TSSScore implements Comparable<TSSScore> {

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

    @Override
    public int compareTo(TSSScore o) {

        if(this.score < o.score) {
            return 1;
        } else if(this.score > o.score) {
            return -1;
        }

        return 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}