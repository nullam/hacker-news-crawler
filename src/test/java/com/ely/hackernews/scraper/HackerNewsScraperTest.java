package com.ely.hackernews.scraper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import com.ely.hackernews.model.HackerNewsEntry;

public class HackerNewsScraperTest {
    private final HackerNewsScraper scraper = new HackerNewsScraper();

    /** Verify the correct extraction of data from Hacker News */
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

    /** Verifies that entries without points or comments are handled correctly. */
    @Test
    void shouldHandleEntriesWithoutPointsOrComments() {

        String html = """
                <table>
                    <tr class="athing submission">
                        <td class="title">
                            <span class="rank">1.</span>
                            <span class="titleline">
                                <a href="https://example.com">Example entry</a>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="subtext"></td>
                    </tr>
                </table>
                """;

        Document document = Jsoup.parse(html);

        List<HackerNewsEntry> entries = scraper.parseEntries(document);

        assertEquals(1, entries.size());
        assertEquals(0, entries.get(0).points());
        assertEquals(0, entries.get(0).comments());
    }

    /** Verifies that no more than 30 entries are extracted. */
    @Test
    void shouldExtractMaximumOfThirtyEntries() {

        StringBuilder html = new StringBuilder("<table>");

        for (int i = 1; i <= 35; i++) {
            html.append("""
                    <tr class="athing submission">
                        <td class="title">
                            <span class="rank">%d.</span>
                            <span class="titleline">
                                <a href="https://example.com">Entry %d</a>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="subtext">
                            <span class="score">10 points</span>
                            <a href="item?id=%d">5 comments</a>
                        </td>
                    </tr>
                    """.formatted(i, i, i));
        }

        html.append("</table>");

        Document document = Jsoup.parse(html.toString());

        List<HackerNewsEntry> entries = scraper.parseEntries(document);

        assertEquals(30, entries.size());
    }
}
