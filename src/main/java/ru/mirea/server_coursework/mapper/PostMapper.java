package ru.mirea.server_coursework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.GetPostDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.model.Post;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "userEmail", source = "post.user.username")
    @Mapping(target = "sellerRating", source = "post.sellerRating")
    GetPostDTO toPostDto(Post post);

    @Mapping(target = "userEmail", source = "post.user.username")
    @Mapping(target = "sellerRating", source = "post.sellerRating")
    List<GetPostDTO> toPostDto(Collection<Post> posts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(UpdatePostDTO dto, @MappingTarget Post entity);
}
