package com.personal.examples;

import com.personal.client.ClientEmulator;
import com.personal.debug.Dumper;
import junit.framework.Assert;
import com.personal.interfaces.Message;
import com.personal.interfaces.MessageConsummer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.personal.scheduler.Scheduler;
import com.personal.scheduler.strategies.Strategy;
import com.personal.scheduler.strategies.StrategyFactory;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static com.personal.scheduler.strategies.Strategy.STRATEGY.LASTGROUPBASED;


public class AppTestLastGroupStrategy

{
	final Logger logger = LogManager.getFormatterLogger ("tests_LASTGROUPBASED");
	private Strategy strategy;
	@Before
	public void setup(){
		strategy = StrategyFactory.getStrategy (LASTGROUPBASED);
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
		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("4", "2", "1", "3"));
		validateResults (consummer, results);

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
		logger.debug ("dead letter queue %s", Dumper.dump (consummer.getDeadLetterQueue ()));
		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("2","6","1","3","7","9"));
		validateResults (consummer, results);
	}

	private void InitConsummer (MessageConsummer consummer) throws InterruptedException {
		consummer.startProcessing ();
		consummer.stopProcessing ();
		consummer.WaitForCompletion ();
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
		logger.debug ("dead letter queue %s", Dumper.dump (consummer.getDeadLetterQueue ()));
		final Queue<String> results = new ArrayDeque<String> (Arrays.asList ("4", "2","6", "1","3", "7", "9"));
		validateResults (consummer, results);

	}

	private void validateResults (MessageConsummer consummer, Queue<String> results) {
		while(!consummer.getDeadLetterQueue ().isEmpty ()) {
			Assert.assertEquals (results.poll (),String.valueOf (consummer.getDeadLetterQueue ().poll ().getID ()));
		}
	}
}
