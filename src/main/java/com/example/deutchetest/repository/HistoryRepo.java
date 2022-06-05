package com.example.deutchetest.repository;

import com.example.deutchetest.entity.History;
import com.example.deutchetest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HistoryRepo extends JpaRepository<History, Integer> {

    @Transactional
    List<History> findAllByUser(User user);

}
