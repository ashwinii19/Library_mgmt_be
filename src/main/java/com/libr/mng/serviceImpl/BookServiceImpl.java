package com.libr.mng.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.libr.mng.dto.request.BookRequestDTO;
import com.libr.mng.dto.request.BookUpdateRequestDTO;
import com.libr.mng.dto.response.BookResponseDTO;
import com.libr.mng.entity.Book;
import com.libr.mng.entity.Notification;
import com.libr.mng.entity.Waitlist;
import com.libr.mng.exception.ResourceNotFoundException;
import com.libr.mng.repository.BookRepository;
import com.libr.mng.repository.NotificationRepository;
import com.libr.mng.repository.WaitlistRepository;
import com.libr.mng.service.BookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final ModelMapper modelMapper;
	private final Cloudinary cloudinary;
	private final WaitlistRepository waitlistRepository;
	private final NotificationRepository notificationRepository;

	@Override
	public BookResponseDTO addBook(BookRequestDTO dto, MultipartFile image) throws IOException {

		if (bookRepository.findByIsbnNumber(dto.getIsbnNumber()).isPresent()) {
			throw new IllegalStateException("ISBN already exists");
		}

		Book book = modelMapper.map(dto, Book.class);

		if (image != null && !image.isEmpty()) {

			Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), Map.of());

			book.setImageUrl(uploadResult.get("secure_url").toString());
		}

		book.setAvailableCopies(dto.getTotalCopies());
		book.setBookStatus("AVAILABLE");

		Book savedBook = bookRepository.save(book);

		return modelMapper.map(savedBook, BookResponseDTO.class);
	}

	@Override
	@Transactional
	public BookResponseDTO updateBook(Long bookId, BookUpdateRequestDTO dto, MultipartFile image) throws IOException {

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id : " + bookId));

		Integer oldAvailableCopies = book.getAvailableCopies();

		if (dto.getTitle() != null && !dto.getTitle().trim().isEmpty()) {

			book.setTitle(dto.getTitle());
		}

		if (dto.getAuthor() != null && !dto.getAuthor().trim().isEmpty()) {

			book.setAuthor(dto.getAuthor());
		}

		if (dto.getCategory() != null && !dto.getCategory().trim().isEmpty()) {

			book.setCategory(dto.getCategory());
		}

		if (dto.getPublisher() != null && !dto.getPublisher().trim().isEmpty()) {

			book.setPublisher(dto.getPublisher());
		}

		if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {

			book.setDescription(dto.getDescription());
		}

		if (dto.getPublicationYear() != null) {

			book.setPublicationYear(dto.getPublicationYear());
		}

		if (dto.getIsbnNumber() != null && !dto.getIsbnNumber().trim().isEmpty()) {

			Optional<Book> existingBook = bookRepository.findByIsbnNumber(dto.getIsbnNumber());

			if (existingBook.isPresent() && !existingBook.get().getBookId().equals(bookId)) {

				throw new IllegalStateException("ISBN already exists");
			}

			book.setIsbnNumber(dto.getIsbnNumber());
		}

		if (dto.getTotalCopies() != null) {

			if (dto.getTotalCopies() < 1) {

				throw new IllegalStateException("Total copies must be greater than 0");
			}

			book.setTotalCopies(dto.getTotalCopies());

			if (book.getAvailableCopies() > dto.getTotalCopies()) {

				book.setAvailableCopies(dto.getTotalCopies());
			}
		}
		if (dto.getAvailableCopies() != null) {

			if (dto.getAvailableCopies() < 0) {

				throw new IllegalStateException("Available copies cannot be negative");
			}

			if (dto.getAvailableCopies() > book.getTotalCopies()) {

				throw new IllegalStateException("Available copies cannot exceed total copies");
			}

			book.setAvailableCopies(dto.getAvailableCopies());
		}

		if (dto.getBookStatus() != null && !dto.getBookStatus().trim().isEmpty()) {

			book.setBookStatus(dto.getBookStatus());
		}

		if (image != null && !image.isEmpty()) {

			Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), Map.of());

			book.setImageUrl(uploadResult.get("secure_url").toString());
		}

		if (book.getAvailableCopies() == 0) {

			book.setBookStatus("OUT_OF_STOCK");

		} else {

			book.setBookStatus("AVAILABLE");
		}

		if (oldAvailableCopies == 0 && book.getAvailableCopies() > 0) {

			List<Waitlist> waitlists = waitlistRepository.findByBookBookIdAndStatus(book.getBookId(), "WAITING");

			for (Waitlist waitlist : waitlists) {

				Notification notification = Notification.builder().user(waitlist.getUser()).title("Book Available")
						.message("The book '" + book.getTitle() + "' is now available.").type("BOOK_AVAILABLE").build();

				notificationRepository.save(notification);

				waitlist.setStatus("NOTIFIED");

				waitlistRepository.save(waitlist);
			}
		}

		Book updatedBook = bookRepository.save(book);

		return modelMapper.map(updatedBook, BookResponseDTO.class);
	}

	@Override
	public void deleteBook(Long bookId) {

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		bookRepository.delete(book);
	}

	@Override
	public BookResponseDTO getBookById(Long bookId) {

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		return modelMapper.map(book, BookResponseDTO.class);
	}

	@Override
	public List<BookResponseDTO> getAllBooks() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<Book> books = bookRepository.findAll(pageable);

		return books.getContent().stream().map(book -> modelMapper.map(book, BookResponseDTO.class)).toList();
	}

	@Override
	public List<BookResponseDTO> searchBooks(String keyword) {

		return bookRepository.findByTitleContainingIgnoreCase(keyword).stream()
				.map(book -> modelMapper.map(book, BookResponseDTO.class)).toList();
	}

	@Override
	public List<BookResponseDTO> getBooksByCategory(String category) {

		return bookRepository.findByCategoryIgnoreCase(category).stream()
				.map(book -> modelMapper.map(book, BookResponseDTO.class)).toList();
	}
}