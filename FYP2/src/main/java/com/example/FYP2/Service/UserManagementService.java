package com.example.FYP2.Service;


import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.User;
import com.example.FYP2.Inteface.UserMapper;
import com.example.FYP2.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserManagementService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    // Account lockout settings
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 10;

    public ReqRes register(UserDTO registrationRequest)
    {
        ReqRes respon = new ReqRes();

        try {

            if(userRepo.findByEmail(registrationRequest.getEmail()).isPresent())
            {
                respon.setMessage("Email Already exist");
                respon.setStatusCode(400);
            }
            else
            {
                User user = userMapper.toEntity(registrationRequest);
                //this part is equal to
                // User user = new User();
                // user.setEmail(dto.getEmail());
                // user.setName(dto.getName());
                // password is ignored (because i told MapStruct to ignore it)

                user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                User userResult = userRepo.save(user);
                //Save this user into database

                if(userResult.getId() > 0)
                {
                    respon.setUserDTO(userMapper.toDTO(userResult));
                    respon.setMessage("User Saved Successfully");
                    respon.setStatusCode(200);
                }
            }

        }catch (Exception err)
        {
            log.error("Register failed", err); // ← full error goes to your logs only
            respon.setStatusCode(500);
            respon.setError("An unexpected error occurred");
        }

        return respon;

    }

    public ReqRes login(UserDTO loginRequest)
    {

        ReqRes respon = new ReqRes();

        try{

            // Check if account exists first
            Optional<User> userOptional = userRepo.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                respon.setStatusCode(401);
                respon.setError("Invalid email or password");
                return respon;
            }

            User user = userOptional.get();

            // Check if account is locked
            if (!user.isAccountNonLocked()) {
                long minutesRemaining = LocalDateTime.now().until(user.getLockUntil(), ChronoUnit.MINUTES) + 1;
                respon.setStatusCode(423); // 423 Locked
                respon.setError("Account is locked due to too many failed login attempts. Try again in " + minutesRemaining + " minute(s).");
                log.warn("Login attempt on locked account: {}", loginRequest.getEmail());
                return respon;
            }

            try {
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail()
                                , loginRequest.getPassword()));

                // Authentication successful — reset failed attempts
                user.setFailedLoginAttempts(0);
                user.setLockUntil(null);
                userRepo.save(user);

            } catch (Exception authEx) {
                // Authentication failed — increment failed attempts
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                log.warn("Failed login attempt {} for user: {}", user.getFailedLoginAttempts(), loginRequest.getEmail());

                if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                    user.setLockUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                    log.warn("Account locked for user: {} until {}", loginRequest.getEmail(), user.getLockUntil());
                }

                userRepo.save(user);

                respon.setStatusCode(401);
                if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                    respon.setError("Account locked for " + LOCK_DURATION_MINUTES + " minutes due to too many failed attempts.");
                } else {
                    int attemptsRemaining = MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts();
                    respon.setError("Invalid email or password. " + attemptsRemaining + " attempt(s) remaining before account lock.");
                }
                return respon;
            }

            var jwt = jwtUtils.generateToken(user);

            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            respon.setStatusCode(200);

            UserDTO userDTO =UserDTO.builder()
                    .token(jwt)
                    .role(user.getRole())
                    .expirationTime("15 Minutes")
                    .refreshtoken(refreshToken)
                    .build();

            respon.setUserDTO(userDTO);
            respon.setMessage("Sucessfully logged in ");


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            log.error("Login failed", err);
            respon.setError("An unexpected error occurred");
        }

        return respon;

    }

    public ReqRes refreshToken(UserDTO refreshTokenRequest)
    {

        ReqRes respon = new ReqRes();

        try {

            String userEmail = jwtUtils.extractUsername(refreshTokenRequest.getRefreshtoken());
           User user = userRepo.findByEmail(userEmail).orElseThrow();

           if(jwtUtils.isTokenValid(refreshTokenRequest.getRefreshtoken(), user))
           {
                var jwt = jwtUtils.generateToken(user);
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

                UserDTO userDTO = userMapper.toDTO(user);
                userDTO.setToken(jwt);
                userDTO.setRefreshtoken(refreshToken);
                userDTO.setExpirationTime("15 Minutes");

                respon.setUserDTO(userDTO);
                respon.setStatusCode(200);
                respon.setMessage("Sucessfully logged in ");
           }

        }catch (Exception err)
        {
            log.error("Refresh token failed", err);
            respon.setError("Token refresh failed");
            respon.setStatusCode(500);
        }

        return respon;

    }

    public ReqRes getALLUser()
    {
        ReqRes respon = new ReqRes();

        try{

            List<User>  userList= userRepo.findAll();

            if(!userList.isEmpty())
            {
                respon.setUserDTOList(userMapper.toListDTO(userList));
                respon.setStatusCode(200);
                respon.setMessage("Sucessfully get User List ");
            }

            else
            {
                respon.setStatusCode(404);
                respon.setMessage("Failed to Catch User List");
            }

            return respon;

        }catch (Exception err)
        {
            respon.setError("An unexpected error occurred");
            log.error("Get all users failed", err);
            respon.setStatusCode(500);
        }

        return respon;
    }

    public ReqRes getUserById(Integer id)
    {
        ReqRes respon = new ReqRes();

        try{

            User userDetail = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));

            respon.setUserDTO(userMapper.toDTO(userDetail));
            UserDTO userDTO = userMapper.toDTO(userDetail);
            System.out.println("Mapped DTO: " + userDTO);
            respon.setMessage("User with id '" + id + "' found successfully");
            respon.setStatusCode(200);


        }catch (Exception err)
        {
            respon.setStatusCode(500);
            log.error("Get user by id failed", err);
            respon.setError("An unexpected error occurred");
        }

        return respon;

    }

    public ReqRes deleteById(Integer id)
    {
        ReqRes respon = new ReqRes();

        try{
            Optional<User> userOptional = userRepo.findById(id);
            if(userOptional.isPresent())
            {
                userRepo.deleteById(id);
                respon.setStatusCode(200);
                respon.setMessage("User "+id+" delete successfully");
            }else {
                respon.setStatusCode(400);
                respon.setMessage("User not found");
            }

        }catch (Exception err)
        {
            log.error("Delete user failed", err);
            respon.setStatusCode(500);
            respon.setError("An unexpected error occurred");
        }
        return respon;
    }

    public ReqRes updateUser(Integer id,UserDTO requestUpdateUser)
    {
        ReqRes respon = new ReqRes();
        User userResult;

        log.info("updateUser called with id: {}, data: {}", id, requestUpdateUser); // ← add this

        try{

            Optional<User> userOptional = userRepo.findById(id);
            if(userOptional.isPresent())
            {
                User updateUserDetail = userOptional.get();
                userMapper.updateUserFromDto(requestUpdateUser,updateUserDetail);

                if(requestUpdateUser.getPassword() != null && !requestUpdateUser.getPassword().isEmpty())
                {
                    updateUserDetail.setPassword(passwordEncoder.encode(requestUpdateUser.getPassword()));
                    userResult = userRepo.save(updateUserDetail);
                }
                else
                {
                    userResult = userRepo.save(updateUserDetail);
                }

                respon.setUserDTO(userMapper.toDTO(userResult));
                respon.setMessage("Update successfully");
                respon.setStatusCode(200);

            }else{
                respon.setMessage("Update Unsuccessfully due user not found");
                respon.setStatusCode(400);
            }

        }catch (Exception err)
        {

            log.error("Update user failed", err);
            respon.setStatusCode(500);
            respon.setError("An unexpected error occurred");

        }

        return respon;
    }

    public ReqRes myProfile(String userEmail)
    {
        ReqRes respon = new ReqRes();
        User user;
        try{

            Optional<User> optionalUser= userRepo.findByEmail(userEmail);
            if(optionalUser.isPresent())
            {
                user = optionalUser.get();
                respon.setUserDTO(userMapper.toDTO(user));
                respon.setMessage("Update found user");
                respon.setStatusCode(200);
            }else
            {
                respon.setMessage("User not found");
                respon.setStatusCode(400);
            }

        }catch (Exception err){
            log.error("Get profile failed", err);
            respon.setStatusCode(500);
            respon.setError("An unexpected error occurred");
        }

        return respon;
    }



}
