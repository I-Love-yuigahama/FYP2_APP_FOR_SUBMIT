package com.example.FYP2.DTO;

import com.example.FYP2.Entity.User;
import com.example.FYP2.Views;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor // Required for Jackson deserialization
@AllArgsConstructor // Required for @Builder to work with @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)//When converting Java →
                                            // JSON,DO NOT include fields that are null.
@JsonIgnoreProperties(ignoreUnknown = true)
//If the incoming JSON contains extra fields that do not exist in this class, ignore them
public class UserDTO {

    @JsonView(Views.Admin.class)
    private Integer  id;

    private String token;

    private String refreshtoken;

    private String expirationTime;

    private String name;

    private String email;


    private String password  ;


    private String role;


}
