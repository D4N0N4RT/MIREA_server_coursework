package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.server_coursework.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
