package fr.d2factory.libraryapp.entities.member;

public class Student extends Member {

    final int MAX_BORROW_DAYS = 30;
    private boolean isFirstYear = false;

    /**
     * @param userName
     * @param email
     * @param isLate
     * @param isFirstYear
     */
    public Student(String userName, String email, boolean isLate, boolean isFirstYear) {
        super(userName, email);
        this.isFirstYear = isFirstYear;
    }

    /**
     * @param userName
     * @param email
     */
    public Student(String userName, String email) {
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
