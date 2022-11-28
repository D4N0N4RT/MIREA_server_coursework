package ru.mirea.server_coursework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.FullPostDTO;
import ru.mirea.server_coursework.dto.ShortPostDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.model.Post;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "sellerEmail", source = "post.user.username")
    ShortPostDTO toShortPostDto(Post post);

    @Mapping(target = "sellerEmail", source = "post.user.username")
    List<ShortPostDTO> toShortPostDto(Collection<Post> posts);

    @Mapping(target = "sellerEmail", source = "post.user.username")
    @Mapping(target = "sellerRating", source = "post.sellerRating")
    FullPostDTO toFullPostDto(Post post);

    @Mapping(target = "sellerEmail", source = "post.user.username")
    List<FullPostDTO> toFullPostDto(Collection<Post> posts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(UpdatePostDTO dto, @MappingTarget Post entity);
}
