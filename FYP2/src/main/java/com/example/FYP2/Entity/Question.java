package com.example.FYP2.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")

public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questions_id")
    private int id;

    @Lob
    @Column(name = "question_image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "answer_text")
    private String answerText;

    private String keyword;

    @Column(name = "max_mark")
    private int maxMark;

}
