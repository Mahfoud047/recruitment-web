package fr.d2factory.libraryapp.entities.member;

public class Resident extends Member {

    final int MAX_BORROW_DAYS = 60;
    final float PRICE_PROPORTION = 0.1f;
    final float PRICE_PROPORTION_LATE = 0.2f;

    /**
     * @param userName
     * @param email
     */
    public Resident(String userName, String email, float initWallet) {
        super(userName, email, initWallet);
    }

    @Override
    public int getNbrOfMaxDays() {
        return this.MAX_BORROW_DAYS;
    }

    @Override
    public void payBook(int numberOfDays) {
        float proportion = numberOfDays <= this.MAX_BORROW_DAYS ? this.PRICE_PROPORTION
                : this.PRICE_PROPORTION_LATE;

        this.setWallet(this.getWallet() - numberOfDays * proportion);
    }
}
