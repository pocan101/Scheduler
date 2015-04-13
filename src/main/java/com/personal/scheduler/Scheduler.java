package com.personal.scheduler;

import com.personal.debug.Dumper;
import com.personal.exceptions.GroupTerminatedException;
import com.personal.gateway.Gateway;
import com.personal.gateway.SimpleGatewayImpl;
import com.personal.interfaces.Message;
import com.personal.interfaces.MessageConsummer;
import com.personal.scheduler.strategies.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.personal.scheduler.data.BasicData;
import com.personal.scheduler.data.BasicKey;
import com.personal.scheduler.strategies.Strategy;

import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jose Cortes on 4/9/2015.
 * Class that models the Scheduler. This Scheduler will send messages to an array of resources through a gateway per resource, according to  an strategy
 */
public final class Scheduler implements MessageConsummer {


    final Logger logger = LogManager.getFormatterLogger("scheduler");
    final Logger loggerThread = LogManager.getFormatterLogger("thread");
    final AtomicBoolean isAvailable;
    final CompletionService<Message> pool;
    final ExecutorService executor;
    private final int numberOfGateways;
    private final String id;
    private final ConcurrentLinkedQueue<Gateway> gateways;
    /**
     * Data structure to store the messages
     */
    private final ConcurrentSkipListMap<BasicKey, BasicData> internalQueues;
    /**
     * Structure to keep track of messages sent to the gateway
     */
    private final ConcurrentLinkedQueue<Message> deadLetterQueue = new ConcurrentLinkedQueue<Message>();
    private TaskPlanner executorThread;
    private Strategy strategy;
    private long sequence;
    /**
     * Structure to manatain the groups that have been black listed
     */
    private ConcurrentMap<String, GROUP_SECURITY_STATUS> blackListedGroups;

    /**
     * Creates an instance of the scheduler class
     *
     * @param numberOfGateways Number of gateways to manage internally
     * @param strategy         Strategy to retrieve messages
     * @param id               of the Scheduler
     */
    public Scheduler(final int numberOfGateways, final Strategy strategy, String id) {

        this.id = id;
        this.numberOfGateways = numberOfGateways;
        this.setStrategy(strategy);

        gateways = new ConcurrentLinkedQueue<Gateway>();

        for (int i = 0; i < numberOfGateways; i++) {
            gateways.add(new SimpleGatewayImpl("Resource" + i));
        }
        final Comparator<BasicKey> comp = new Comparator<BasicKey>() {
            @Override
            public int compare(BasicKey o1, BasicKey o2) {
                return o1.compareTo(o2);
            }
        };
        internalQueues = new ConcurrentSkipListMap<BasicKey, BasicData>(comp);
        isAvailable = new AtomicBoolean(true);
        executor = Executors.newFixedThreadPool(numberOfGateways);
        pool = new ExecutorCompletionService<Message>(executor);
        executorThread = new TaskPlanner();
        blackListedGroups = new ConcurrentHashMap<String, GROUP_SECURITY_STATUS>();
    }

    /**
     * Current thread waits until the consummer is done with msg load
     *
     * @throws InterruptedException
     */
    public void WaitForCompletion() throws InterruptedException {
        executorThread.join();
    }

    /**
     * Gets scheduler id
     *
     * @return id
     */
    public String getID() {
        return id;
    }

    /**
     * Stop processing messages in this Scheduler
     *
     */
    public void stopProcessing() {

        isAvailable.set(false);
    }

    /**
     * Start processing messages in this Scheduler
     *
     */
    public void startProcessing() {
        executorThread.start();
    }

    /**
     * Checks whether the scheduler is available to receive messages
     *
     * @return String
     */
    public AtomicBoolean isAvailable() {
        return isAvailable;
    }

    /**
     * Stores message into the internal data structure to be retrieved for the collection strategy
     *
     * @param msg the msg
     * @throws GroupTerminatedException
     */
    public void consumme(final Message msg) throws GroupTerminatedException {

        logger.debug(" received message to queue from group %s", msg.getGroup());
        final String key = String.valueOf(msg.getGroup());

        avoidTerminatedGroups(msg);

        if (msg.getType() != Message.MESSAGE_TYPE.TYPE_INFORMATION) {
            provisionBlackListedGroups(msg);
            return;
        }


        final ConcurrentLinkedQueue<Message> value = new ConcurrentLinkedQueue<Message>();
        value.add(msg);
        final long arrivingOrder = System.currentTimeMillis();

        if (!keyExists(msg, key)) {
            internalQueues.put(new BasicKey(arrivingOrder, key), new BasicData(value));
        }
        logger.trace(" internalQueues dump %s ", Dumper.dump(internalQueues));
    }

    /**
     * Checks whether the keys already exists in the map
     *
     * @param msg message to be add to the data queue of this structure in the given key
     * @param key the key in which the message has to be stored
     * @return Boolean that indicates existence
     */
    private Boolean keyExists(Message msg, String key) {
        for (BasicKey keyCurrent : internalQueues.keySet()) {

            if (keyCurrent.getGroup().equals(key)) {

                internalQueues.get(keyCurrent).getMessageList().add(msg);
                return true;
            }
        }
        return false;
    }


    /**
     * Adds the message group to the blacklisted queue in case that the message is a control message for cancellation or termination
     *
     * @param msg the message
     */
    private void provisionBlackListedGroups(final Message msg) {

        switch (msg.getType()) {
            case TYPE_CANCELLATION:
                blackListedGroups.put(String.valueOf(msg.getGroup()), GROUP_SECURITY_STATUS.CANCELLED);
                break;
            case TYPE_TERMINATION:
                blackListedGroups.put(String.valueOf(msg.getGroup()), GROUP_SECURITY_STATUS.TERMINATED);
                break;
        }
    }

    /**
     * If the group has been terminated the message sould be stored
     *
     * @param msg message
     * @throws GroupTerminatedException
     */
    private void avoidTerminatedGroups(Message msg) throws GroupTerminatedException {
        final String key = String.valueOf(msg.getGroup());
        if (blackListedGroups.containsKey(key)) {

            if (blackListedGroups.get(key) == GROUP_SECURITY_STATUS.TERMINATED) {
                throw new GroupTerminatedException();
            }
        }
    }

    /**
     * Sets strategy for processing messages. Notice that the new strategy take over the for the rest of the precessing
     *
     * @param strategy
     */
    public void setStrategy(final Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Makes the gateway available after processing
     *
     * @param g
     * @return Boolean that indicates success
     */
    private Boolean makeGatewayAvailable(Gateway g) {
        return g.isAvailable().compareAndSet(false, true);
    }

    /**
     * Sends message to the gateway
     *
     * @param g       the gateway to which the message has to be sent
     * @param message the message
     * @return boolean that indicates wheter it has been sent or not
     */
    private Boolean sendToGateway(final Gateway g, final Message message) {
        loggerThread.debug("gateway available. sending message %s from group %s to gateway %s", message.getID(), message.getGroup(), g.getID());
        final AsyncTask task = new AsyncTask(message, g);
        pool.submit(task);
        deadLetterQueue.add(message);
        return true;
    }

    /**
     * Checks whether the message has to be processed
     *
     * @param message
     * @return boolena indicated whether the message sould be sent or not
     */
    private boolean messageIsNotBanned(final Message message) {
        final String key = String.valueOf(message.getGroup());
        return (!blackListedGroups.containsKey(key)) ? true : blackListedGroups.get(key).equals(GROUP_SECURITY_STATUS.TERMINATED);
    }

    /**
     * Gets the dead letter queue that stores all sent messages to the gateway
     *
     * @return queue
     */
    public ConcurrentLinkedQueue<Message> getDeadLetterQueue() {
        return deadLetterQueue;
    }

    public enum GROUP_SECURITY_STATUS {CANCELLED, TERMINATED}

    /**
     * Class that models an task planner  in the scheduler.
     */
    private final class TaskPlanner extends Thread {
        /**
         * Runs the task planner in background
         */
        public void run() {

            while (isAvailable.get() || !internalQueues.isEmpty()) {

                sendNextMessage();
            }
            executor.shutdown();

        }

        /**
         * Sends message to the gateway
         *
         * @return boolean indicates success
         */
        private Boolean sendNextMessage() {
            for (Gateway g : gateways) {

                if (g.isAvailable().compareAndSet(true, false)) {
                    final Message message = strategy.getNextMessage(internalQueues);
                    if (message != null) {

                        return (messageIsNotBanned(message)) ? sendToGateway(g, message) : makeGatewayAvailable(g);
                    }
                    makeGatewayAvailable(g);
                }
            }
            return false;
        }
    }

    /**
     * Immutable Class that models an asynchronous task that sends message to the gateway. ONly one thread reserved per gateway that will be reused
     */
    private final class AsyncTask implements Callable<Message> {
        final Message m;
        final Gateway g;

        /**
         * Creates the tasks
         *
         * @param m the message to be sent
         * @param g the gateway
         */
        AsyncTask(final Message m, final Gateway g) {

            this.m = m;
            this.g = g;
        }

        public Message call() throws Exception {
            try {
                g.send(m);
            } catch (GroupTerminatedException e) {
                loggerThread.error(e.getMessage());
            }
            return m;
        }
    }
}
