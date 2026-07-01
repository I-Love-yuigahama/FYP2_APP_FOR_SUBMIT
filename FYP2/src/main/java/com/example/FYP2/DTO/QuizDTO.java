package com.example.FYP2.DTO;


import com.example.FYP2.Entity.GamePlayer;
import com.example.FYP2.Entity.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDTO {


    private String roomCode;

    private UserDTO host;

    private String status;

    List<QuestionDTO>   questionDTOList;

    private int questionId;

     List<GamePlayerDTO> gamePlayerDTOList;

    private String participantAns;

    List<LeaderBoardDTO> leaderBoardDTOList;

    private int questionIndex;


}
