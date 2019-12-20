package fr.d2factory.libraryapp.entities.member;

public class Resident extends Member {

    final int MAX_BORROW_DAYS = 60;

    /**
     * @param userName
     * @param email
     * @param isLate
     */
    public Resident(String userName, String email) {
        super(userName, email);
    }

    @Override
    public int getNbrOfMaxDays() {
        return this.MAX_BORROW_DAYS;
    }

    public boolean isLate(int nbDays) {
        return nbDays > this.MAX_BORROW_DAYS;
    }

    @Override
    public void payBook(int numberOfDays) {

    }
}
