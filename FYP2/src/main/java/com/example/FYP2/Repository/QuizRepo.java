package com.example.FYP2.Repository;

import com.example.FYP2.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepo extends JpaRepository<Quiz, Integer> {

    Optional<Quiz> findByRoomCode(String roomCode);

    boolean existsByRoomCode(String roomCode);

    List<Quiz> findByQuestions_Id(int questionId);

    List<Quiz> findByHost_Id(int userId);

    Optional<Quiz> findByHost_Id(int userId, String status);

//    Why findByHost_Id works
//
//    Because:
//
//    host = field name in Quiz
//    id = field name in User
    // method name must match the Java field name, not the column name.
    //@ManyToOne
    //@JoinColumn(name = "host_id")
    //private User host;   // ← field name is "host", type is User so must write like findByHostId/findByHost_Id


//    Yes! Every word after findBy must start with a capital letter, because Spring reads it by capitalisation (camelCase).
//    javafindByHostId
//    // Spring reads this as:
//// host → Id
//// finds Quiz where quiz.host.id = ?
//    If you wrote lowercase:
//    javafindByhostId  // ❌ Spring can't parse "hostId" correctly
//    So the rule is: first letter after findBy must be capital, and every new word starts with capital.
//    More examples:
//    javafindByRoomCode       // room → Code ✅
//            findByHostId         // host → Id ✅
//    findByGameOwnerId    // game → Owner → Id ✅
//            findByStatus         // status ✅

}
