package ru.mirea.server_coursework.dto;

import lombok.Getter;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
public class CreatePostDTO {
    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 100, message = "Название не может быть длинее 100 символов")
    private String title;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Цена не может отсутствовать")
    @Min(value = 1, message = "Цена должна быть не меньше 1")
    private double price;
    @NotNull(message = "Объявление не может быть без категории")
    private Long categoryId;
    private Boolean exchanged;
    private Boolean delivered;

    public Post toPost(User user, Category category) {
        return Post.builder()
                .title(title)
                .description(description)
                .price(price)
                .promotion(0)
                .sold(false)
                .category(category)
                .user(user)
                .city(user.getCity())
                .exchanged(exchanged)
                .delivered(delivered)
                .sellerRating(user.getRating())
                .postingDate(LocalDate.now())
                .buyerId(0L)
                .build();
    }
}
