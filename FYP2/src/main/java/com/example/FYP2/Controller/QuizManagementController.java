package com.example.FYP2.Controller;


import com.example.FYP2.DTO.QuizDTO;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.Entity.GamePlayer;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Repository.QuizRepo;
import com.example.FYP2.Repository.UserRepo;
import com.example.FYP2.Service.QuizService;
import com.example.FYP2.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@JsonView(Views.User.class)
@RestController
public class QuizManagementController {


    @Autowired
    QuizService quizService;

    @PostMapping("/user/create-quiz")
    public ResponseEntity<ReqRes> createQuiz()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return  ResponseEntity.ok(quizService.createQuiz(email));
    }

    @PostMapping("/user/join-quiz/{roomCode}")
    public ResponseEntity<ReqRes> joinQuiz(@PathVariable String roomCode)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(quizService.joinQuiz(roomCode,email));
    }

    @GetMapping("/user/quiz-session-detail/{roomCode}")
    public ResponseEntity<ReqRes> quizSessionDetail(@PathVariable String roomCode)
    {
            return ResponseEntity.ok(quizService.getQuizSessionDetail(roomCode));
    }

    @PatchMapping("/user/start-quiz-session/{roomCode}")//@PatchMappingUpdating one field only
    public ResponseEntity<ReqRes> startQuizSession(@PathVariable String roomCode )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return  ResponseEntity.ok(quizService.startQuizSession(roomCode,email));
    }

    @PostMapping("/user/submit-answer/{roomCode}")
    public ResponseEntity<ReqRes> submitQuizAnswer(@PathVariable String roomCode ,@RequestBody QuizDTO quizDTO)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        quizDTO.setRoomCode(roomCode);
        return  ResponseEntity.ok(quizService.submitQuizAnswer(quizDTO,email));
    }

    @GetMapping("/user/get-leaderboard/{roomCode}")
    public ResponseEntity<ReqRes> getleaderboard(@PathVariable String roomCode )
    {
        return  ResponseEntity.ok(quizService.getleaderboard(roomCode));
    }

    @GetMapping("/user/get-participant-result/{roomCode}")
    public ResponseEntity<ReqRes> getParticipantResult(@PathVariable String roomCode )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return  ResponseEntity.ok(quizService.getParticipantResult(roomCode,email));
    }

    @GetMapping("/user/get-quiz-history")
    public ResponseEntity<ReqRes> getQuizHistory()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return  ResponseEntity.ok(quizService.getQuizHistory(email));
    }

    @PatchMapping("/user/end-quiz-session/{roomCode}")
    public ResponseEntity<ReqRes> endQuizSession(@PathVariable String roomCode) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(
                quizService.endQuizSession(roomCode, email)
        );
    }

    @GetMapping("/user/quiz-session/rejoin")
    public  ResponseEntity<ReqRes> rejoin(@RequestParam String roomCode)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return  ResponseEntity.ok(quizService.rejoin(roomCode,email));

    }




}
