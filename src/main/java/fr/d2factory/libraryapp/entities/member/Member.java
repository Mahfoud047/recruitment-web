package fr.d2factory.libraryapp.entities.member;

import fr.d2factory.libraryapp.services.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {

    private String userName;
    private String email;


    private boolean isLate = false;

    /**
     * An initial sum of money the member has
     */
    private float wallet;

    /**
     *
     * @param userName
     * @param email
     */
    public Member(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public abstract int getNbrOfMaxDays();

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }
}
