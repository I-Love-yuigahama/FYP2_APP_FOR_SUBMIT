package com.example.FYP2.DTO;

import com.example.FYP2.Entity.Quiz;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {


    private int statusCode;
    private String error;
    private String message;

    //UserDTO
    private UserDTO userDTO;
    private List<UserDTO> userDTOList;

    //QuestionDTO
    private QuestionDTO questionDTO;
    private List<QuestionDTO> questionDTOList;
//    private List<QuestionPublicDTO> questionPublicDTOList;

    private QuizDTO quizDTO;
    private List<QuizDTO> quizDTOList;

//    private QuizPublicDTO quizPublicDTO;

    private GamePlayerDTO gamePlayerDTO;
    private List<GamePlayerDTO> gamePlayerDTOList;

    private List<LeaderBoardDTO> leaderBoardDTOList;

    private List<QuizHistoryDTO> quizHistoryDTOList;

    private MailBody mailBody;

}
