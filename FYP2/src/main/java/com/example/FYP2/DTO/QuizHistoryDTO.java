package com.example.FYP2.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor  // ✅ Add this
@NoArgsConstructor   // ✅ Add this too (needed for Jackson)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizHistoryDTO {

    private String quizRole;

    private String roomCode;

    private String status;

    private String  endedAt;



}
