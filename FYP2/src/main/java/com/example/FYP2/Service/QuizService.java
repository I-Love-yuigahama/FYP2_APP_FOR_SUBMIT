package com.example.FYP2.Service;

import com.example.FYP2.Controller.QuizWebSocketController;
import com.example.FYP2.DTO.*;
import com.example.FYP2.Entity.GamePlayer;
import com.example.FYP2.Entity.Question;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Inteface.*;
import com.example.FYP2.Repository.GamePlayerRepo;
import com.example.FYP2.Repository.QuestionRepo;
import com.example.FYP2.Repository.QuizRepo;
import com.example.FYP2.Repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
@Service
public class QuizService {

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private GamePlayerRepo gamePlayerRepo;

    @Autowired
    private QuizWebSocketController webSocketController;


    @Autowired
    private GamePlayerMapper gamePlayerMapper;

    @Autowired
    private AiGradingService aiGradingService;

    @Autowired
    private LeaderBoardMapper leaderBoardMapper;

    @Autowired
    private QuizPublicMapper quizPublicMapper;

    @Autowired
    private EntityManager entityManager;  // ← add this field

    // ← replace ConcurrentHashMap with ScheduledExecutorService
    // 1. Create a HashMap (Key: String, Value: Integer)
    private final java.util.concurrent.ConcurrentHashMap<String, java.util.concurrent.ScheduledFuture<?>> pendingBroadcasts = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ScheduledExecutorService scheduler = java.util.concurrent.Executors.newScheduledThreadPool(4);
    //ScheduledExecutorService: Manages thread pool and schedules tasks where it can schedule,Shutdown and submit.
    //ScheduledFuture: Represents one scheduled task where it can only cancal,isDone,isCancelled()

    public ReqRes createQuiz(String email)
    {

        ReqRes respon = new ReqRes();

        try{

            User host = userRepo.findByEmail(email).orElseThrow();

             List<Quiz> checkHost =  quizRepo.findByHost_Id(host.getId());

            for(Quiz check : checkHost)
            {
                if(check.getStatus().equals("WAITING")||check.getStatus().equals("START"))
                {
//                    String roomCode = check.getRoomCode();
                    respon.setQuizDTO(quizMapper.toDto(check));
                    respon.setMessage("You are the Host of the Room:"+check.getRoomCode());
                    respon.setStatusCode(200);
                    return respon;
                }

            }

            Quiz quiz = Quiz
                    .builder()
                    .roomCode(generateRoomCode())
                    .host(host)
                    .status("WAITING")
                    .build();
            quiz.setQuestions(questionRepo.findRandomQuestions());
            respon.setQuizDTO(quizMapper.toDto(quizRepo.save(quiz)));
            respon.setStatusCode(200);
            respon.setMessage("Quiz Create Sucessfully ");


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            err.printStackTrace();  // ← shows full stack trace
            respon.setError(err.getMessage());
        }

        return respon;
    }

    public ReqRes joinQuiz(String roomCode, String email)
    {
        ReqRes respon = new ReqRes();

        try{

           Quiz quizSession = quizRepo.findByRoomCode(roomCode)
                   .orElseThrow( () -> new RuntimeException("Quiz not found"));

           User participant = userRepo.findByEmail(email)
                   .orElseThrow( () -> new RuntimeException("user not found"));


//
//            Optional<Quiz> activeQuiz = quizRepo.findByHost_Id(participant.getId(),quizSession.getStatus());

                //check if is host
               if(quizSession.getHost().getId().equals(participant.getId()))
               {
                   respon.setQuizDTO(quizMapper.toDto(quizSession ));
                   respon.setStatusCode(200);
                   respon.setMessage("You are the host of this quiz");
                   return respon;
               }

           if(gamePlayerRepo.existsByQuizSessionIdAndParticipantId(quizSession.getId(), participant.getId()))
           {
//               throw new RuntimeException("User already joined this quiz");
               //After throw, execution stops. so no need else


               respon.setQuizDTO(quizMapper.toDto(quizSession));
               respon.setStatusCode(200);
               respon.setMessage("Already joined this quiz");
               return respon;
           }

               GamePlayer gamePlayer = GamePlayer
                       .builder()
                       .quizSession(quizSession)
                       .participant(participant)
                       .score(0)
                       .hasSubmitted(false)
                       .build();

               gamePlayerRepo.save(gamePlayer);
               quizSession.getGamePlayerList().add(gamePlayer);

               Quiz saved = quizRepo.save(quizSession);

               // ← broadcast only the player list, NOT the full quiz status
               QuizDTO joinBroadcast = new QuizDTO();
               joinBroadcast.setGamePlayerDTOList(quizMapper.toDto(quizSession).getGamePlayerDTOList());
               joinBroadcast.setStatus("PLAYER_JOINED");
               webSocketController.broadcastQuizUpdate(roomCode, joinBroadcast);

            respon.setQuizDTO(quizMapper.toDto(saved ));
            respon.setStatusCode(200);
            respon.setMessage("Joined successfully");


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return  respon;

    }

    public ReqRes getQuizSessionDetail(String roomCode)
    {
        ReqRes respon = new ReqRes();

        try{

            Quiz quiz = quizRepo.findByRoomCode(roomCode)
                    .orElseThrow(() -> new RuntimeException("Quiz Session detail Couldn't not fetch"));

            respon.setQuizDTO(quizMapper.toDto(quiz));

            respon.setStatusCode(200);
            respon.setMessage("Joined successfully");

        } catch (Exception err) {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return  respon;
    }

    public ReqRes startQuizSession(String roomCode, String email)
    {

        ReqRes respon = new ReqRes();

        try{

            User user = userRepo.findByEmail(email)
                    .orElseThrow( () -> new RuntimeException("Cannot start"));

            Quiz quiz = quizRepo.findByRoomCode(roomCode)
                    .orElseThrow( () -> new RuntimeException("Error of starting quiz"));

            if(quiz.getHost().getId()!= user.getId())
            {
                respon.setStatusCode(403);
                respon.setError("Only the host can start the quiz");
                return respon;
            }

            quiz.setStatus("START");
            Quiz saved = quizRepo.save(quiz);
            webSocketController.broadcastQuizUpdate(roomCode,quizMapper.toDto(quiz));
            respon.setQuizDTO(quizMapper.toDto(saved));
            respon.setStatusCode(200);
            respon.setMessage("Start the quiz successfully");


        }catch (Exception err)
        {

            respon.setStatusCode(500);
            respon.setError(err.getMessage());

        }

        return  respon;

    }
    @Transactional
    public ReqRes submitQuizAnswer(QuizDTO quizDTO, String email)
    {
        ReqRes respon = new ReqRes();

        try{

            saveAns(quizDTO,email);

            // ← cancel any pending broadcast for this room
            java.util.concurrent.ScheduledFuture<?> existing = pendingBroadcasts.get(quizDTO.getRoomCode());
            if (existing != null) {
                existing.cancel(false);

//                future.cancel(boolean mayInterruptIfRunning);
//                The boolean parameter means:
//
//                true  -> interrupt the thread if task is already running
//                false -> don't interrupt if task is already running

            }

            // ← schedule a NEW broadcast 1 second from now
            // if another submit comes in within 1s, this gets cancelled and rescheduled
            java.util.concurrent.ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    ReqRes leaderboardResult = getleaderboard(quizDTO.getRoomCode());
                    QuizDTO broadcast = new QuizDTO();
                    broadcast.setLeaderBoardDTOList(leaderboardResult.getLeaderBoardDTOList());
                    broadcast.setStatus("SCORE_UPDATE");
                    webSocketController.broadcastQuizUpdate(quizDTO.getRoomCode(), broadcast);
                    pendingBroadcasts.remove(quizDTO.getRoomCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);

            pendingBroadcasts.put(quizDTO.getRoomCode(), future);

            respon.setStatusCode(200);
            respon.setMessage("Quiz answer receive successfully");



        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());

        }


        return respon;
    }

    @Transactional
    public void saveAns(QuizDTO quizDTO, String email) throws Exception {

        Quiz quiz = quizRepo.findByRoomCode(quizDTO.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Error submitting answer"));

        if ("END".equalsIgnoreCase(quiz.getStatus())) {
            throw new RuntimeException("The quiz have Ended");
//            scheduler.shut
        }

        Question questionQuiz = questionRepo.findById(quizDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Error of finding question"));

        User user = userRepo.findByEmail(email).orElseThrow();

        GamePlayer gamePlayer = gamePlayerRepo
                .findByQuizSessionIdAndParticipantIdForUpdate(quiz.getId(), user.getId())
                //This for handle Database race condition which it use For Update this keyword and it lock the row
                //
                // example
//                Thread A gets lock
//                Thread B waits
//
//                Thread A:
//                10 + 5 = 15
//                commit
//
//                Thread B:
//                reads 15
//                15 + 3 = 18
//                commit
                .orElseThrow(() -> new RuntimeException("Error of finding user"));

        if (gamePlayer.isHasSubmitted()) {
            throw new RuntimeException("Player already submitted final quiz");
        }

        double gradeAnswerScore = aiGradingService.gradeAnswer(
                questionMapper.toDTO(questionQuiz), quizDTO.getParticipantAns());

        gamePlayer.setScore(gamePlayer.getScore() + gradeAnswerScore);

        if ("SUBMIT".equalsIgnoreCase(quizDTO.getStatus())) {
            gamePlayer.setHasSubmitted(true);
        }

        gamePlayer.setCurrentQuestionIndex(quizDTO.getQuestionIndex());
    }

    public ReqRes getleaderboard(String roomCode)
    {

        ReqRes respon = new ReqRes();


        try{

           List <GamePlayer> quizSessionListGamePlayer = gamePlayerRepo.findByQuizSession_RoomCodeOrderByScoreDesc(roomCode);

           List<LeaderBoardDTO> rankingfunc = leaderBoardMapper.toListDTO(quizSessionListGamePlayer);
//           for(int i =0; i < rankingfunc.size(); i ++)
//           {
//               rankingfunc.get(i).setRank(i + 1);
//           }
//           respon.setLeaderBoardDTOList(rankingfunc);

            IntStream.range(0, rankingfunc.size())
                            .forEach(i -> rankingfunc.get(i).setRank(i+1));


            respon.setLeaderBoardDTOList(rankingfunc);
            respon.setStatusCode(200);
            respon.setMessage("Quiz answer receive successfully");


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return respon;
    }


    public ReqRes getParticipantResult(String roomCode, String email)
    {

        ReqRes respon = new ReqRes();

        try{

            Quiz quiz = quizRepo.findByRoomCode(roomCode).orElseThrow();
            User user = userRepo.findByEmail(email).orElseThrow();

           GamePlayer gamePlayer = gamePlayerRepo.findByQuizSessionIdAndParticipantId(quiz.getId(), user.getId())
                   .orElseThrow( () -> new RuntimeException("Couldn't find and get player info "));

            // ← add this
            System.out.println("=== getParticipantResult: requestEmail=" + email
                    + " | foundOwner=" + gamePlayer.getParticipant().getEmail()
                    + " | score=" + gamePlayer.getScore());

           respon.setGamePlayerDTO(gamePlayerMapper.toDTO(gamePlayer));
           respon.setMessage("Player score receive successfully");
           respon.setStatusCode(200);

        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());

        }

        return respon;

    }



    public  ReqRes endQuizSession(String roomCode, String email)
    {

        ReqRes respon = new ReqRes();

        try{

            User user = userRepo.findByEmail(email).orElseThrow();
            Quiz quiz =quizRepo.findByRoomCode(roomCode).orElseThrow();

            if(quiz.getHost().getId() != user.getId())
            {
                respon.setStatusCode(403);
                respon.setError("Only the host can end the quiz");
                return respon;
            }

            //set time format
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            //update the quiz session
            quiz.setStatus("END");
            quiz.setEndedAt(dateTime.format(formatter));

            Quiz saved = quizRepo.save(quiz);

            webSocketController.broadcastQuizUpdate(roomCode,quizMapper.toDto(quiz));
            respon.setQuizDTO(quizMapper.toDto(saved));
            respon.setStatusCode(200);
            respon.setMessage("Quiz ended successfully");

        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError("An unexpected error occurred");
        }
        return respon;
    }



    public ReqRes  getQuizHistory(String email)
    {

        ReqRes respon = new ReqRes();


        try{

            User user = userRepo.findByEmail(email).orElseThrow();

            List<QuizHistoryDTO> quizHistoryDTOList = quizRepo.findByHost_Id(user.getId())
                    .stream()
                    .map(quiz -> {

                    // .stream() — opens a "pipeline", prepares to process one by one
                    // Nothing happens yet, just ready to flow

                    // .map(quiz -> { ... }) — for EACH item, run the block
                    // "quiz" is just a temporary variable, like a "current item holder"

                        QuizHistoryDTO dto = new QuizHistoryDTO();
                        dto.setQuizRole(user.getId() == quiz.getHost().getId()? "HOST" : "PARTICIPANT");
                        dto.setStatus(quiz.getStatus());
                        dto.setRoomCode(quiz.getRoomCode());
                        dto.setEndedAt(quiz.getEndedAt());
                        return dto;
                        //return tells .map() what the transformed result is for each item.
                    })
                    .toList();
            //// .toList() — collects all returned dtos into a list

                respon.setQuizHistoryDTOList(quizHistoryDTOList);
                respon.setStatusCode(200);
                respon.setMessage("Quiz history retrieved successfully");

        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }
        return  respon;
    }

    public ReqRes rejoin(String roomCode, String email)
    {
        ReqRes respon = new ReqRes();

        try{

            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid User"));

            Quiz quiz = quizRepo.findByRoomCode(roomCode).orElseThrow(() -> new RuntimeException("Invalid roomCode"));

            GamePlayer gamePlayer = gamePlayerRepo.findByQuizSessionIdAndParticipantId(quiz.getId(), user.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid Quiz Id and User Id"));


            QuizDTO quizDTO = quizMapper.toDto(quiz);
            quizDTO.setQuestionIndex(gamePlayer.getCurrentQuestionIndex());
//            gamePlayerRepo.save(gamePlayer);
            respon.setQuizDTO(quizDTO);
            respon.setStatusCode(200);
            respon.setMessage("Have Succesfully rejoin succesfully");


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());

        }

        return respon;

    }


    public String generateRoomCode()
    {
        String code;

        do{
            code = generateCode();
        }while (quizRepo.existsByRoomCode(code));

        return code;
    }

    public String generateCode()
    {
        String Character="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder roomCode = new StringBuilder();

        for ( int i = 0 ; i < 4 ; i++)
        {
            roomCode.append(Character.charAt(random.nextInt(Character.length())));
            //random.nextInt(chars.length()) Generate a random number from 0 to n(depend on how long is the string);
            //Character.charAt(3) the CHaracter is define on top on the random,
            // and this line mean Get the character at index [number]
            //roomCode.append() Now the String is added to the StringBuilder.
        }

        return roomCode.toString();
    }




}
