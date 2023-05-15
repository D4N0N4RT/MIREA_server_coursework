package ru.mirea.server_coursework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.ShortPostDTO;
import ru.mirea.server_coursework.dto.GetUserDTO;
import ru.mirea.server_coursework.dto.UpdateUserDTO;
import ru.mirea.server_coursework.model.User;

import java.util.Collection;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDTO dto, @MappingTarget User entity);

    @Mapping(target = "registrationDate", source = "user.registrationDate")
    GetUserDTO toUserDto(User user, Collection<ShortPostDTO> postDTOCollection);

    /*@Mapping(target = "registrationDate", source = "user.registrationDate")
    List<GetUserDTO> toUserDto(Collection<User> users, PostMapper mapper);*/
}
