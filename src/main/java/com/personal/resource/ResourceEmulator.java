package com.personal.resource;

import com.personal.exceptions.GroupTerminatedException;
import com.personal.interfaces.Message;
import com.personal.interfaces.MessageConsummer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public final class ResourceEmulator implements MessageConsummer {

    private final String id;
    private AtomicBoolean isAvailable;

    /**
     * Constructs the resource
     *
     * @param id of the resource
     */
    public ResourceEmulator(String id) {
        this.id = id;
        isAvailable = new AtomicBoolean(true);
    }

    /**
     * consumme the message sent
     *
     * @param msg the msg
     * @throws GroupTerminatedException
     */
    public void consumme(final Message msg) {

        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {

        }

        msg.completed();
        isAvailable.set(true);
    }

    /**
     * Specifies whether the resource is available
     *
     * @return Boolean
     */
    public AtomicBoolean isAvailable() {
        return isAvailable;
    }

    /**
     * Current thread waits until the consummer is done with msg load
     *
     * @throws InterruptedException
     */
    public void WaitForCompletion() {

    }

    /**
     * Gets consummer id
     *
     * @return String
     */
    public String getID() {
        return id;
    }

    /**
     * Stops processing messages.
     */
    public void stopProcessing() {
        isAvailable.set(false);
    }

    /**
     * Starts processing messages
     */
    public void startProcessing() {

    }

    /**
     * Gets dead letter queue for the consummer
     *
     * @return queue
     */
    public ConcurrentLinkedQueue<Message> getDeadLetterQueue() {
        return null;
    }
}
