package fr.d2factory.libraryapp.library.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.entities.book.Book;
import fr.d2factory.libraryapp.entities.book.ISBN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONConverter {

    public static List<Book> convertJSONFileToBooks(String fileName) throws IOException {
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(fileName));


        List<Book> books = new ArrayList<>();


        for (JsonNode node : rootNode) {
            String title = node.path("title").asText();
            String author = node.path("author").asText();
            long isbnCode = node.path("isbn").path("isbnCode").asLong();
            Book book = new Book(title, author, new ISBN(isbnCode));
            books.add(book);
        }
        return books;

    }
}
