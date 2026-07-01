package com.example.FYP2;

import com.example.FYP2.DTO.QuestionDTO;
import com.example.FYP2.Entity.Question;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Inteface.QuestionMapper;
import com.example.FYP2.Repository.QuestionRepo;
import com.example.FYP2.Repository.QuizRepo;
import com.example.FYP2.Repository.UserRepo;
import com.example.FYP2.Service.AiGradingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class testing {

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AiGradingService aiGradingService;

    @Autowired
    private QuestionRepo questionRepo;


    private static final int[] questionIds ={5};

    private static final String[] StudentTestingAns ={
            "Protein R is a tough, fibrous membrane that forms the outer cell wall of the plant, acting like a protective skin to keep bacteria out.",
//            "xylem tubes",
    };
    // ─────────────────────────────────────────
    // Answer test
    // ─────────────────────────────────────────

    @Test
    void testAnswer() throws Exception{


//        ArrayList<Question> questionIdsArrayList = new ArrayList<>();
//
//        for(int questionId : questionIds)
//        {
//            Question questionList = questionRepo.findById(questionId).orElseThrow( () -> new RuntimeException("Error of finding question"));
//            if(!questionList.getQuestionText().isBlank())
//            {
//                questionIdsArrayList.add(questionList);
//            }
//            else
//            {
//                continue;
//            }
//        }

        System.out.println("hello this is testing function ");

        for(int i=0; i<questionIds.length; i++)
        {
            QuestionDTO dto = questionMapper.toDTO(questionRepo.findById(questionIds[i])
                    .orElseThrow( () -> new RuntimeException("Error of finding question")));
            String StudentAns = StudentTestingAns[i];


            System.out.printf("This is the Question: %s %n",dto.getQuestionText());
            System.out.printf("This is the Ans Text: %s %n",dto.getAnswerText());
            System.out.printf("This is the Keyword: %s  %n",dto.getKeyword());
            System.out.printf("This is the MaxMark: %s  %n",dto.getMaxMark());
            System.out.printf("This is the testing of the Ai grading For one keyword only The marks: %.2f %n \n",aiGradingService.gradeAnswer(dto,StudentAns));
        }

    }


}
