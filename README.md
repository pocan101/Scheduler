1) To compile use maven 3. just issue mvn clean install and wait for tests to complete. To build the java docs issue mvn site
2)Test will test the basic algorithms for use case default and termination and cancellation using two different strategies. Is important to recall than processing starts when all messages are already queue in the scheduler for easy tracking and predictibility
3)For real case scenario in which messages are received after processing starts just issue java -jar Scheduler-1.0-SNAPSHOT.jar
4)Strategies can be swap on the fly.
5)ONlY THE DEPENDENCIES LOG4J IS USED. Gson is only for debugging purposes. THERE IS NOT UNIT TESTING IMPLEMENTED SO THERE IS NO NEED FOR A MOCKING FRAMEWORK.
6)Ill be happy to respond questions about unit testing in the interview. I undestood there was no obligation to implement it. I used TDD for plotting the scenarios before proper coding the solution.
7)Patterns used in this solution are:
	
		a)Decorator for introducing the scheduler functionality. it decores the standar calls to the resource direcly with fancy functionality.
		b)Factory for the strategies
		c)Builder for messages
		Others
8) This solution favors non blocking thread safety based on swap and compare.
9) Also I have worked in the High availability scenario for this solution. According to my criteria this solution isnt really for high performance and high scallability however it is a strong candiadte for high availability and geographical redundancy. Ill be happy to share my comments with you in interview and show you what ive done.

10) Please let me know your thoughts. Looking forward to hearing your feedback
