package com.lalbrecht.mediasite.repositories;

import com.lalbrecht.mediasite.models.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends CrudRepository<Post, String> {
    @Query(value = "SELECT stars FROM posts WHERE post_id = ?1", nativeQuery = true)
    int getPostLikes(String pid);
    @Transactional
    @Modifying
    @Query(value = "UPDATE posts SET stars = ?2 WHERE post_id = ?1", nativeQuery = true)
    void starPost(String id, int stars);
}
