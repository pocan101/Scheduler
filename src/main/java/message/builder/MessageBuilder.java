package message.builder;

import message.interfaces.Message;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public interface MessageBuilder {
    /**
     * Creates an information message
     *
     * @param group of the message
     * @param id    of the message
     * @return the message
     */
    Message createMessage(final int group, final long id);

    /**
     * Creates a control message of type termination or cancellation
     *
     * @param group of the message
     * @param id    of the message
     * @param type  the type of the control message
     * @return the message
     */
    Message createMessage(final int group, final long id, Message.MESSAGE_TYPE type);
}
