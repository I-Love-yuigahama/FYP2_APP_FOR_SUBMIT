package com.example.FYP2.Inteface;


import com.example.FYP2.DTO.QuizDTO;
import com.example.FYP2.DTO.QuizPublicDTO;
import com.example.FYP2.Entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GamePlayerMapper.class,UserMapper.class})
public interface QuizPublicMapper {

    @Mapping(source = "gamePlayerList", target = "gamePlayerDTOList")
    @Mapping(source = "questions", target = "questionPublicDTOList")
    QuizPublicDTO toDto(Quiz entity);




}
