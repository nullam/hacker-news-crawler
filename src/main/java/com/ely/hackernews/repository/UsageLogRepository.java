package com.ely.hackernews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ely.hackernews.entity.UsageLog;

public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {

}
