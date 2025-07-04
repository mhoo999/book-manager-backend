package sesac.bookmanager.global.exceptions;

public class UserWasDeletedException extends RuntimeException {
    public UserWasDeletedException(String message) {
        super(message);
    }
}
