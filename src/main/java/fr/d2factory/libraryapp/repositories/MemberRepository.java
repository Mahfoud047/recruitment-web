package fr.d2factory.libraryapp.repositories;

import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.book.ISBN;
import fr.d2factory.libraryapp.entities.member.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class MemberRepository {
    private HashMap<Member, List<Book>> borrowedBooksByMembers = new HashMap<>();

    public List<Book> getBooksOfMember(Member member) {
        return this.borrowedBooksByMembers.computeIfAbsent(member, k -> new ArrayList<>());
    }


    /**
     * @param member
     * @param book
     * @return
     */
    public Book saveMemberBorrow(Member member, Book book) {
        if (borrowedBooksByMembers.containsKey(member)) {
            borrowedBooksByMembers.get(member).add(book);
        } else {
            final List<Book> newBookList = new ArrayList<Book>();
            newBookList.add(book);
            borrowedBooksByMembers.put(member, newBookList);
        }
        return book;
    }

}
