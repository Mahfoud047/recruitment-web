package fr.d2factory.libraryapp.entities.member;
import fr.d2factory.libraryapp.services.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {

    private String userName;
    private String email;


    /**
     * An initial sum of money the member has
     */
    private float wallet;

    /**
     * @param userName
     * @param email
     * @param initWallet
     */
    public Member(String userName, String email, float initWallet) {
        this.userName = userName;
        this.email = email;
        this.wallet = initWallet;
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


    @Override
    public String toString() {
        return "Member: { userName: " + this.userName +
                ", email: " + this.email +
                ", wallet: " + this.wallet +
                " }";
    }
}
