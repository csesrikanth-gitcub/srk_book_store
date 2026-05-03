package com.example.practice.demo_one.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import com.example.practice.demo_one.service.entity.Book;
import com.example.practice.demo_one.service.repository.BookRepository;


@Service
public class BookService {

	
	Book bookEntity;
	@Autowired
	BookRepository bookRepo;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	
	// Use parameter-index (#p0) to avoid relying on compiled parameter names
	@Cacheable(value = "book", key = "#p0")
	public Optional<Book> findbyID(Long id)
	{
		System.out.println("Entered into Find by BookID service API");
		Optional<Book> identifiedBook= bookRepo.findById(id) ;

		return identifiedBook;

	}
	
	@Cacheable("books")
	public List<Book> getAllBooks()
	{
		System.out.println("Entered into Get all books service API ");
		List<Book> bookList=bookRepo.findAll();
		
		
		return bookList;
		
	}

	// Evict the cached list and put the saved book into the per-id cache
	@CacheEvict(value = "books", allEntries = true)
	@CachePut(value = "book", key = "#result.bookId")
	public Book addBook(Book book) {
		// save to DB
		Book b1=bookRepo.save(book);

		return b1;
	}
	


	// Manual cache retrieval method
	public Book getBookFromCacheManual(Long id) {
		// The per-id cache is named "book" (see @Cacheable on findbyID)
		String key = "book::" + id;

		// Fetch value from Redis
		Object cachedValue = redisTemplate.opsForValue().get(key);

		System.out.println("Entered into Cached by BookID service, key=" + key + ", rawValue=" + cachedValue);

		if (cachedValue == null) {
			return null;
		}

		// If the serializer preserved type info, the object may already be a Book
		if (cachedValue instanceof Book) {
			return (Book) cachedValue;
		}

		// If the cached value was deserialized as a Map (common when type info isn't present), convert it
		if (cachedValue instanceof Map) {
			// convert map -> Book using ObjectMapper
			Book book = objectMapper.convertValue(cachedValue, Book.class);
			return book;
		}

		// As a last resort, try to convert JSON string to Book
		try {
			if (cachedValue instanceof String) {
				return objectMapper.readValue((String) cachedValue, Book.class);
			}
		} catch (Exception ex) {
			System.out.println("Failed to convert cached value to Book: " + ex.getMessage());
		}

		return null;
	}
}
