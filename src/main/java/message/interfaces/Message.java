package message.interfaces;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public interface Message {
    /**
     * Mutable property to show whether the message is complete
     */
    void completed();

    /**
     * Gets group in which the message is in
     *
     * @return the group
     */
    int getGroup();

    /**
     * reports completion fo this message
     *
     * @return boolean
     */
    Boolean isCompleted();

    /**
     * Gets the id of the message
     *
     * @return the id
     */
    long getID();

    /**
     * Gets message type
     *
     * @return mesasge type
     */
    MESSAGE_TYPE getType();

    /**
     * Gests body of the message
     *
     * @return body of the message
     */
    String getBody();

    /**
     * Gets the header of this message
     *
     * @return headers of this message
     */
    String getHeaders();

    /**
     * Enum with the message type
     */
    public static enum MESSAGE_TYPE {
        TYPE_INFORMATION, TYPE_CANCELLATION, TYPE_TERMINATION
    }
}
