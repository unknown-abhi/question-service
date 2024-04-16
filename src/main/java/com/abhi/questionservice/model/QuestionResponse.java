package com.abhi.questionservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QuestionResponse {

    private Integer id;
    private String questionResponses;
}
