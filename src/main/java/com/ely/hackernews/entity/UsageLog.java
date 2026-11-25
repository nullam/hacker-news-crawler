package com.ely.hackernews.entity;

import java.time.Instant;

import jakarta.persistence.*;

import com.ely.hackernews.entity.FilterType;

@Entity
@Table(name = "usage_logs")
public class UsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilterType filterApplied;

    protected UsageLog() {
    }

    public UsageLog(FilterType filterApplied) {
        this.requestedAt = Instant.now();
        this.filterApplied = filterApplied;
    }

    public Long getId() {
        return id;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public FilterType getFilterApplied() {
        return filterApplied;
    }
}
