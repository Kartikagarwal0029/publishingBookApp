package com.example.deutchetest.repository;


import com.example.deutchetest.entity.Article;
import com.example.deutchetest.entity.Draft;
import com.example.deutchetest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Integer> {

    @Transactional
    List<Article> findAllByUser(User user);

    @Transactional
    List<Article> findAllByAuthor(String email);

    @Transactional
    void deleteById(int id);

}
