package com.example.QuizApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query("SELECT q FROM Quiz q WHERE q.startDate <= ?1 AND q.endDate >= ?1")
    List<Quiz> findActiveQuizzes(LocalDateTime currentTime);

    @Query("SELECT q FROM Quiz q WHERE q.endDate < ?1")
    List<Quiz> findFinishedQuizzes(LocalDateTime currentTime);
}
