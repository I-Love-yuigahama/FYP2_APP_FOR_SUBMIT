package com.example.FYP2.Inteface;


import com.example.FYP2.DTO.QuizDTO;
import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.Question;
import com.example.FYP2.Entity.Quiz;
import com.example.FYP2.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GamePlayerMapper.class,UserMapper.class})
public interface QuizMapper {


    @Mapping(source = "gamePlayerList", target = "gamePlayerDTOList")
    @Mapping(source = "questions", target = "questionDTOList")
    QuizDTO toDto(Quiz entity);


    @Mapping(source = "gamePlayerDTOList", target = "gamePlayerList")
    @Mapping(source = "questionDTOList", target = "questions")
    Quiz toEntity(QuizDTO dto);



}
