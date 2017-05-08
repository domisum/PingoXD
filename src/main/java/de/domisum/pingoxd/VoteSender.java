package de.domisum.pingoxd;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

public class VoteSender
{

	// constants
	private static final String PINGO_VOTE_URL = "http://pingo.upb.de/vote";


	// SENDING
	public String send(Vote vote)
	{
		StringBuilder data = new StringBuilder();
		try
		{
			data.append(encodeKeyValue("utf8", "✓")).append("&");
			data.append(encodeKeyValue("authenticity_token", vote.authenticityToken)).append("&");
			for(String encodedOption : vote.getEncodedOptions())
				data.append(encodeKeyValue("option"+(vote.multipleChoice ? "[]" : ""), encodedOption)).append("&");
			data.append(encodeKeyValue("id", vote.id)).append("&");
			data.append(encodeKeyValue("commit", "Vote!"));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		try
		{
			URL url = new URL(PINGO_VOTE_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setConnectTimeout(500);
			con.setReadTimeout(500);


			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", String.valueOf(data.length()));

			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			/*con.setRequestProperty("Referer", "http://pingo.upb.de/"+vote.surveyId);
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Upgrade-Insecure-Requests", "1");*/

			OutputStream os = con.getOutputStream();
			os.write(data.toString().getBytes());
			os.close();

			int responseCode = con.getResponseCode();
			if(responseCode != 200)
				return responseCode+" "+con.getResponseMessage();
		}
		catch(java.io.IOException e)
		{
			if(e instanceof SocketTimeoutException)
				return "Timeout";

			e.printStackTrace();
		}

		return null;
	}

	private String encodeKeyValue(String key, String value) throws UnsupportedEncodingException
	{
		String keyEncoded = URLEncoder.encode(key, "UTF-8");
		String valueEncoded = URLEncoder.encode(value, "UTF-8");

		return keyEncoded+"="+valueEncoded;
	}

}
