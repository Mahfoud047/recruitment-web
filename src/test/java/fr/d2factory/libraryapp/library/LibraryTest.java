package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.book.ISBN;
import fr.d2factory.libraryapp.entities.member.Resident;
import fr.d2factory.libraryapp.entities.member.Student;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
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


class LibraryTest {
    private Library library;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private Student student;
    private Resident resident;
    private static List<Book> books;
    private LocalDate now;


    @BeforeEach
    void setup() throws IOException {

        final String jsonPath = "src/test/resources/books.json";
        books = JSONConverter.convertJSONFileToBooks(jsonPath);

        bookRepository = new BookRepository();
        bookRepository.addBooks(books);

        memberRepository = new MemberRepository();

        library = new LibraryService(bookRepository, memberRepository);

        student = new Student("John Doe", "email@example.com", 200);
        resident = new Resident("Sarah Boudes", "email@example.com", 200);

        now = LocalDate.now();
    }

    @Test
    void student_can_borrow_a_book_if_book_is_available() {
        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book availableBook = libService.getBookRepo().findBook(isbn.getIsbnCode());

        assertNotNull(availableBook); /* book is available */

        libService.borrowBook(isbn.getIsbnCode(), student, now);

        final List<Book> borrowsOfMember = libService.getMemberRepo().getBooksOfMember(student);

        assertTrue(borrowsOfMember.contains(availableBook)); /* book is borrowed by the right member */
    }

    @Test
    void resident_can_borrow_a_book_if_book_is_available() {
        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book availableBook = libService.getBookRepo().findBook(isbn.getIsbnCode());

        assertNotNull(availableBook); /* book is available */

        libService.borrowBook(isbn.getIsbnCode(), resident, now);

        final List<Book> borrowsOfMember = libService.getMemberRepo().getBooksOfMember(resident);

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
        final float walletBefore = resident.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final int days = 5;

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), resident, now.minusDays(days));

        libService.returnBook(borrowedBook, resident);

        final float walletAfter = resident.getWallet();

        assertEquals(walletBefore - (days * 0.1f), walletAfter);

    }

    @Test
    void students_pay_10_cents_the_first_30days() {
        final float walletBefore = student.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), student, now.minusDays(30));

        libService.returnBook(borrowedBook, student);

        final float walletAfter = student.getWallet();

        assertEquals(walletBefore - (30 * 0.1f), walletAfter);
    }

    @Test
    void students_pay_10_cents_after_30days() {
        final float walletBefore = student.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final int days = 31;

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), student, now.minusDays(days));

        libService.returnBook(borrowedBook, student);

        final float walletAfter = student.getWallet();

        assertEquals(walletBefore - (days * 0.1f), walletAfter);
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days() {

        /* 1st year student */
        student = new Student("John Doe", "email@example.com", 200, false, true);

        final float walletBefore = student.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), student, now.minusDays(15));

        libService.returnBook(borrowedBook, student);

        final float walletAfter = resident.getWallet();

        assertEquals(walletBefore, walletAfter);

    }

    @Test
    void students_in_1st_year_are_taxed_10_cents_for_more_than_15days() {

        /* 1st year student */
        student = new Student("John Doe", "email@example.com", 200, false, true);

        final float walletBefore = student.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final int daysAfter15 = 2;

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), student, now.minusDays(15 + daysAfter15));

        libService.returnBook(borrowedBook, student);

        final float walletAfter = student.getWallet();

        assertEquals(walletBefore - (daysAfter15 * 0.1f), walletAfter);

    }

    @Test
    void residents_pay_10cents_for_each_day_they_keep_a_book_before_the_initial_60days() {
        final float walletBefore = resident.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final int days = 59;

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), resident, now.minusDays(days));

        libService.returnBook(borrowedBook, resident);

        final float walletAfter = resident.getWallet();

        assertEquals(walletBefore - (days * 0.1f), walletAfter);
    }

    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        final float walletBefore = resident.getWallet();

        final ISBN isbn = books.get(0).getIsbn();

        final int days = 61;

        final LibraryService libService = ((LibraryService) library);

        final Book borrowedBook = libService.borrowBook(isbn.getIsbnCode(), resident, now.minusDays(days));

        libService.returnBook(borrowedBook, resident);

        final float walletAfter = resident.getWallet();

        assertEquals(walletBefore - (days * 0.2f), walletAfter);
    }

    @Test
    void students_cannot_borrow_book_if_they_have_late_books() {

        final ISBN isbn = books.get(0).getIsbn();
        final ISBN isbn2 = books.get(1).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        libService.borrowBook(isbn.getIsbnCode(), student, now.minusDays(student.getNbrOfMaxDays() + 1));

        Assertions.assertThrows(HasLateBooksException.class, () -> {
            libService.borrowBook(isbn2.getIsbnCode(), student, now);
        });

    }

    @Test
    void residents_cannot_borrow_book_if_they_have_late_books() {

        final ISBN isbn = books.get(0).getIsbn();
        final ISBN isbn2 = books.get(1).getIsbn();

        final LibraryService libService = ((LibraryService) library);

        libService.borrowBook(isbn.getIsbnCode(), resident,
                now.minusDays(resident.getNbrOfMaxDays() + 1));

        Assertions.assertThrows(HasLateBooksException.class, () -> {
            libService.borrowBook(isbn2.getIsbnCode(), resident, now);
        });

    }
}
