package com.example.booklist.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by ka.wada on 2017/07/01.
 */
@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public int registerAll(List<? extends Book> books) {
        for (Book book: books) {
            bookRepository.save(book);
        }
        logger.info("{} records inserted", books.size());
        return books.size();
    }
}
