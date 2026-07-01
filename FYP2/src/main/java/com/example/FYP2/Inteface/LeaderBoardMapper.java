package com.example.FYP2.Inteface;

import com.example.FYP2.DTO.LeaderBoardDTO;
import com.example.FYP2.Entity.GamePlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaderBoardMapper {


    @Mapping(source = "participant.email", target = "username") // ✅ GamePlayer.participant.email → username
    @Mapping(source = "score", target = "score")               // ✅ GamePlayer.score → score
    @Mapping(target = "rank", ignore = true)                   // ✅ rank is set manually in service
    LeaderBoardDTO toDTO(GamePlayer entity);

    List<LeaderBoardDTO> toListDTO (List<GamePlayer> entity);


}
