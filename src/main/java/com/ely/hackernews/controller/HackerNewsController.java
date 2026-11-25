package com.ely.hackernews.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ely.hackernews.model.HackerNewsEntry;
import com.ely.hackernews.service.HackerNewsService;

@RestController
@RequestMapping("/api/news")
public class HackerNewsController {

    private final HackerNewsService service;

    public HackerNewsController(HackerNewsService service) {
        this.service = service;
    }

    /**
     * Retrieves entries with titles containing more than five words.
     *
     * @return entries ordered by comments in descending order.
     * @throws IOException if the homepage cannot be retrieved.
     */
    @GetMapping("/long-titles")
    public List<HackerNewsEntry> getLongTitleEntries() throws IOException {
        return service.getLongTitleEntries();
    }

    /**
     * Retrieves entries with five or fewer words in their titles.
     *
     * @return entries ordered by points in descending order.
     * @throws IOException if the homepage cannot be retrieved.
     */
    @GetMapping("/short-titles")
    public List<HackerNewsEntry> getShortTitleEntries() throws IOException {
        return service.getShortTitleEntries();
    }

}
