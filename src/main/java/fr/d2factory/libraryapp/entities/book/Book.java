package fr.d2factory.libraryapp.entities.book;

/**
 * A simple representation of a book
 */
public class Book {
    String title;
    String author;
    ISBN isbn;

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return "Book: { isbn: " + this.isbn + ", title: " + this.title + ", author: " +
                this.author + " }";
    }
}
