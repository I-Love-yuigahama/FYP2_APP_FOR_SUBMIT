package com.example.FYP2.Service;

import com.example.FYP2.DTO.MailBody;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.ForgotPassword;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Inteface.UserMapper;
import com.example.FYP2.Repository.ForgotPasswordRepo;
import com.example.FYP2.Repository.UserRepo;
import jakarta.mail.internet.MimeMessage;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private ForgotPasswordRepo forgotPasswordRepo;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private JavaMailSender mailSender;

    public ReqRes receivedUserEmail(String email)
    {
            ReqRes respon = new ReqRes();

            try
            {
                    User user = userRepo.findByEmail(email)
                    .orElseThrow( () -> new UsernameNotFoundException("Please provide the valid email "));

                    // Use orphanRemoval — just nullify and save the user
                    if (user.getForgotPassword() != null) {
                        user.setForgotPassword(null);
                        userRepo.save(user);  // orphanRemoval will auto-delete the ForgotPassword row
                    }

                    int otp = otpGenerator();

                String html = """
                <html>
                    <body>
                        <p>This is the OTP for your Forgot Password request:</p>
                        
                        <h1 style="color:black; font-size:36px;">
                            <b>%s</b>
                        </h1>
                    </body>
                </html>
                """.formatted(otp);

            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text(html)
                    .subjective("OTP forgot password request")
                    .build();

            ForgotPassword fp = ForgotPassword
                    .builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                    .user(user)
                    .build();

            sendSimpleMessage(mailBody);
            respon.setMailBody(mailBody);
            forgotPasswordRepo.save(fp);
            respon.setMessage("Email verificaiton send succesfully");
            respon.setStatusCode(200);
            return respon;

            }catch (Exception e) {
                e.printStackTrace(); // This will show in your Spring console
                respon.setStatusCode(500);
                respon.setMessage("Error: " + e.getMessage());
                return respon;
            }


    }

    public void sendSimpleMessage(MailBody mailBody)
    {
//        ReqRes reqRes= new ReqRes();

        try
        {
//            SimpleMailMessage message = new SimpleMailMessage();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

            mimeMessageHelper.setTo(mailBody.to());
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setSubject(mailBody.subjective());
            mimeMessageHelper.setText(mailBody.text(),true);

            mailSender.send(mimeMessage);
    //        reqRes.setMailBody(mailBody);

        }catch (Exception e)
        {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public ReqRes verifyOtp(Integer otp, String email)
    {
        ReqRes respon = new ReqRes();

        User user = userRepo.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("Please provide the valid email "));

       ForgotPassword fp = forgotPasswordRepo.findByOtpAndUser(otp,user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for Email: "+email));

       if(fp.getExpirationTime().before(Date.from(Instant.now())))
       {
           forgotPasswordRepo.deleteById(fp.getForget_password_id());
           respon.setMessage("OTP has expired");
           respon.setStatusCode(400);
           return respon;
       }

        fp.setVerified(true);
       forgotPasswordRepo.save(fp);

        respon.setMessage("OTP verified");
        respon.setStatusCode(200);
        return respon;



    }

    public ReqRes changedPassword(UserDTO requestUpdateUser)
    {
        ReqRes respon = new ReqRes();

            User user = userRepo.findByEmail(requestUpdateUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("could not find User email"));

        ForgotPassword fp = forgotPasswordRepo.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("No OTP request found. Please verify your email first"));

            if(!fp.isVerified())
            {
                respon.setMessage("OTP has not been verified. Please complete OTP verification first");
                respon.setStatusCode(403);
                return respon;
            }

         ReqRes updateRespon= userManagementService.updateUser(user.getId(), requestUpdateUser);

            if(updateRespon.getStatusCode() == 200) {
                user.setForgotPassword(null);
                userRepo.save(user);
            }
         respon.setMessage(updateRespon.getMessage());
         respon.setStatusCode(updateRespon.getStatusCode());
         return respon;
    }


    //helper func
    private Integer otpGenerator()
    {
        Random random= new Random();
        return random.nextInt(100_000,999_999);
    }

}
