package com.example.deutchetest.repository;

import com.example.deutchetest.entity.Draft;
import com.example.deutchetest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DraftRepo extends JpaRepository<Draft, Integer> {

    @Transactional
    List<Draft> findAllByUser(User user);

    @Transactional
    void deleteById(int id);
}
