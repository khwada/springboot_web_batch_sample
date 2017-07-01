package com.example.booklist.batch;

import com.example.booklist.shared.Book;
import com.example.booklist.shared.BookService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Date;
import java.util.List;

/**
 * Created by ka.wada on 2017/07/01.
 */
@Configuration
@EnableBatchProcessing
public class BooklistImportBatch {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    BookService bookService;

    @Bean
    public FlatFileItemReader<Book> reader() {
        FlatFileItemReader<Book> reader = new FlatFileItemReader<>();
        reader.setEncoding("UTF-8");
        reader.setResource(new ClassPathResource("csv/books.csv"));
        reader.setLineMapper(new DefaultLineMapper<Book>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer() {{
                    setNames(new String[]{"title", "author"});
                }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {{
                    setTargetType(Book.class);
                }});
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Book, Book> processor() {
        ItemProcessor<Book, Book> processor = new ItemProcessor<Book, Book>() {
            @Override
            public Book process(Book item) throws Exception {
                Book book = new Book();
                book.setTitle(item.getTitle());
                book.setAuthor(item.getAuthor());
                book.setRegistratedAt(new Date());
                return book;
            }
        };
        return processor;
    }

    @Bean
    public ItemWriter<Book> writer() {
        ItemWriter<Book> writer = new ItemWriter<Book>() {
            @Override
            public void write(List<? extends Book> items) throws Exception {
                bookService.registerAll(items);
            }
        };
        return writer;
    }

    @Bean
    public JobExecutionListener lister() {
        return new JobStartEndListner();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Book, Book>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("booklist_import")
                .incrementer(new RunIdIncrementer())
                .listener(lister())
                .flow(step1())
                .end()
                .build();
    }
}
