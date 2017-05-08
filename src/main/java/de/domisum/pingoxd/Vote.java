package de.domisum.pingoxd;

public class Vote
{

	public final String authenticityToken;

	public final int surveyId;
	public final String id;
	public final boolean multipleChoice;
	public final int[] options;


	// INIT
	public Vote(String authenticityToken, int surveyId, String id, boolean multipleChoice, int... options)
	{
		this.authenticityToken = authenticityToken;
		this.surveyId = surveyId;

		this.id = id;
		this.multipleChoice = multipleChoice;
		this.options = options;

		if(!multipleChoice)
			if(options.length > 1)
				throw new IllegalArgumentException("Multiple options are only allowed in multiple choice sessions");
	}

	public String[] getEncodedOptions()
	{
		String[] encodedOptions = new String[this.options.length];
		for(int i = 0; i < this.options.length; i++)
			encodedOptions[i] = encodeOption(this.options[i]);

		return encodedOptions;
	}

	public String encodeOption(int option)
	{
		// this is not nicely done since the id is too big to be contained in long
		// TODO improve this encoding

		int modifiedLength = 5;
		String firstPartId = this.id.substring(0, this.id.length()-modifiedLength);
		String secondPartId = this.id.substring(this.id.length()-modifiedLength);

		int secondPartIdDecoded = Integer.decode("0x"+secondPartId);
		int secondPartOptionDecoded = secondPartIdDecoded+option;
		String secondPartOption = String.format("%02x", secondPartOptionDecoded);

		return firstPartId+secondPartOption;
	}

}
