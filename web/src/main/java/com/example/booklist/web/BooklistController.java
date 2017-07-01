package com.example.booklist.web;

import com.example.booklist.shared.Book;
import com.example.booklist.shared.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

/**
 * Created by ka.wada on 2017/07/01.
 */
@Controller
public class BooklistController {

    @Autowired
    BookService bookService;

    @GetMapping("/")
    public String booklist(Model model) {
        model.addAttribute("booklist", bookService.findAll());
        return "index";
    }
}
