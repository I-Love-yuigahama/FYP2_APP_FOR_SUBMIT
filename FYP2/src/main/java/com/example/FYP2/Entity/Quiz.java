package com.example.FYP2.Entity;


import com.example.FYP2.DTO.QuestionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.util.List;


@Builder
@Data
@NoArgsConstructor  // ✅ add
@AllArgsConstructor // ✅ add
@Entity
@Table(name = "quiz")
public class Quiz {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int id;

    @Column(unique = true)
    private String roomCode;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    private String status;


    @ManyToMany
    @JoinTable(
            name = "quiz_question",
            joinColumns = @JoinColumn(name="quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    List<Question> questions;


    @OneToMany(mappedBy = "quizSession", cascade = CascadeType.ALL)
    private List<GamePlayer> gamePlayerList;

    private String  endedAt;





}
