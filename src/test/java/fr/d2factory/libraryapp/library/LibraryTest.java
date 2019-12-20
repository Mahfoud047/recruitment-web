package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.book.ISBN;
import fr.d2factory.libraryapp.entities.member.Resident;
import fr.d2factory.libraryapp.entities.member.Student;
import fr.d2factory.libraryapp.library.util.JSONConverter;
import fr.d2factory.libraryapp.repositories.BookRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import fr.d2factory.libraryapp.repositories.MemberRepository;
import fr.d2factory.libraryapp.services.Library;
import fr.d2factory.libraryapp.services.LibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private Student student;
    private Resident resident;
    private static List<Book> books;
    private LocalDate now;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
        final String jsonPath = "src/test/resources/books.json";
        books = JSONConverter.convertJSONFileToBooks(jsonPath);

        bookRepository = new BookRepository();
        bookRepository.addBooks(books);

        memberRepository = new MemberRepository();

        library = new LibraryService(bookRepository, memberRepository);

        student = new Student("John Doe", "email@example.com");
        resident = new Resident("Sarah Boudes", "email@example.com");

        now = LocalDate.now();
    }

    @Test
    void member_can_borrow_a_book_if_book_is_available() {
        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book availableBook = libService.getBookRepo().findBook(isbn.getIsbnCode());

        assertNotNull(availableBook); /* book is available */

        libService.borrowBook(isbn.getIsbnCode(), student, now);

        final List<Book> borrowsOfMember = libService.getMemberRepo().getBooksOfMember(student);

        assertTrue(borrowsOfMember.contains(availableBook)); /* book is borrowed by the right member */
    }

    @Test
    void borrowed_book_is_no_longer_available() {

        final LibraryService libService = ((LibraryService) library);

        final ISBN isbn = books.get(0).getIsbn();

        libService.borrowBook(isbn.getIsbnCode(), student, now);

        final Book availableBook = libService.getBookRepo().findBook(isbn.getIsbnCode());

        assertNull(availableBook); /* is the book no longer available ? */

    }

    @Test
    void borrowed_book_is_borrowed_in_the_right_date() {

        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), student, now);

        final LocalDate borrowDate = libService.getBookRepo().findBorrowedBookDate(borrowedBook);

        assertSame(borrowDate, now); /* book is borrowed for the right date */

    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        Assertions.fail("Implement me");
    }

    @Test
    void students_pay_10_cents_the_first_30days() {
        Assertions.fail("Implement me");
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        Assertions.fail("Implement me");
    }

    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        Assertions.fail("Implement me");
    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books() {
        Assertions.fail("Implement me");
    }
}
