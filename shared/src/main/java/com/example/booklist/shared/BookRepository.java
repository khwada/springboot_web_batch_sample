package com.example.booklist.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ka.wada on 2017/07/01.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
