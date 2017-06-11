package com.drfl.twinstickshooter.game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * High score structure class for easy serialization to JSON file
 */
public class TSSScore implements Comparable<TSSScore> {

    /**
     * Name of the high scorer.
     */
    private String name;

    /**
     * The high score value.
     */
    private int score;

    /**
     * The current date in string format.
     */
    private String date;

    /**
     * Constructs a score instance with a certain name and score using the current date.
     *
     * @param name The name of the high scorer
     * @param score The value of the high score
     */
    TSSScore(String name, int score) {
        this.name = name;
        this.score = score;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.date = dateFormat.format(new Date());
    }

    /**
     * Default constructor, needed for JSON serialization.
     */
    public TSSScore() { }

    @Override
    public int compareTo(TSSScore o) {

        if(this.score < o.score) {
            return 1;
        } else if(this.score > o.score) {
            return -1;
        }

        return 0;
    }

    /**
     *  @return The high scorer name
     */
    public String getName() {
        return name;
    }

    /**
     *  @return The high score value
     */
    public int getScore() {
        return score;
    }

    /**
     *  @return The string representation of the high score date
     */
    public String getDate() {
        return date;
    }
}