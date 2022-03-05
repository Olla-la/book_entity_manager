package telran.java40.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import telran.java40.book.model.Book;


@Repository
public class BookRepositoryImpl implements BookRepository {

	@PersistenceContext
	EntityManager em;


	@Override
	public Optional<Book> findById(Long id) {
		Book book = em.find(Book.class, id);
		return Optional.ofNullable(book);
	}



	@Override
	public Stream<Book> findByAuthorsName(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Book> findByPublisherName(String publisherName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book save(Book book) {
		em.persist(book);
		return book;
	}

	@Override
	public void delete(Book book) {
		em.remove(book);

	}

	@Override
	public boolean existsById(Long isbn) {
		Book book = em.find(Book.class, isbn);
		return book!=null;
	}

}
