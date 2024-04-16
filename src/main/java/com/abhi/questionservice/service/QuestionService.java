package com.abhi.questionservice.service;

import com.abhi.questionservice.dao.QuestionDao;
import com.abhi.questionservice.model.Question;
import com.abhi.questionservice.model.QuestionResponse;
import com.abhi.questionservice.model.QuestionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) { // Constructor Injection
        this.questionDao = questionDao;
    }

    public ResponseEntity<List<?>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>(List.of("Error occurred while fetching all questions"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<?>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategory(category);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>(List.of("Error occurred while fetching questions by category"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addQuestion(Question question) {
        try {
            Question savedQuestion = questionDao.save(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            // Log the exception or handle it appropriately
            return new ResponseEntity<>("Error occurred while adding a new question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> deleteQuestion(int id) {
        Optional<Question> questionOptional = questionDao.findById(id);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            questionDao.delete(question); // Or use questionDao.deleteById(id)
            return ResponseEntity.ok("Data with ID :: " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id :: " + id + " not found");
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) throws Exception {
        List<Question> questionsFromDB = new ArrayList<>();

        for (Integer questionId : questionIds) {
            questionsFromDB.add(questionDao.findById(questionId).get());
        }
        if (questionsFromDB.isEmpty()) {
            throw new Exception("No questions found in the quiz.");
        }

        List<QuestionWrapper> questionForUser = questionsFromDB.stream()
                .map(question -> new QuestionWrapper(question.getId(), question.getQuestionTitle(),
                        question.getOption1(), question.getOption2(),
                        question.getOption3(), question.getOption4()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionForUser, HttpStatus.OK);
    }

    public ResponseEntity<?> getScore(List<QuestionResponse> questionResponses) {
        try {
            int correctAnswers = 0;

            for (QuestionResponse questionResponse : questionResponses) {
                Question question = questionDao.findById(questionResponse.getId()).get();
                // Ensure that we do not get an IndexOutOfBoundsException
                if (questionResponse.getQuestionResponses().equals(question.getRightAnswer())) {
                    correctAnswers++;
                }
            }
            return new ResponseEntity<>(correctAnswers, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception (optional, depending on the logging framework)
            // e.printStackTrace();

            // Return a generic error response
            return new ResponseEntity<>("An error occurred while calculating the result", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
