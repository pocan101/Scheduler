package scheduler.strategies;

import scheduler.data.BasicData;
import scheduler.data.BasicKey;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Strategy that retreives messages that belongs to the fisrt created group in the data structure.
 */
public final class FirstCreatedGroupBasedStrategy extends BaseStrategy {


    /**
     * Bastract method to provide next key to be send to the gateway according to the desired strategy.
     *
     * @param internalQueues the data structure
     * @return the key
     */
    @Override
    public BasicKey getNextKey(ConcurrentSkipListMap<BasicKey, BasicData> internalQueues) {

        return internalQueues.firstKey();
    }
}
