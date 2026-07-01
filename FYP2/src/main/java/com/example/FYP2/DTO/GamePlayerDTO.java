package com.example.FYP2.DTO;


import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayerDTO {

    private String roomCode;

    private String userName;

    private String userEmail;

    private double score;

    private boolean hasSubmitted;



}
