package fr.d2factory.libraryapp.entities.book;

public class ISBN {
    long isbnCode;

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    public long getIsbnCode() {
        return isbnCode;
    }

    @Override
    public String toString() {
        return String.valueOf(this.isbnCode);
    }
}
