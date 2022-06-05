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
public class Draft {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Lob
    private String blog;

    private LocalDate publishedDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;
}
