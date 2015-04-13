package message.interfaces;

import exceptions.GroupTerminatedException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public interface MessageConsummer {
    /**
     * consumme the message sent
     *
     * @param msg the msg
     * @throws GroupTerminatedException
     */
    void consumme(final Message msg) throws GroupTerminatedException;

    /**
     * Specifies whether the resource is available
     *
     * @return Boolean
     */

    AtomicBoolean isAvailable();

    /**
     * Current thread waits until the consummer is done with msg load
     *
     * @throws InterruptedException
     */
    void WaitForCompletion() throws InterruptedException;

    /**
     * Gets consummer id
     *
     * @return String
     */
    String getID();

    /**
     * Stops processing messages.
     */
    void stopProcessing();

    /**
     * Starts processing messages
     */
    void startProcessing();

    /**
     * Gets dead letter queue for the consummer
     *
     * @return queue
     */
    public ConcurrentLinkedQueue<Message> getDeadLetterQueue();
}
