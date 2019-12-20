package fr.d2factory.libraryapp.services;

import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.member.Member;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.repositories.BookRepository;
import fr.d2factory.libraryapp.repositories.MemberRepository;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class LibraryService implements Library {

    private BookRepository bookRepo;
    private MemberRepository memberRepo;

    public BookRepository getBookRepo() {
        return bookRepo;
    }

    public MemberRepository getMemberRepo() {
        return memberRepo;
    }

    public LibraryService(BookRepository bookRepo, MemberRepository memberRepo) {
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {

        // Check if the member has a late borrow
        memberRepo.getBooksOfMember(member).forEach(
                book -> {
                    final LocalDate borrowDate = bookRepo.findBorrowedBookDate(book);
                    if (borrowDate != null && DAYS.between(borrowDate, borrowedAt) > member.getNbrOfMaxDays()) {
                        throw new HasLateBooksException();
                    }
                }
        );

        final Book bookToBorrow = bookRepo.findBook(isbnCode);

        if (bookToBorrow != null) {
            System.out.println("just before saveBookBorrow");
            bookRepo.saveBookBorrow(bookToBorrow, borrowedAt);
            memberRepo.saveMemberBorrow(member, bookToBorrow);
        }

        return bookToBorrow;
    }


    @Override
    public void returnBook(Book book, Member member) {

    }
}
