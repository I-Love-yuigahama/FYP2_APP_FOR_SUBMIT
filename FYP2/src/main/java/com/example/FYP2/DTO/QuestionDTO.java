package com.example.FYP2.DTO;


import com.example.FYP2.Views;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class QuestionDTO {

    private int id;
    private byte[] image;
    private String questionText;

    @JsonView(Views.Admin.class)
    private String answerText;

    @JsonView(Views.Admin.class)
    private String keyword;

    @JsonView(Views.Admin.class)
    private int maxMark;



}
