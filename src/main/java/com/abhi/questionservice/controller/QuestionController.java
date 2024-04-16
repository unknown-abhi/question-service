package com.abhi.questionservice.controller;

import com.abhi.questionservice.model.Question;
import com.abhi.questionservice.model.QuestionResponse;
import com.abhi.questionservice.model.QuestionWrapper;
import com.abhi.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) { // Constructor Injection
        this.questionService = questionService;
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<?>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("questionsByCategory/{category}")
    public ResponseEntity<List<?>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("addQuestion")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @DeleteMapping("deleteQuestion/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable int id) {
        return questionService.deleteQuestion(id);
    }

    @GetMapping("generateQuestionQuiz")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String categoryName, @RequestParam Integer numQuestions) {
        return questionService.getQuestionsForQuiz(categoryName, numQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) throws Exception {
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<?> getScore(@RequestBody List<QuestionResponse> questionResponses) {
        return questionService.getScore(questionResponses);
    }
}
