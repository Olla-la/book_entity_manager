package telran.java40.book.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
		TypedQuery<Book> query = em.createQuery("select distinct b from Book b join b.authors a where a.name=?1", Book.class);
		query.setParameter(1,  authorName);
		return query.getResultStream();
	}

	@Override
	public Stream<Book> findByPublishersName(String publisherName) {
		TypedQuery<Book> query = em.createQuery("select b from Book b where b.publisher.publisherName=?1", Book.class);
		query.setParameter(1,  publisherName);
		return query.getResultStream();
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
