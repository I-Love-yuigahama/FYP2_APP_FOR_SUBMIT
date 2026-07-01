package com.example.FYP2.Repository;

import com.example.FYP2.Entity.ForgotPassword;
import com.example.FYP2.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Integer> {

    Optional<ForgotPassword> findByOtpAndUser(Integer Otp, User user);

    Optional<ForgotPassword> findByUser(User user);


}
