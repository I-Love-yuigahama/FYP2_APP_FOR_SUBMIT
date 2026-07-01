package com.example.FYP2.Inteface;

import com.example.FYP2.DTO.UserDTO;
import com.example.FYP2.Entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {



    @Mapping(target = "id",ignore = true)
    @Mapping(target = "password",ignore = true)
    User toEntity(UserDTO dto);


    @Mapping(target = "password",ignore = true)
    @Mapping(target = "token",ignore = true)
    @Mapping(target = "refreshtoken",ignore = true)
    @Mapping(target = "expirationTime",ignore = true)
    UserDTO toDTO(User  userEntity);


    // with @BeanMapping — add extra rules
//    nullValuePropertyMappingStrategy — "what should I do when a DTO field is null?":
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)  // ← add this
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "password",ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
    //@MappingTarget User entity
    //tells MapStruct: “Don’t create a new User
    //Update THIS exact object instead”



    List<UserDTO> toListDTO(List<User> userEntityList);
    //MapStruct works like this:
    //Single object mapping = main rule
    // List mapping = just loop and reuse single mapping

}
