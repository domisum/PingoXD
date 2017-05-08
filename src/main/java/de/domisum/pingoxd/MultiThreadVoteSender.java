package de.domisum.pingoxd;

import de.domisum.lib.auxilium.util.java.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadVoteSender
{

	// settings
	private final Vote vote;
	private final int numberOfVotes;
	private final int numberOfThreads;

	// temp
	private List<Thread> threads = new ArrayList<>();
	private int votesSent = 0;


	// INIT
	public MultiThreadVoteSender(Vote vote, int numberOfVotes, int numberOfThreads)
	{
		this.vote = vote;
		this.numberOfVotes = numberOfVotes;
		this.numberOfThreads = numberOfThreads;
	}


	// sending
	public void sendVotes()
	{
		for(int i = 0; i < this.numberOfThreads; i++)
			startThread();

		while(!this.threads.isEmpty())
		{
			this.threads.removeIf((t)->!t.isAlive());
			System.out.println("Threads active: "+this.threads.size());

			ThreadUtil.sleep(1000);
		}

		System.out.println("All threads ded");
	}

	private void startThread()
	{
		Thread thread = new Thread(this::threadRun);
		this.threads.add(thread);
		thread.start();
	}

	private void threadRun()
	{
		VoteSender voteSender = new VoteSender();

		while(this.votesSent < this.numberOfVotes)
		{
			String error = voteSender.send(this.vote);
			if(error != null)
			{
				System.out.println(error);

				if(error.equals("Tiemout"))
					ThreadUtil.sleep(300);

				continue;
			}

			this.votesSent++;
			System.out.println("Sent vote "+this.votesSent);
		}
	}

}
