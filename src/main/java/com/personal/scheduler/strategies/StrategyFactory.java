package com.personal.scheduler.strategies;

/**
 * Created by Jose Cortes on 12/04/15.
 * Factory that created an instance of a given strategy enumerate
 */
public class StrategyFactory {

    /**
     * Gest the strategy associated to de given enumerate
     *
     * @param strategy strategy enumerate
     * @return the strategy instance object
     */
    public static Strategy getStrategy(final Strategy.STRATEGY strategy) {
        switch (strategy) {
            case FIRSTGROUPBASED:
                return new FirstCreatedGroupBasedStrategy();
            case LASTGROUPBASED:
                return new LastCreatedGroupBasedStrategy();
        }

        return new FirstCreatedGroupBasedStrategy();
    }
}
