package de.domisum.pingoxd.download;

import lombok.Getter;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SurveyDataFetcher
{

	// constants
	private static final String SURVEY_URL = "http://pingo.upb.de/";

	// input
	private int surveyId;

	// output
	@Getter private String autheticityToken;
	@Getter private String id;
	@Getter private boolean multipleChoice;


	// INIT
	public SurveyDataFetcher(int surveyId)
	{
		this.surveyId = surveyId;
	}


	// FETCHING
	public boolean fetch()
	{
		DocumentDownloader documentDownloader = new DocumentDownloader();


		String url = SURVEY_URL+this.surveyId;
		Document document = documentDownloader.download(url);
		if(document == null)
			return false;


		Elements authenticityTokenElements = document.select("input[name=authenticity_token]");
		this.autheticityToken = authenticityTokenElements.first().val();
		if(this.autheticityToken == null)
			return false;


		Elements idElements = document.select("input[name=id]");
		if(idElements.first() == null)
			return false;
		this.id = idElements.first().val();
		if(this.id == null || this.id.equals(""))
			return false;


		Elements multipleChoiceElements = document.select("input[type=checkbox]");
		multipleChoiceElements.forEach((element->
		{
			String name = element.attr("name");
			if(name.startsWith("option") && name.endsWith("[]"))
				this.multipleChoice = true;
		}));

		return true;
	}

}
