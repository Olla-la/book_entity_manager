package telran.java40.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java40.book.model.Book;

public interface BookRepository {
	Stream<Book> findByAuthorsName(String authorName);

	Stream<Book> findByPublisherName(String publisherName);
	
	Optional<Book> findById(Long isbn);
	
	Book save(Book book);
	
	void delete(Book book);
	
	boolean existsById(Long isbn);
	
}
