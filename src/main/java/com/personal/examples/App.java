package com.personal.examples;

import com.personal.client.ClientEmulator;
import com.personal.interfaces.Message;
import com.personal.interfaces.MessageConsummer;
import com.personal.scheduler.Scheduler;
import com.personal.scheduler.strategies.Strategy;
import com.personal.scheduler.strategies.StrategyFactory;

/**
 * Class to simulate all use cases together
 */
public class App {
    final static Strategy.STRATEGY strategyFirst = Strategy.STRATEGY.FIRSTGROUPBASED;
    final static Strategy.STRATEGY strategyLast = Strategy.STRATEGY.LASTGROUPBASED;

    public static void main(String[] args) throws InterruptedException {

        final MessageConsummer consummer = UseCaseDefault(strategyFirst);
        consummer.stopProcessing();
        consummer.WaitForCompletion();
        final MessageConsummer consummerCancel = UseCaseCancellation(strategyFirst);
        consummerCancel.stopProcessing();
        consummerCancel.WaitForCompletion();
        final MessageConsummer consummerTerminate = UseCaseTermination(strategyFirst);
        consummerTerminate.stopProcessing();
        consummerTerminate.WaitForCompletion();
      
    }

    /**
     * Default use case. this is the case defined in the exercise
     *
     * @param strategy the default strategy
     * @return Message cumsummer
     */
    private static MessageConsummer UseCaseDefault(final Strategy.STRATEGY strategy) {
        final MessageConsummer consummer = new Scheduler(1, StrategyFactory.getStrategy(strategy), "Scheduler");
        final ClientEmulator client = new ClientEmulator(consummer);

        client.SendNewMessge(2, 1);
        client.SendNewMessge(1, 2);
        client.SendNewMessge(2, 3);
        client.SendNewMessge(3, 4);
        consummer.startProcessing();
        return consummer;
    }

    /**
     * Cancellation use case. Messages in group 3 are cancelled
     *
     * @param strategy the default strategy
     * @return Message consummer
     */
    private static MessageConsummer UseCaseCancellation(final Strategy.STRATEGY strategy) {
        final MessageConsummer consummer = new Scheduler(2, StrategyFactory.getStrategy(strategy), "Scheduler");
        final ClientEmulator client = new ClientEmulator(consummer);
        consummer.startProcessing();
        client.SendNewMessge(2, 1);
        client.SendNewMessge(1, 2);
        client.SendNewMessge(2, 3);
        client.SendNewMessge(3, 4);
        client.SendNewControlMessage(3, 5, Message.MESSAGE_TYPE.TYPE_CANCELLATION);

        client.SendNewMessge(1, 6);
        client.SendNewMessge(2, 7);
        client.SendNewMessge(3, 8);
        client.SendNewMessge(2, 9);
        client.SendNewMessge(1, 10);
        client.SendNewMessge(2, 11);
        client.SendNewMessge(3, 12);
        client.SendNewMessge(2, 13);
        client.SendNewMessge(1, 14);
        client.SendNewMessge(2, 15);
        client.SendNewMessge(3, 16);
        client.SendNewMessge(2, 17);
        client.SendNewMessge(1, 18);
        client.SendNewMessge(2, 19);
        client.SendNewMessge(3, 20);
        client.SendNewMessge(2, 21);
        client.SendNewMessge(1, 22);
        client.SendNewMessge(2, 23);
        client.SendNewMessge(3, 24);
        return consummer;
    }

    /**
     * Termination use case. After one message the group 3 sends termination
     *
     * @param strategy the default strategy
     * @return Message consummer
     */
    private static MessageConsummer UseCaseTermination(final Strategy.STRATEGY strategy) {
        final MessageConsummer consummer = new Scheduler(2, StrategyFactory.getStrategy(strategy), "Scheduler");
        final ClientEmulator client = new ClientEmulator(consummer);
        consummer.startProcessing();
        client.SendNewMessge(2, 1);
        client.SendNewMessge(1, 2);
        client.SendNewMessge(2, 3);
        client.SendNewMessge(3, 4);
        client.SendNewControlMessage(3, 5, Message.MESSAGE_TYPE.TYPE_TERMINATION);

        client.SendNewMessge(1, 6);
        client.SendNewMessge(2, 7);
        client.SendNewMessge(3, 8);
        client.SendNewMessge(2, 9);
        client.SendNewMessge(1, 10);
        client.SendNewMessge(2, 11);
        client.SendNewMessge(3, 12);
        client.SendNewMessge(2, 13);
        client.SendNewMessge(1, 14);
        client.SendNewMessge(2, 15);
        client.SendNewMessge(3, 16);
        client.SendNewMessge(2, 17);
        client.SendNewMessge(1, 18);
        client.SendNewMessge(2, 19);
        client.SendNewMessge(3, 20);
        client.SendNewMessge(2, 21);
        client.SendNewMessge(1, 22);
        client.SendNewMessge(2, 23);
        client.SendNewMessge(3, 24);
        return consummer;
    }
}
