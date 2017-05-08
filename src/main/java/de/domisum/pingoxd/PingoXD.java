package de.domisum.pingoxd;

import de.domisum.pingoxd.download.SurveyDataFetcher;

import java.util.Arrays;

public class PingoXD
{

	public static void main(String[] args)
	{
		if(args.length != 4)
		{
			System.err.println("Expecting <surveyId> <options> <votes> <threads>");
			return;
		}

		int surveyId = parseInt(args[0]);
		int[] options = parseOptions(args[1]);
		int numberOfVotes = parseInt(args[2]);
		int numberOfThreads = parseInt(args[3]);

		if(surveyId == 0 || options == null || numberOfVotes == 0 || numberOfThreads == 0)
			return;

		System.out.println("surveyId: "+surveyId);
		System.out.println("options: "+Arrays.toString(options));
		System.out.println("threads: "+numberOfVotes);
		System.out.println("threads: "+numberOfThreads);
		System.out.println();


		SurveyDataFetcher surveyDataFetcher = new SurveyDataFetcher(surveyId);
		boolean fetchSuccess = surveyDataFetcher.fetch();
		if(!fetchSuccess)
		{
			System.out.println("Fetching survey data failed");
			return;
		}


		Vote vote = new Vote(surveyDataFetcher.getAutheticityToken(), surveyId, surveyDataFetcher.getId(),
				surveyDataFetcher.isMultipleChoice(), options);

		MultiThreadVoteSender multiThreadVoteSender = new MultiThreadVoteSender(vote, numberOfVotes, numberOfThreads);
		multiThreadVoteSender.sendVotes();
	}


	// PARSING
	private static int parseInt(String intString)
	{
		try
		{
			return Integer.parseInt(intString);
		}
		catch(Exception e)
		{
			System.err.println("'"+intString+"' is not a number");
		}

		return 0;
	}

	private static int[] parseOptions(String optionsString)
	{
		try
		{
			String[] optionsSplit = optionsString.split(",");
			int[] options = new int[optionsSplit.length];
			for(int i = 0; i < optionsSplit.length; i++)
				options[i] = parseOption(optionsSplit[i]);

			return options;
		}
		catch(Exception e)
		{
			System.err.println("'"+optionsString+"' is not a valid option selector");
		}

		return null;
	}

	private static int parseOption(String optionString)
	{
		optionString = optionString.toLowerCase();
		int option = -1;

		if(optionString.matches("^[0-9]+$"))
			option = Integer.parseInt(optionString);

		if(optionString.matches("^[a-z]$"))
			option = optionString.charAt(0)-96;


		if(option < 1 || option > 100)
			throw new IllegalArgumentException("The option ids have to be in the range 1-100");

		return option;
	}

}
