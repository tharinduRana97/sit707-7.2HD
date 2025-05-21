package com.tavisca.api.book.service;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BookAppIntegrationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
    	System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/local/bin/google-chrome");
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("CHROMEDRIVER PATH = " + System.getProperty("webdriver.chrome.driver"));

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    // ----- Helper Methods -----

    private void addBook(String id, String title, String author) {
        driver.get(BASE_URL + "/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("id"))).sendKeys(id);
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("author")).sendKeys(author);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        waitForMessage();
    }

    private void borrowBook(String id) {
        driver.get(BASE_URL + "/borrow");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("bookId"))).sendKeys(id);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        waitForMessage();
    }

    private void returnBook(String id) {
        driver.get(BASE_URL + "/return");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("bookId"))).sendKeys(id);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        waitForMessage();
    }

    private void waitForMessage() {
        wait.until(driver -> {
            WebElement messageElement;
            try {
                messageElement = driver.findElement(By.className("message"));
                String text = messageElement.getText();
                return text != null && !text.trim().isEmpty() && !"null".equalsIgnoreCase(text.trim());
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }


    // ----- Core Integration Tests (AAAA) -----

    @Test
    public void TC_01_AddBook_Success() {
        // Arrange & Act
        addBook("3101", "Valid Book", "Author A");

        // Assert
        String page = driver.getPageSource();
        assertTrue(page.contains("Book added successfully"));
    }

    @Test
    public void TC_02_AddBook_Duplicate_ShouldFail() {
        // Arrange
        addBook("3102", "Book", "Author A");

        // Act
        addBook("3102", "Book", "Author A");

        // Assert
        assertTrue(driver.getPageSource().contains("Book with this ID already exists"));
    }

    @Test
    public void TC_03_AddBook_EmptyTitle_ShouldFail() {
        // Arrange & Act
        addBook("3103", "", "Author");

        // Assert
        assertTrue(driver.getPageSource().contains("Title and author must not be empty"));
    }

    @Test
    public void TC_04_AddBook_EmptyAuthor_ShouldFail() {
        // Arrange & Act
        addBook("3104", "Book", "");

        // Assert
        assertTrue(driver.getPageSource().contains("Title and author must not be empty"));
    }

    @Test
    public void TC_05_AddBook_ZeroId_ShouldFail() {
        // Arrange & Act
        addBook("0", "Book", "Author");

        // Assert
        assertTrue(driver.getPageSource().contains("Invalid book ID"));
    }

    @Test
    public void TC_06_AddBook_NegativeId_ShouldFail() {
        // Arrange & Act
        addBook("-10", "Book", "Author");

        // Assert
        assertTrue(driver.getPageSource().contains("Invalid book ID"));
    }

    @Test
    public void TC_07_BorrowBook_Success() {
        // Arrange
        addBook("3105", "Borrowable Book", "Author B");

        // Act
        borrowBook("3105");

        // Assert
        assertTrue(driver.getPageSource().contains("Book borrowed successfully"));
    }

    @Test
    public void TC_08_BorrowBook_AlreadyBorrowed_ShouldFail() {
        // Arrange
        addBook("3106", "Borrowed Twice", "Author C");
        borrowBook("3106");

        // Act
        borrowBook("3106");

        // Assert
        assertTrue(driver.getPageSource().contains("Book is already borrowed"));
    }

    @Test
    public void TC_09_BorrowBook_NotFound_ShouldFail() {
        // Act
        borrowBook("99901");

        // Assert
        assertTrue(driver.getPageSource().contains("Book not found"));
    }

    @Test
    public void TC_10_ReturnBook_Success() {
        // Arrange
        addBook("3107", "Return Me", "Author D");
        borrowBook("3107");

        // Act
        returnBook("3107");

        // Assert
        assertTrue(driver.getPageSource().contains("Book returned successfully"));
    }

    @Test
    public void TC_11_ReturnBook_NotBorrowed_ShouldFail() {
        // Arrange
        addBook("3108", "Not Borrowed Yet", "Author E");

        // Act
        returnBook("3108");

        // Assert
        assertTrue(driver.getPageSource().contains("Book was not borrowed"));
    }

    @Test
    public void TC_12_ReturnBook_NotFound_ShouldFail() {
        // Act
        returnBook("99902");

        // Assert
        assertTrue(driver.getPageSource().contains("Book not found"));
    }

    @Test
    public void TC_13_GetBookInfo_Found_ShouldDisplayBook() {
        // Arrange
        addBook("3109", "Info Book", "Author F");

        // Act
        driver.get(BASE_URL + "/info?bookId=3109");

        // Assert
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"), "Info Book"));
        assertTrue(driver.getPageSource().contains("Info Book"));
    }

    @Test
    public void TC_14_GetBookInfo_NotFound_ShouldDisplayError() {
        // Act
        driver.get(BASE_URL + "/info?bookId=99903");

        // Assert
        assertTrue(driver.getPageSource().contains("Book not found"));
    }

    @Test
    public void TC_15_GetBookInfo_NoQuery_ShouldNotFail() {
        // Act
        driver.get(BASE_URL + "/info");

        // Assert
        assertFalse(driver.getPageSource().contains("Book not found")); // No query should be neutral
    }

    // ----- Security and Input Edge Validation -----

    @Test
    public void TC_16_AddBook_ScriptInjection_ShouldSucceedAsText() {
        // Arrange & Act
        addBook("3110", "<script>alert('x')</script>", "EvilAuthor");

        // Assert
        assertTrue(driver.getPageSource().contains("Book added successfully"));
    }

    @Test
    public void TC_17_AddBook_VeryLongTitle_ShouldSucceed() {
        // Arrange
        String title = "A".repeat(500);
        addBook("3111", title, "Author");

        // Assert
        assertTrue(driver.getPageSource().contains("Book added successfully"));
    }

    @Test
    public void TC_18_AddBook_VeryLongAuthor_ShouldSucceed() {
        // Arrange
        String author = "B".repeat(500);
        addBook("3112", "Book", author);

        // Assert
        assertTrue(driver.getPageSource().contains("Book added successfully"));
    }
}
