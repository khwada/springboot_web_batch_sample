package com.example.booklist.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by ka.wada on 2017/07/01.
 */
@Entity
@Data
public class Book {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String author;
    private Date registratedAt;
}
