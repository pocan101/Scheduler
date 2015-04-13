package exceptions;

/**
 * Exception class to indicate that the group is already terminated and cannot process any other messages of the group
 */
public class GroupTerminatedException extends Throwable {

    public static final String MESSAGE = "The group has been terminated. No other messages from that group are allowed";

    @Override
    public String getMessage() {

        return MESSAGE;
    }
}
