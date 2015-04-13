package gateway;

import exceptions.GroupTerminatedException;
import message.interfaces.Message;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public interface Gateway {
    /**
     * Sends message to the gateway
     *
     * @param msg the message
     * @throws GroupTerminatedException
     */
    void send(final Message msg) throws GroupTerminatedException;

    /**
     * Checks whether the gateway is available for processing the message
     *
     * @return Atomic Boolean
     */
    AtomicBoolean isAvailable();

    /**
     * Gets the ID of the gateway
     *
     * @return the id
     */
    String getID();
}
