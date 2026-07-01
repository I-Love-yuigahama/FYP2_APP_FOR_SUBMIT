package com.example.FYP2.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizPublicDTO {


    private String roomCode;

    private UserDTO host;

    private String status;

    List<QuestionPublicDTO>   questionPublicDTOList;

    private int questionId;

     List<GamePlayerDTO> gamePlayerDTOList;

    private String participantAns;

    List<LeaderBoardDTO> leaderBoardDTOList;

    private int questionIndex;


}
