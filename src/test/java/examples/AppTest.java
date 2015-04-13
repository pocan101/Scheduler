package examples;

import client.ClientEmulator;


import debug.Dumper;
import junit.framework.Assert;
import message.interfaces.Message;
import message.interfaces.MessageConsummer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import scheduler.Scheduler;
import scheduler.strategies.Strategy;
import scheduler.strategies.StrategyFactory;

import java.util.*;

import static scheduler.strategies.Strategy.STRATEGY.FIRSTGROUPBASED;


public class AppTest 

{
	final Logger logger = LogManager.getFormatterLogger ("tests_FIRSTGROUPBASED");
	private Strategy strategy;
	@Before
	public void setup(){
		strategy = StrategyFactory.getStrategy (FIRSTGROUPBASED);
	}
	@After
	public void cleaunp(){


	}

	@Test
	public  void UseCaseDefault () throws InterruptedException {

		final MessageConsummer consummer = new Scheduler (1, strategy, "Scheduler");
		final ClientEmulator client = new ClientEmulator (consummer);

		client.SendNewMessge (2, 1);
		client.SendNewMessge (1, 2);
		client.SendNewMessge (2, 3);
		client.SendNewMessge (3, 4);
		InitConsummer (consummer);
		logger.debug ("dead letter queue %s", Dumper.dump (consummer.getDeadLetterQueue ()));
		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("1", "3", "2", "4"));
		validateResults (consummer, results);

	}

	private void InitConsummer (MessageConsummer consummer) throws InterruptedException {
		consummer.startProcessing ();
		consummer.stopProcessing ();
		consummer.WaitForCompletion ();
	}

	@Test
	public  void UseCaseCancellation () throws InterruptedException {
		final MessageConsummer consummer = new Scheduler (2, strategy, "Scheduler");
		final ClientEmulator client = new ClientEmulator (consummer);

		client.SendNewMessge (2, 1);
		client.SendNewMessge (1, 2);
		client.SendNewMessge (2, 3);
		client.SendNewMessge (3, 4);
		client.SendNewControlMessage (3, 5, Message.MESSAGE_TYPE.TYPE_CANCELLATION);

		client.SendNewMessge (1, 6);
		client.SendNewMessge (2, 7);
		client.SendNewMessge (3, 8);
		client.SendNewMessge (2, 9);

		InitConsummer (consummer);
		logger.trace ("dead letter queue %s", Dumper.dump (consummer.getDeadLetterQueue ()));
		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("1", "3","7","9", "2","6"));
		validateResults (consummer, results);


	}
	@Test
	public  void UseCaseTermination () throws InterruptedException {
		final MessageConsummer consummer = new Scheduler (2, strategy, "Scheduler");
		final ClientEmulator client = new ClientEmulator (consummer);
		client.SendNewMessge (2, 1);
		client.SendNewMessge (1, 2);
		client.SendNewMessge (2, 3);
		client.SendNewMessge (3, 4);
		client.SendNewControlMessage (3, 5, Message.MESSAGE_TYPE.TYPE_TERMINATION);

		client.SendNewMessge (1, 6);
		client.SendNewMessge (2, 7);
		client.SendNewMessge (3, 8);
		client.SendNewMessge (2, 9);
		InitConsummer (consummer);
		logger.trace ("dead letter queue %s", Dumper.dump (consummer.getDeadLetterQueue ()));

		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("1", "3","7","9", "2","6", "4"));
		validateResults (consummer, results);




	}

	private void validateResults (MessageConsummer consummer, Queue<String> results) {
		while(!consummer.getDeadLetterQueue ().isEmpty ()) {
			Assert.assertEquals (String.valueOf (consummer.getDeadLetterQueue ().poll ().getID ()), results.poll ());
		}
	}
}
