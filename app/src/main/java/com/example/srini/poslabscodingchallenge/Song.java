package com.example.srini.poslabscodingchallenge;

public class Song {

    private String title;
    private String artist;
    private String collection;
    private String pic;


    public Song(String t, String a, String c, String p)
    {
        title = t;
        artist = a;
        collection = c;
        pic= p;
    }

    public String toString()
    {
        return title+ " by "+ artist+ " from "+collection;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getCollection()
    {
        return collection;
    }

    public String getURL()
    {
        return pic;
    }

}
