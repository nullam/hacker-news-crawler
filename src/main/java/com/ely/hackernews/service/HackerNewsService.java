package com.ely.hackernews.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ely.hackernews.model.HackerNewsEntry;
import com.ely.hackernews.scraper.HackerNewsScraper;
import com.ely.hackernews.entity.FilterType;
import com.ely.hackernews.entity.UsageLog;
import com.ely.hackernews.repository.UsageLogRepository;

import java.io.IOException;

@Service
public class HackerNewsService {

        private final HackerNewsScraper scraper;
        private final UsageLogRepository usageLogRepository;

        public HackerNewsService(HackerNewsScraper scraper, UsageLogRepository usageLogRepository) {
                this.scraper = scraper;
                this.usageLogRepository = usageLogRepository;
        }

        /**
         * Filters entries with titles containing more than five words
         * and sorts them by comments in descending order.
         *
         * @param entries the list of Hacker News entries.
         * @return filtered entries ordered by number of comments.
         */
        public List<HackerNewsEntry> filterLongTitlesByComments(
                        List<HackerNewsEntry> entries) {

                return entries.stream()
                                .filter(entry -> countWords(entry.title()) > 5)
                                .sorted(Comparator.comparingInt(HackerNewsEntry::comments)
                                                .reversed())
                                .toList();
        }

        /**
         * Filters entries with titles containing five or fewer words
         * and sorts them by points in descending order.
         *
         * @param entries the list of Hacker News entries.
         * @return filtered entries ordered by number of points.
         */
        public List<HackerNewsEntry> filterShortTitlesByPoints(
                        List<HackerNewsEntry> entries) {

                return entries.stream()
                                .filter(entry -> countWords(entry.title()) <= 5)
                                .sorted(Comparator.comparingInt(HackerNewsEntry::points)
                                                .reversed())
                                .toList();
        }

        /**
         * Counts words in a title, excluding tokens that contain only symbols.
         *
         * @param title the title to analyze.
         * @return the number of words.
         */
        private long countWords(String title) {

                if (title == null || title.isBlank()) {
                        return 0;
                }

                return java.util.Arrays.stream(title.trim().split("\\s+"))
                                .filter(word -> word.matches(".*[\\p{L}\\p{N}].*"))
                                .count();
        }

        /**
         * Retrieves and filters entries with long titles.
         *
         * @return entries sorted by comments in descending order.
         * @throws IOException if the homepage cannot be retrieved.
         */
        public List<HackerNewsEntry> getLongTitleEntries() throws IOException {
                usageLogRepository.save(new UsageLog(FilterType.LONG_TITLES));
                return filterLongTitlesByComments(scraper.scrapeTopEntries());
        }

        /**
         * Retrieves and filters entries with short titles.
         *
         * @return entries sorted by points in descending order.
         * @throws IOException if the homepage cannot be retrieved.
         */
        public List<HackerNewsEntry> getShortTitleEntries() throws IOException {
                usageLogRepository.save(new UsageLog(FilterType.SHORT_TITLES));
                return filterShortTitlesByPoints(scraper.scrapeTopEntries());
        }
}
