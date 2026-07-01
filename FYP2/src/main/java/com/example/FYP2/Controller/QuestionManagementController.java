package com.example.FYP2.Controller;

import com.example.FYP2.DTO.QuestionDTO;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.Service.QuestionService;
import com.example.FYP2.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@JsonView(Views.Admin.class)
@RestController
public class QuestionManagementController {

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/admin/add-question", consumes = "multipart/form-data")
    public ResponseEntity<?> addQuestion(@RequestPart QuestionDTO questionDTO
                                        ,@RequestPart(value="image", required=false) MultipartFile image)
    throws Exception
    {
        if(image != null && !image.isEmpty())
        {
            questionDTO.setImage(image.getBytes());
        }
            return ResponseEntity.ok(questionService.addQuestion(questionDTO));
    }


    @GetMapping("/admin/get-all-question")
    public ResponseEntity<ReqRes> getALlQuestion()
    {
        return ResponseEntity.ok(questionService.getALlQuestion());
    }

    @GetMapping("/admin/get-question/{questionId}")
    public ResponseEntity<ReqRes> getQuestionById(@PathVariable Integer questionId)
    {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @DeleteMapping("/admin/delete-question/{questionId}")
    public ResponseEntity<ReqRes> deleteQuestionById(@PathVariable Integer questionId)
    {
        return ResponseEntity.ok(questionService.deleteQuestionById(questionId));
    }

    @PostMapping(value = "/admin/update-question/{questionId}", consumes = "multipart/form-data")
    public ResponseEntity<ReqRes> updateQuestionById(@PathVariable Integer questionId,
                                                     @RequestPart QuestionDTO requestUpdateQuestion,
                                                     @RequestPart(value="image", required=false) MultipartFile image)
            throws Exception
    {
        if(image != null && !image.isEmpty())
        {
            requestUpdateQuestion.setImage(image.getBytes());
        }
        return ResponseEntity.ok(questionService.updateQuestionById(questionId,requestUpdateQuestion));
    }


}
