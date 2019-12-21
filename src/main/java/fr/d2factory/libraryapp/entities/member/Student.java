package fr.d2factory.libraryapp.entities.member;

public class Student extends Member {

    final int MAX_BORROW_DAYS = 30;
    final int FREE_BORROW_DAYS_1ST_YEAR = 15;
    final float PRICE_PROPORTION = 0.1f;
    private boolean isFirstYear = false;

    /**
     * @param userName
     * @param email
     * @param isLate
     * @param isFirstYear
     */
    public Student(String userName, String email, float initWallet, boolean isLate, boolean isFirstYear) {
        super(userName, email, initWallet);
        this.isFirstYear = isFirstYear;
    }

    /**
     * @param userName
     * @param email
     * @param initWallet
     */
    public Student(String userName, String email, float initWallet) {
        super(userName, email, initWallet);
    }

    @Override
    public int getNbrOfMaxDays() {
        return this.MAX_BORROW_DAYS;
    }

    @Override
    public void payBook(int numberOfDays) {

        int nbDays = numberOfDays;

        if (this.isFirstYear && numberOfDays > this.FREE_BORROW_DAYS_1ST_YEAR) {
            nbDays = nbDays - this.FREE_BORROW_DAYS_1ST_YEAR;
        }

        this.setWallet(this.getWallet() - nbDays * this.PRICE_PROPORTION);
    }

}
