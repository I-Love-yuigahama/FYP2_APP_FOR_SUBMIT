package com.example.FYP2.Repository;

import com.example.FYP2.Entity.GamePlayer;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface GamePlayerRepo extends JpaRepository<GamePlayer, Integer> {

//    List<GamePlayer> findByUserAndQuizSession(Quiz quizSession, User participant);

    boolean existsByQuizSessionIdAndParticipantId(Integer quizId, Integer userId);

    //quizSession 你在 GamePlayer 里的定义d
    // Even though the database column is quiz_id,
    // Spring looks at the Java variable name (quizSession) to build the query.

    List<GamePlayer> findByQuizSession_RoomCodeOrderByScoreDesc(String roomCode);

    Optional<GamePlayer> findByQuizSessionIdAndParticipantId(Integer quizId, Integer userId);


    //findBy Start the query (SQL SELECT)

    //QuizSession Matches your Java field private Quiz quizSession

    // _ Tells Spring to look inside the Quiz entity
    // tell Spring: "Stop guessing. The variable is exactly quizSession,
    // and the property inside it is exactly roomCode."

    // RoomCode A field named roomCode inside the Quiz class(roomCode).
    
    // OrderByScoreDesc Sorts by the score field in descending order
    //
    // ← add this locking version for submitQuizAnswer
    @Lock(LockModeType.PESSIMISTIC_WRITE)

//    PESSIMISTIC_WRITE = Lock this database row immediately for writing.

    @Query("SELECT g FROM GamePlayer g WHERE g.quizSession.id = :quizSessionId AND g.participant.id = :participantId")
//    Look at every GamePlayer object
//    Call each one "g"
//
//    The : means:
//      This is a placeholder variable.
//      similair to String name = "John"; where name is variable
//      :quizSessionId is a JPQL variabl
//
    Optional<GamePlayer> findByQuizSessionIdAndParticipantIdForUpdate(

//            Spring doesn't care about ForUpdate. i could name anything i like
//            but Developers usually write: ForUpdate  to remind themselves:
//            This query uses PESSIMISTIC_WRITE and returns a locked row.


            @Param("quizSessionId") Integer quizSessionId,
            @Param("participantId") Integer participantId
//            repository.findByQuizSessionIdAndParticipantIdForUpdate(5, 2);
//            Spring receives:
//
//            quizSessionId = 5
//            participantId = 2
//            @Param("quizSessionId") tells Spring:Put the value 2 into :participantId

//            Spring transform fROm
//            SELECT g
//              FROM GamePlayer g
//              WHERE g.quizSession.id = :quizSessionId
//              AND g.participant.id = :participantId
//
//              to
//
//                SELECT g
//                    FROM GamePlayer g
//                    WHERE g.quizSession.id = 5
//                    AND g.participant.id = 2
    );



}
