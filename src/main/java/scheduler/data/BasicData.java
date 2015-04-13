package scheduler.data;

import message.interfaces.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Jose Cortes on 10/04/15.
 * <p/>
 * Immutable class that represents the value of  the data structure  for storing messages
 */
public final class BasicData {


    private final ConcurrentLinkedQueue<Message> messageList;

    /**
     * Creates the a BasicData object
     *
     * @param messageList
     */
    public BasicData(final ConcurrentLinkedQueue<Message> messageList) {

        this.messageList = messageList;
    }

    /**
     * Get messages list
     *
     * @return queue
     */
    public ConcurrentLinkedQueue<Message> getMessageList() {
        return messageList;
    }
}
