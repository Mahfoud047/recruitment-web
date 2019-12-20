package fr.d2factory.libraryapp.repositories;

import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.book.ISBN;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books) {
        for (Book newBook : books) {
            availableBooks.putIfAbsent(newBook.getIsbn(), newBook);
        }
    }

    /**
     * @param isbnCode
     * @return
     */
    public Book findBook(long isbnCode) {
        return availableBooks.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getIsbnCode() == isbnCode)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

    }

    /**
     * @param book
     * @param borrowedAt
     */
    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        borrowedBooks.put(book, borrowedAt);
        availableBooks.remove(book.getIsbn(), book);
    }

    /**
     * @param book
     */
    public void saveBookReturn(Book book) {
        availableBooks.put(book.getIsbn(), book);
        borrowedBooks.remove(book);

    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }
}
