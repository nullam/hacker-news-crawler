package com.ely.hackernews.scraper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import com.ely.hackernews.model.HackerNewsEntry;

public class HackerNewsScraperTest {
    private final HackerNewsScraper scraper = new HackerNewsScraper();

    @Test
    void shouldExtractEntryDetailsFromHtml() {

        String html = """
                <table>
                    <tr class="athing submission">
                        <td class="title">
                            <span class="rank">1.</span>
                            <span class="titleline">
                                <a href="https://example.com">
                                    Example Hacker News entry
                                </a>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="subtext">
                            <span class="score">125 points</span>
                            <a href="item?id=123">42 comments</a>
                        </td>
                    </tr>
                </table>
                """;

        Document document = Jsoup.parse(html);

        List<HackerNewsEntry> entries = scraper.parseEntries(document);

        assertEquals(1, entries.size());

        HackerNewsEntry entry = entries.get(0);

        assertEquals(1, entry.rack());
        assertEquals("Example Hacker News entry", entry.title());
        assertEquals(125, entry.points());
        assertEquals(42, entry.comments());
    }

}
