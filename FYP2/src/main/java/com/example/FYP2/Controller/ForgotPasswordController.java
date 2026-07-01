package com.example.FYP2.Controller;

import com.example.FYP2.DTO.MailBody;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.ForgotPassword;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Repository.ForgotPasswordRepo;
import com.example.FYP2.Repository.UserRepo;
import com.example.FYP2.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private   EmailService emailService;

    @Autowired
    private   ForgotPasswordRepo forgotPasswordRepo;

    //send mail for verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<ReqRes> verifyEmail(@PathVariable String email)
    {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow( () -> new UsernameNotFoundException("Please provide the valid email "));
        return ResponseEntity.ok(emailService.receivedUserEmail(email));
    }

    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<ReqRes> verifyOtp(@PathVariable Integer otp, @PathVariable String email)
    {

        return ResponseEntity.ok(emailService.verifyOtp(otp,email));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ReqRes> verifyOtp(@RequestBody UserDTO requestUpdateUser)
    {

        return ResponseEntity.ok(emailService.changedPassword(requestUpdateUser));
    }





}
