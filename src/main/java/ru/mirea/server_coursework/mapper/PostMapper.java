package ru.mirea.server_coursework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.model.Post;

@Component
@Mapper(componentModel = "spring")
public interface PostMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(UpdatePostDTO dto, @MappingTarget Post entity);
}
