    package com.example.FYP2.Inteface;


    import com.example.FYP2.DTO.GamePlayerDTO;
    import com.example.FYP2.Entity.GamePlayer;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    @Mapper(componentModel = "spring")
    public interface GamePlayerMapper {

        @Mapping(source = "participant.email", target = "userName")
        @Mapping(source = "participant.email", target = "userEmail")
        @Mapping(source = "quizSession.roomCode", target = "roomCode")
        GamePlayerDTO toDTO(GamePlayer entity);

        GamePlayer toEntity(GamePlayerDTO dto);


    }
