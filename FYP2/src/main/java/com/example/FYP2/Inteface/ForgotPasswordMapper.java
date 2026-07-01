package com.example.FYP2.Inteface;

import com.example.FYP2.DTO.MailBody;
import com.example.FYP2.Entity.ForgotPassword;
import jakarta.persistence.Entity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForgotPasswordMapper {

    MailBody toDto(ForgotPassword entity);

}
