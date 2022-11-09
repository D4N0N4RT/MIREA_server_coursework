package ru.mirea.server_coursework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.server_coursework.dto.GetPostDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @SequenceGenerator(name = "postsIdSeq",
            sequenceName = "posts_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postsIdSeq")
    @Column(name = "id", updatable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String title;

    private String description;

    private double price;

    private double promotion;

    private boolean sold;

    private int rating;

    @Column(name="posting_date")
    private LocalDate postingDate;

    @Enumerated(EnumType.STRING)
    private Category category;

    public GetPostDTO toDTO() {
        return GetPostDTO.builder().id(this.id).title(this.title).category(this.category)
                .price(this.price).userEmail(this.user.getUsername())
                .rating(this.rating).postingDate(this.postingDate)
                .description(this.description).build();
    }
}
