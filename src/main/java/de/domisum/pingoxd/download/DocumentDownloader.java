package de.domisum.pingoxd.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DocumentDownloader
{

	// -------
	// DOWNLOADING
	// -------
	public Document download(String url)
	{
		try
		{
			url = URLDecoder.decode(url, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}


		try
		{
			return Jsoup.connect(url).timeout(5000).get();
		}
		catch(IOException e)
		{
			System.err.println("errorUrl: "+url);
			e.printStackTrace();
		}

		return null;
	}

}
