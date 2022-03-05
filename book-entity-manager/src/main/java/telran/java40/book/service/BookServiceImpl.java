package telran.java40.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.java40.book.dao.AuthorRepository;
import telran.java40.book.dao.BookRepository;
import telran.java40.book.dao.PublisherRepository;
import telran.java40.book.dto.AuthorDto;
import telran.java40.book.dto.BookDto;
import telran.java40.book.dto.exceptions.EntityNotFoundException;
import telran.java40.book.model.Author;
import telran.java40.book.model.Book;
import telran.java40.book.model.Publisher;

@Service
public class BookServiceImpl implements BookService {
	BookRepository bookRepository;
	AuthorRepository authorRepository;
	PublisherRepository publisherRepository;
	ModelMapper modelMapper;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
			PublisherRepository publisherRepository, ModelMapper modelMapper) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.publisherRepository = publisherRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if(bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		//Publisher
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElseGet(()->publisherRepository.save(new Publisher(bookDto.getPublisher())));
		//Authors
		Set<Author> authors = bookDto.getAuthors().stream()
								.map(a -> authorRepository.findById(a.getName())//orElseGet - better then orElse because of optiomization and it doesn`t rewrite existent Obj and act after 1st part only
										.orElseGet(()->authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
								.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher );
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(Long isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBook(Long isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		bookRepository.delete(book);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(Long isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
//	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
//		return bookRepository.findByAuthorsName(authorName)
//				.map(b -> modelMapper.map(b, BookDto.class))
//				.collect(Collectors.toList());
		Author author = authorRepository.findById(authorName).orElseThrow(() -> new EntityNotFoundException());
		return author.getBooks().stream()
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
//	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
//		return bookRepository.findByPublisherPublisherName(publisherName)
//				.map(b -> modelMapper.map(b, BookDto.class))
//				.collect(Collectors.toList());
		Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(() -> new EntityNotFoundException());
		return publisher.getBooks().stream()
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<AuthorDto> findBookAuthors(Long isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		return book.getAuthors().stream()
				.map(a -> modelMapper.map(a, AuthorDto.class))
				.collect(Collectors.toList());
	}

	@Override
	//@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String authorName) {
//		return publisherRepository.findPublisherNameByAuthor(authorName);
		//return publisherRepository.findDistinctByBooksAuthorsName(authorName)
		return publisherRepository.findPublisherNameByAuthor(authorName);
				//.map(Publisher::getPublisherName)
				//.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(() -> new EntityNotFoundException());
//		bookRepository.findByAuthorsName(authorName)
//						.forEach(b -> bookRepository.delete(b));
		authorRepository.delete(author);
		return modelMapper.map(author, AuthorDto.class);
	}
}
