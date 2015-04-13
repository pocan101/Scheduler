package scheduler.strategies;

import debug.Dumper;
import message.interfaces.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scheduler.data.BasicData;
import scheduler.data.BasicKey;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Jose Cortes on 12/04/15.
 * Abstract class that models the strategy fro retrieving messages
 */
public abstract class BaseStrategy implements Strategy {
    final protected Logger logger = LogManager.getFormatterLogger("strategies");
    protected BasicKey currentGroup = null;

    /**
     * Gests the next message according to the Strategy
     *
     * @param internalQueues the data structure
     * @return the message
     */
    public Message getNextMessage(final ConcurrentSkipListMap<BasicKey, BasicData> internalQueues) {

        if (internalQueues.isEmpty()) {
            return null;
        }

        if (currentGroup == null) {
            currentGroup = getNextKey(internalQueues);
        }
        logger.trace(" next message from ID  %s queues %s", currentGroup, Dumper.dump(internalQueues));

        return getMessage(internalQueues, currentGroup);
    }

    /**
     * Gets the last mesasge from the message queue in the basic Data stored in the data structure
     *
     * @param internalQueues the data structure
     * @param key            the key of the message
     * @return the message
     */
    protected Message getMessage(final ConcurrentSkipListMap<BasicKey, BasicData> internalQueues, final BasicKey key) {
        final BasicData data = internalQueues.get(key);
        final Message message = data.getMessageList().poll();

        if (data.getMessageList().isEmpty()) {
            internalQueues.remove(key);
            this.currentGroup = null;
            logger.trace(" deleted message queues %s", Dumper.dump(internalQueues));
        }

        return message;
    }

    /**
     * Bastract method to provide next key to be send to the gateway according to the desired strategy.
     *
     * @param internalQueues the data struture
     * @return the key
     */
    public abstract BasicKey getNextKey(ConcurrentSkipListMap<BasicKey, BasicData> internalQueues);
}
