package tw.yen.spring.exception;

public class PasswordUpdateException extends RuntimeException {
	public PasswordUpdateException(String message) {
        super(message);
    }
}
