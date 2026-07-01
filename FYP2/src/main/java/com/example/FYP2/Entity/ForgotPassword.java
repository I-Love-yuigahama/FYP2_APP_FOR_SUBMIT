package com.example.FYP2.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer forget_password_id;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    // Tracks whether the OTP has been successfully verified
    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
