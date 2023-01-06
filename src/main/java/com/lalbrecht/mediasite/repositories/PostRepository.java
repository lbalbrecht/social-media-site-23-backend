package com.lalbrecht.mediasite.repositories;

import com.lalbrecht.mediasite.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, String> {
}
