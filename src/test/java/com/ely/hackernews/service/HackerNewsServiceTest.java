package com.ely.hackernews.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;

import com.ely.hackernews.model.HackerNewsEntry;
import com.ely.hackernews.repository.UsageLogRepository;
import com.ely.hackernews.scraper.HackerNewsScraper;
import com.ely.hackernews.entity.FilterType;

public class HackerNewsServiceTest {
    @Mock
    private HackerNewsScraper scraper;

    private HackerNewsService service;

    @Mock
    private UsageLogRepository usageLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new HackerNewsService(scraper, usageLogRepository);
    }

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

    /**
     * Verifies that requesting long-title entries records the LONG_TITLES filter
     * in the usage log repository.
     *
     * @throws IOException if an error occurs while retrieving the stories
     */
    @Test
    void shouldLogLongTitlesFilterUsage() throws IOException {
        when(scraper.scrapeTopEntries()).thenReturn(List.of());

        service.getLongTitleEntries();

        verify(usageLogRepository).save(
                argThat(log -> log.getFilterApplied() == FilterType.LONG_TITLES));
    }

    /**
     * Verifies that requesting short-title entries records the SHORT_TITLES filter
     * in the usage log repository.
     *
     * @throws IOException if an error occurs while retrieving the stories
     */
    @Test
    void shouldLogShortTitlesFilterUsage() throws IOException {
        when(scraper.scrapeTopEntries()).thenReturn(List.of());

        service.getShortTitleEntries();

        verify(usageLogRepository).save(
                argThat(log -> log.getFilterApplied() == FilterType.SHORT_TITLES));
    }
}
