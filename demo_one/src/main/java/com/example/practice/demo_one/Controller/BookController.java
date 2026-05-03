package com.example.practice.demo_one.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.example.practice.demo_one.service.BookService;
import com.example.practice.demo_one.service.entity.Book;


@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	BookService bservice;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    
	@GetMapping("/all")
	public ResponseEntity<?> getBooks()
	{
		logger.info("Entered into Book controller and trying to Getting the books");
		
		List<Book> bookList=bservice.getAllBooks();
		
		logger.info(" ** this is from Logger :: Entered into controller and GOT the books");
		
		return ResponseEntity.status(HttpStatus.OK).body(bookList);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findByID(@PathVariable long id )
	{
		
		System.out.println("Entered into controller and trying to get the given bookid");
		Optional<Book> book= bservice.findbyID(id);
		
		return  book.map(b -> ResponseEntity.status(HttpStatus.FOUND).body(book.get())).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());	 
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addBook(@RequestBody Book book)
	
	{
		System.out.println("Entered into controller and trying to add the book");
		Book addedBook=bservice.addBook(book);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
		
	}
	
	@GetMapping("/bookCache/{id}")
	public ResponseEntity<?> getBookFromCacheManual(@PathVariable Long id) {
	    Book cachedBook = bservice.getBookFromCacheManual(id);
	    if (cachedBook != null) {
	        return ResponseEntity.ok(cachedBook);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found in cache");
	    }
	}
}
