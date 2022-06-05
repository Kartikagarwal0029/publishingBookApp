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
public class History {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Lob
    private String blog;

    @Lob
    private LocalDate localDate;

    @ManyToOne
    private User user;

    public History(String blog, LocalDate localDate, User user) {
        this.blog = blog;
        this.localDate = localDate;
        this.user = user;
    }
}
