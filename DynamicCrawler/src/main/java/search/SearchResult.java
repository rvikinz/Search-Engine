package search;

import java.util.Comparator;

public class SearchResult 
{
	String fileName;
	String url;
	String title;
	String snippet;
	float score;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public float getScore() {
		return score;
	}

	public void setScore( float score) {
		this.score = score;
	}
	
	public SearchResult( String fileName, String url, String title, String snippet, float score )
	{
		this.fileName = fileName;
		this.url = url;
		this.title = title;
		this.snippet = snippet;
		this.score = score;
	}
}

class SearchResultUtil implements Comparator<SearchResult>
{
	public int compare( SearchResult firstSR, SearchResult secSR )
	{
		if( firstSR.getScore() < secSR.getScore() )
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}
