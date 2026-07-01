package com.example.FYP2.Inteface;

import com.example.FYP2.DTO.QuestionDTO;
import com.example.FYP2.Entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    Question toEntity(QuestionDTO dto);

    QuestionDTO toDTO(Question entity);

    List<QuestionDTO> toListDTO(List<Question> entity);

    @Mapping(target = "id", ignore = true)
    void updateQuestionFromDto( QuestionDTO dto ,@MappingTarget Question entity);

}
