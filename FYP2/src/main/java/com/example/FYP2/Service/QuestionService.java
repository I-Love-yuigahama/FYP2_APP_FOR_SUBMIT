package com.example.FYP2.Service;

import com.example.FYP2.DTO.QuestionDTO;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.Entity.Question;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Inteface.QuestionMapper;
import com.example.FYP2.Repository.QuestionRepo;
import com.example.FYP2.Repository.QuizRepo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuestionMapper questionMapper;


    @Autowired
    private QuizRepo quizRepo;


    public ReqRes addQuestion(QuestionDTO questionDTO)
    {
        ReqRes respon = new ReqRes();

        try{

            Question newQuestion = questionMapper.toEntity(questionDTO);
            Question neqQuestionResult = questionRepo.save(newQuestion);
            respon.setStatusCode(200);
            respon.setMessage("Question added successfully");

        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return respon;
    }

    public ReqRes getALlQuestion()
    {
        ReqRes respon = new ReqRes();

        try{

            List<Question> questionList =  questionRepo.findAll();

            if (!questionList.isEmpty())
            {
                respon.setQuestionDTOList(questionMapper.toListDTO(questionList));
                respon.setMessage("Get Question List successfully");
                respon.setStatusCode(200);
            }

            else
            {
                respon.setMessage("Fail to Get Question List");
                respon.setStatusCode(400);
            }


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return  respon;

    }

    public ReqRes getQuestionById(Integer id)
    {
        ReqRes respon = new ReqRes();

        try{

            Question question = questionRepo.findById(id).orElseThrow();
            respon.setQuestionDTO(questionMapper.toDTO(question));
            respon.setMessage("Get Question successfully");
            respon.setStatusCode(200);


        }catch (Exception err){

            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }

        return  respon;

    }

    public ReqRes deleteQuestionById(Integer id)
    {

        ReqRes respon = new ReqRes();

        try
        {
            Optional<Question> questionOptional = questionRepo.findById(id);

            if(questionOptional.isPresent())
            {
                Question question=questionOptional.get();
                List<Quiz> quizQuestionList = quizRepo.findByQuestions_Id(question.getId());


                for(Quiz quiz : quizQuestionList)
                {
//                    if(quiz.getQuestions().contains(question))
                    //The contains() method checks whether it contains in the list in this case is list.
//                        {
                                if(!quiz.getStatus().equals("END"))
                                {

                                    respon.setMessage("The question are Still processing in the Quiz Pls try again later");
                                    respon.setStatusCode(400);
                                    return respon;
                                }
//                        }
                }

                for(Quiz quiz : quizQuestionList)
                {
                    quiz.getQuestions().remove(question);
                    quizRepo.save(quiz);
                }
                questionRepo.deleteById(id);
                respon.setMessage("delete question successfully");
                respon.setStatusCode(200);

            }else
            {
                respon.setMessage("fail to delete question");
                respon.setStatusCode(400);
            }

        }catch (Exception err){

            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }
            return respon;
    }

    public ReqRes updateQuestionById(Integer id, QuestionDTO requestUpdateQuestion)
    {

        ReqRes respon = new ReqRes();
        try
        {

            Optional<Question> questionOptional = questionRepo.findById(id);

            if(questionOptional.isPresent())
            {
                Question updateQuestionDetail = questionOptional.get();
                questionMapper.updateQuestionFromDto(requestUpdateQuestion, updateQuestionDetail);
                respon.setQuestionDTO(questionMapper.toDTO(questionRepo.save(updateQuestionDetail)));
                respon.setStatusCode(200);
                respon.setMessage("Question updated successfully");
            }else {
                respon.setMessage("fail to Update question");
                respon.setStatusCode(400);
            }

        }catch (Exception err)
        {
            respon.setStatusCode(500);
            respon.setError(err.getMessage());
        }
        return respon;
    }

}
