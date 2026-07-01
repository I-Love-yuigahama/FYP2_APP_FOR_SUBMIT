package com.example.FYP2.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor  // ✅ add
@AllArgsConstructor // ✅ add
@Entity
@Table(name = "gameplayer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"quiz_id", "user_id"})
        })

public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne//MANY of THIS entity → ONE of the target entity.
    //@ManyToOne side is usually the owner
    @JoinColumn(name = "quiz_id")
    private Quiz quizSession;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User participant;
    //So user contains:
    //
    //id
    //
    //username
    //
    //email
    //
    //password
    //
    //etc.
    //
    //Not just the ID.


    private double score;

    // To check if they finished answering
    private boolean hasSubmitted;

    private int currentQuestionIndex;




}
