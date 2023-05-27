package com.example.QuizApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz getActiveQuiz() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Quiz> activeQuizzes = quizRepository.findActiveQuizzes(currentTime);
        if (!activeQuizzes.isEmpty()) {
            return activeQuizzes.get(0);
        }
        return null;
    }

    public String getQuizResult(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz != null && LocalDateTime.now().isAfter(quiz.getEndDate().plusMinutes(5))) {
            int rightAnswer = quiz.getRightAnswer();
            List<String> options = quiz.getOptions();
            return options.get(rightAnswer);
        }
        return null;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // Update quiz status based on the start and end times
    public void updateQuizStatus() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Quiz> quizzes = quizRepository.findAll();
        for (Quiz quiz : quizzes) {
            if (currentTime.isBefore(quiz.getStartDate())) {
                quiz.setStatus(QuizStatus.INACTIVE);
            } else if (currentTime.isAfter(quiz.getEndDate())) {
                quiz.setStatus(QuizStatus.FINISHED);
            } else {
                quiz.setStatus(QuizStatus.ACTIVE);
            }
        }
        quizRepository.saveAll(quizzes);
    }
}
