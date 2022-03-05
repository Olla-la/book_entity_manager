package telran.java40.book.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java40.book.model.Author;

public interface AuthorRepository {
	Optional <Author> findById(String authorId);
	
	void delete(Author author);
	
	Author save(Author author);

}
