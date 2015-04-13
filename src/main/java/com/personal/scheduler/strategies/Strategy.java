package com.personal.scheduler.strategies;

import com.personal.interfaces.Message;
import com.personal.scheduler.data.BasicData;
import com.personal.scheduler.data.BasicKey;

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
