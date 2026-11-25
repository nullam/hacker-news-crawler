package com.ely.hackernews.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ely.hackernews.model.HackerNewsEntry;

public class HackerNewsServiceTest {
    private final HackerNewsService service = new HackerNewsService();

    /**
     * Verifies that titles with more than five words
     * are filtered and sorted by comments in descending order.
     */
    @Test
    void shouldFilterLongTitlesAndSortByComments() {

        List<HackerNewsEntry> entries = List.of(
                new HackerNewsEntry(1, "This is a very long title example", 100, 20),
                new HackerNewsEntry(2, "Short title", 200, 50),
                new HackerNewsEntry(3, "Another interesting story about Java development", 80, 40),
                new HackerNewsEntry(4, "A title with several words to test", 150, 10));

        List<HackerNewsEntry> result = service.filterLongTitlesByComments(entries);

        assertEquals(3, result.size());
        assertEquals(3, result.get(0).rack());
        assertEquals(1, result.get(1).rack());
        assertEquals(4, result.get(2).rack());
    }

    /**
     * Verifies that titles with five or fewer words
     * are filtered and sorted by points in descending order.
     */
    @Test
    void shouldFilterShortTitlesAndSortByPoints() {

        List<HackerNewsEntry> entries = List.of(
                new HackerNewsEntry(1, "Java is evolving", 100, 20),
                new HackerNewsEntry(2, "Spring Boot", 250, 50),
                new HackerNewsEntry(3, "Another interesting story about Java development", 300, 40),
                new HackerNewsEntry(4, "This is - a self-explained example", 150, 10));

        List<HackerNewsEntry> result = service.filterShortTitlesByPoints(entries);

        assertEquals(3, result.size());
        assertEquals(2, result.get(0).rack());
        assertEquals(4, result.get(1).rack());
        assertEquals(1, result.get(2).rack());
    }
}
