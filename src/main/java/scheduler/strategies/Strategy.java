package scheduler.strategies;

import message.interfaces.Message;
import scheduler.data.BasicData;
import scheduler.data.BasicKey;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Interface that models an Strategy
 */
public interface Strategy {

    /**
     * Get next message to be processed
     *
     * @param internalQueues the data structure
     * @return the message
     */
    Message getNextMessage(final ConcurrentSkipListMap<BasicKey, BasicData> internalQueues);

    /**
     * Enum to define strategy
     */
    public static enum STRATEGY {
        FIRSTGROUPBASED, LASTGROUPBASED
    }
}
