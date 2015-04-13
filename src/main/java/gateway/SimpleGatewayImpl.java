package gateway;

import exceptions.GroupTerminatedException;
import message.interfaces.Message;
import message.interfaces.MessageConsummer;
import resource.ResourceEmulator;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public final class SimpleGatewayImpl implements Gateway {

    final String id;
    private final MessageConsummer consummer;

    /**
     * Constructs a simpe gateway
     *
     * @param id the id of this gateway
     */
    public SimpleGatewayImpl(final String id) {
        this.id = id;
        this.consummer = new ResourceEmulator(id);
    }

    /**
     * Send message to the gateway
     *
     * @param msg the message
     * @throws GroupTerminatedException
     */
    public void send(final Message msg) throws GroupTerminatedException {

        consummer.consumme(msg);
    }

    /**
     * Checks whether the gateway is available
     *
     * @return Atomic Booolean
     */
    public AtomicBoolean isAvailable() {

        return consummer.isAvailable();
    }

    /**
     * Gets the id of the gateway
     *
     * @return the id
     */
    public String getID() {
        return "GatewayToResource_" + id;
    }
}
