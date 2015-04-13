package client;

import exceptions.GroupTerminatedException;
import message.builder.MessageBuilder;
import message.builder.SimpleMessageBuilderImpl;
import message.interfaces.Message;
import message.interfaces.MessageConsummer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public class ClientEmulator {

    final Logger logger = LogManager.getFormatterLogger("client");
    private final MessageBuilder builder;
    private final MessageConsummer consummer;

    /**
     * Constructs a client emultaor object
     *
     * @param consummer the consummer for the messages
     */
    public ClientEmulator(final MessageConsummer consummer) {
        this.consummer = consummer;
        builder = new SimpleMessageBuilderImpl();
    }

    /**
     * Sends information message to the consummer
     *
     * @param group in which the meesag will be
     * @param id    of the message
     * @return the message
     */
    public Message SendNewMessge(final int group, final long id) {
        final Message message = builder.createMessage(group, id);
        try {

            consummer.consumme(message);

        } catch (GroupTerminatedException e) {
            logger.error(e.getMessage());
        }
        return message;
    }

    /**
     * Sends control message to the consumer.
     *
     * @param group in which th messag will be
     * @param id    of the message
     * @param type  of the control meesage
     * @return the message
     */
    public Message SendNewControlMessage(final int group, final long id, final Message.MESSAGE_TYPE type) {
        final Message message = builder.createMessage(group, id, type);
        try {

            consummer.consumme(message);
        } catch (GroupTerminatedException e) {
            logger.error(e.getMessage());
        }
        return message;
    }
}
