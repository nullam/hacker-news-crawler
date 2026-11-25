package com.ely.hackernews.scraper;

import java.io.IOException;

import com.ely.hackernews.model.HackerNewsEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class HackerNewsScraper {
    private static final String HACKER_NEWS_URL = "https://news.ycombinator.com/";
    private static final int MAX_ENTRIES = 30;

    public Document fetchPage() throws IOException {
        return Jsoup.connect(HACKER_NEWS_URL).userAgent("Mozilla/5.0").timeout(10_000).get();
    }

    /**
     * Retrieves the Hacker News homepage and extracts its first 30 entries.
     *
     * @return a list containing the extracted Hacker News entries.
     * @throws IOException if the homepage cannot be retrieved.
     */
    public List<HackerNewsEntry> scrapeTopEntries() throws IOException {

        Document document = fetchPage();

        return parseEntries(document);
    }

    /**
     * Extracts the first 30 Hacker News entries from an HTML document.
     *
     * @param document the HTML document to process.
     * @return a list containing the extracted Hacker News entries.
     */
    List<HackerNewsEntry> parseEntries(Document document) {

        Elements storyRows = document.select("tr.athing");

        List<HackerNewsEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(storyRows.size(), MAX_ENTRIES); i++) {

            Element storyRow = storyRows.get(i);

            Element titleLink = storyRow.selectFirst(".titleline > a");
            Element metadataRow = storyRow.nextElementSibling();

            if (titleLink == null || metadataRow == null) {
                continue;
            }

            String title = titleLink.text();
            String rankText = storyRow.select(".rank").text();

            Element scoreElement = metadataRow.selectFirst(".score");

            String scoreText = scoreElement != null
                    ? scoreElement.text()
                    : "";

            String commentsText = metadataRow.select("a").stream()
                    .map(Element::text)
                    .filter(text -> text.toLowerCase().contains("comment"))
                    .findFirst()
                    .orElse("");

            int rank = parseInteger(rankText);

            if (rank == 0) {
                rank = i + 1;
            }

            int points = parseInteger(scoreText);
            int comments = parseInteger(commentsText);

            HackerNewsEntry entry = new HackerNewsEntry(
                    rank,
                    title,
                    points,
                    comments);

            entries.add(entry);
        }

        return entries;
    }

    /**
     * Extracts the numeric value from a text.
     *
     * @param text text containing a numeric value.
     * @return the extracted integer, or 0 if no number is found.
     */
    private int parseInteger(String text) {
        String number = text.replaceAll("[^0-9]", "");

        return number.isEmpty() ? 0 : Integer.parseInt(number);
    }
}