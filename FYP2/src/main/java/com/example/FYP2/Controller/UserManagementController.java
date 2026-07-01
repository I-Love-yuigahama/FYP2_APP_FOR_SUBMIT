package com.example.FYP2.Controller;

import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Repository.UserRepo;
import com.example.FYP2.Service.UserDetailService;
import com.example.FYP2.Service.UserManagementService;
import com.example.FYP2.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@JsonView(Views.Admin.class)
@RestController
public class UserManagementController {


    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody UserDTO registerRequest)
    {
        return ResponseEntity.ok(userManagementService.register(registerRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody UserDTO loginRequest)
    {
        return  ResponseEntity.ok(userManagementService.login(loginRequest));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody UserDTO refreshTokenRequest)
    {
        return ResponseEntity.ok(userManagementService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/admin/get-all-user")
    public ResponseEntity<ReqRes>  getAllUsers()
    {
        return ResponseEntity.ok(userManagementService.getALLUser());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserId(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(userManagementService.getUserById(userId));
    }

    @DeleteMapping("/admin/delete-user/{userId}")
    public ResponseEntity<ReqRes> deleteById(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(userManagementService.deleteById(userId));
    }

    @PutMapping("/admin/update-user/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody UserDTO requestUpdateUser)
    {
        return ResponseEntity.ok(userManagementService.updateUser(userId,requestUpdateUser));
    }

//    @JsonView(Views.User.class)
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes respon = userManagementService.myProfile(email);
        return ResponseEntity.status(respon.getStatusCode()).body(respon);
    }

    @PutMapping("/user/update-profile")
    public ResponseEntity<ReqRes> updateMyProfile(@RequestBody UserDTO requestUpdateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        ReqRes respon = userManagementService.updateUser(user.getId(), requestUpdateUser);
        return ResponseEntity.status(respon.getStatusCode()).body(respon);
    }



}
