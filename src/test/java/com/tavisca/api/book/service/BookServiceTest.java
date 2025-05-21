package com.tavisca.api.book.service;

import com.tavisca.api.book.POJO.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

	private BookService bookService;

	@BeforeEach
	void setup() {
		bookService = new BookService();
	}

	// ----- addBook() Tests -----

	@Test
	void addBook_R1_whenBookIsNull_shouldReturnError() {
		// Arrange
		Book book = null;

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book cannot be null", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R2_whenIdIsZero_shouldReturnError() {
		// Arrange
		Book book = new Book(0, "Title", "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Invalid book ID", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R3_whenIdIsNegative_shouldReturnError() {
		// Arrange
		Book book = new Book(-5, "Title", "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Invalid book ID", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R4_whenBookAlreadyExists_shouldReturnError() {
		// Arrange
		Book book1 = new Book(1, "Book", "Author", false);
		Book book2 = new Book(1, "Another", "Author", false);
		bookService.addBook(book1);

		// Act
		ApiResponse response = bookService.addBook(book2);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book with this ID already exists", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R5_whenTitleIsNull_shouldReturnError() {
		// Arrange
		Book book = new Book(2, null, "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R6_whenTitleIsEmpty_shouldReturnError() {
		// Arrange
		Book book = new Book(3, "", "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R7_whenTitleHasWhitespaceOnly_shouldReturnError() {
		// Arrange
		Book book = new Book(4, "   ", "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R8_whenAuthorIsNull_shouldReturnError() {
		// Arrange
		Book book = new Book(5, "Title", null, false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R9_whenAuthorIsEmpty_shouldReturnError() {
		// Arrange
		Book book = new Book(6, "Title", "", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R10_whenAuthorHasWhitespaceOnly_shouldReturnError() {
		// Arrange
		Book book = new Book(7, "Title", "   ", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Title and author must not be empty", ((ApiResponseError) response).getMessage());
	}

	@Test
	void addBook_R11_whenValidBook_shouldReturnSuccess() {
		// Arrange
		Book book = new Book(8, "Effective Java", "Joshua Bloch", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		assertEquals("Book added successfully", ((ApiResponseSuccess) response).getData());
	}

	// ----- borrowBook() Tests -----

	@Test
	void borrowBook_R1_whenBookNotFound_shouldReturnError() {
		// Arrange
		BorrowRequest req = new BorrowRequest(99);

		// Act
		ApiResponse response = bookService.borrowBook(req);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void borrowBook_R2_whenAlreadyBorrowed_shouldReturnError() {
		// Arrange
		Book book = new Book(10, "Borrowed", "X", true);
		bookService.addBook(book);

		// Act
		ApiResponse response = bookService.borrowBook(new BorrowRequest(10));

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book is already borrowed", ((ApiResponseError) response).getMessage());
	}

	@Test
	void borrowBook_R3_whenAvailable_shouldReturnSuccess() {
		// Arrange
		Book book = new Book(11, "Borrowable", "Y", false);
		bookService.addBook(book);

		// Act
		ApiResponse response = bookService.borrowBook(new BorrowRequest(11));

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		assertEquals("Book borrowed successfully", ((ApiResponseSuccess) response).getData());
	}

	// ----- returnBook() Tests -----

	@Test
	void returnBook_R1_whenBookNotFound_shouldReturnError() {
		// Arrange
		BorrowRequest req = new BorrowRequest(777);

		// Act
		ApiResponse response = bookService.returnBook(req);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void returnBook_R2_whenNotBorrowed_shouldReturnError() {
		// Arrange
		Book book = new Book(12, "Unborrowed", "Z", false);
		bookService.addBook(book);

		// Act
		ApiResponse response = bookService.returnBook(new BorrowRequest(12));

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book was not borrowed", ((ApiResponseError) response).getMessage());
	}

	@Test
	void returnBook_R3_whenBorrowed_shouldReturnSuccess() {
		// Arrange
		Book book = new Book(13, "Returnable", "Z", true);
		bookService.addBook(book);

		// Act
		ApiResponse response = bookService.returnBook(new BorrowRequest(13));

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		assertEquals("Book returned successfully", ((ApiResponseSuccess) response).getData());
	}

	// ----- getBookInfo() Tests -----

	@Test
	void getBookInfo_R1_whenBookNotFound_shouldReturnError() {
		// Act
		ApiResponse response = bookService.getBookInfo(1000);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void getBookInfo_R2_whenFound_shouldReturnBook() {
		// Arrange
		Book book = new Book(14, "Spring in Action", "Craig Walls", false);
		bookService.addBook(book);

		// Act
		ApiResponse response = bookService.getBookInfo(14);

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		Book result = (Book) ((ApiResponseSuccess) response).getData();
		assertEquals("Spring in Action", result.getTitle());
	}

	/// Additional test cases -edge
	//Add Book – Edge Cases
	@Test
	void addBook_E1_whenTitleIsVeryLong_shouldSucceed() {
		// Arrange
		String longTitle = "A".repeat(1000); // simulate long title
		Book book = new Book(20, longTitle, "Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		assertEquals("Book added successfully", ((ApiResponseSuccess) response).getData());
	}

	@Test
	void addBook_E2_whenAuthorIsVeryLong_shouldSucceed() {
		// Arrange
		String longAuthor = "B".repeat(1000); // simulate long author
		Book book = new Book(21, "Some Book", longAuthor, false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
		assertEquals("Book added successfully", ((ApiResponseSuccess) response).getData());
	}

	@Test
	void addBook_E3_whenIdIsMaxInteger_shouldSucceed() {
		// Arrange
		Book book = new Book(Integer.MAX_VALUE, "Edge Book", "Edge Author", false);

		// Act
		ApiResponse response = bookService.addBook(book);

		// Assert
		assertTrue(response instanceof ApiResponseSuccess);
	}

	// Borrow Book – Edge Cases
	@Test
	void borrowBook_E1_whenBorrowRequestHasZeroId_shouldReturnError() {
		// Arrange
		BorrowRequest req = new BorrowRequest(0);

		// Act
		ApiResponse response = bookService.borrowBook(req);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void borrowBook_E2_whenBorrowRequestIsNull_shouldThrowException() {
		// Arrange, Act & Assert
		assertThrows(NullPointerException.class, () -> bookService.borrowBook(null));
	}

	// Return Book – Edge Cases
	@Test
	void returnBook_E1_whenReturnRequestHasNegativeId_shouldReturnError() {
		// Arrange
		BorrowRequest request = new BorrowRequest(-50);

		// Act
		ApiResponse response = bookService.returnBook(request);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void returnBook_E2_whenReturnRequestIsNull_shouldThrowException() {
		// Arrange, Act & Assert
		assertThrows(NullPointerException.class, () -> bookService.returnBook(null));
	}

	// Get Book Info – Edge Cases
	@Test
	void getBookInfo_E1_whenIdIsZero_shouldReturnError() {
		// Act
		ApiResponse response = bookService.getBookInfo(0);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@Test
	void getBookInfo_E2_whenIdIsNegative_shouldReturnError() {
		// Act
		ApiResponse response = bookService.getBookInfo(-1);

		// Assert
		assertTrue(response instanceof ApiResponseError);
		assertEquals("Book not found", ((ApiResponseError) response).getMessage());
	}

	@AfterEach
	void tearDown() {
		// Optional cleanup
	}
}
