package com.ely.hackernews.model;

/**
 * Data model representing a Hacker News entry.
 * Contains the information extracted from each news item.
 * 
 * @param rack   Position of the entry on the Hacker News homepage.
 * @param title    Title of the news entry.
 * @param points   Number of points received by the entry.
 * @param comments Number of comments associated with the entry
 */

public record HackerNewsEntry(
        int rack,
        String title,
        int points,
        int comments) {

}
