package com.example.deutchetest.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Lob
    private String blog;

    private LocalDate publishedDate;

    @ManyToOne
    private User user;

    private String author;

    @ManyToOne
    private History history;

    public Article(String blog, LocalDate publishedDate, User user) {
        this.blog = blog;
        this.publishedDate = publishedDate;
        this.user = user;
    }
}
