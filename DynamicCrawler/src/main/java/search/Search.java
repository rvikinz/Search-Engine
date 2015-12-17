package search;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;

import indexing.Indexer;
import searchServlet.SearchEngineServlets;

public class Search 
{	
	public  List<SearchResult> search (String queryString, int topk, ArrayList<String> filePaths, ArrayList<String> urls ) 
	{
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		try
		{
			SearchResult srchResult;
			Path path = Paths.get( SearchEngineServlets.INDEX_DIR );
			IndexReader indexReader = DirectoryReader.open(FSDirectory.open( path ));
			IndexSearcher indexSearcher = new IndexSearcher( indexReader );
			QueryParser queryparser = new QueryParser( "text", Indexer.analyzer );
			TopScoreDocCollector collector;
		
			StringTokenizer strtok = new StringTokenizer(queryString, " ~`!@#$%^&*()_-+={[}]|:;'<>,./?\"\'\\/\n\t\b\f\r");
			String querytoparse = "";
			
			if( SearchEngineServlets.tfidfScoring == 1 || SearchEngineServlets.tfidfPageRankScoring == 1 )
			{
				while(strtok.hasMoreElements()) 
				{
					String token = strtok.nextToken();
					querytoparse += "text:" + token + "^1" + "title:" + token+ "^1.5" + "meta:" + token+ "^1.5";
				}
			}
			else if( SearchEngineServlets.pageRankScoring == 1)
			{
				while(strtok.hasMoreElements()) 
				{
					String token = strtok.nextToken();
					querytoparse += "text:" + token + "^1" + "title:" + token+ "^1" + "meta:" + token+ "^1";
				}
			}
			
			Query query = queryparser.parse( querytoparse );
			collector = TopScoreDocCollector.create( 20 );
			//TopDocs results = indexSearcher.search( query, topk );
			indexSearcher.search( query, collector );
			ScoreDoc[] results = collector.topDocs().scoreDocs; 
			
			System.out.println( "Score Length: " + results.length + " Results Found\n" );	
 
			for(int i=0; i<results.length; i++)
			{
				int docID = results[i].doc;
				String filename = "";
				Document resultDoc = indexSearcher.doc( docID );
				Field field = (Field) resultDoc.getField( "fileName" );
					
				//String filename = resultDoc.getField( "fileName" ).stringValue();
				if( field != null )
				{
					filename = field.stringValue();
					System.out.println( filename );
				}
				
				//Find out which URL to hit, when user selects that result from list of visible results
				//String fileName = resultDoc.getField("fileName").stringValue();
				//String urlToHit = getURLFromFileName(fileName);
				
				String snippetToShow = generateSnippet(queryString , resultDoc);

				String strFileNum = filename.replaceAll( "[^0-9]", "");
				int fileNumber = Integer.parseInt( strFileNum );

				
				srchResult = new SearchResult( resultDoc.get( "fileName" ), urls.get( fileNumber ), resultDoc.get( "title" ), snippetToShow, results[i].score );
				searchResults.add( srchResult );
				System.out.println(snippetToShow+"\n");

				//Continue from here ... . . .  .. .  . .. .			
			}
	
			if( SearchEngineServlets.pageRankScoring == 1 || SearchEngineServlets.tfidfPageRankScoring == 1 )
			{
				
			}
			return searchResults;			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			//indexSearcher.close();
		}
		
		return null;
	}

	//Done Fetching URL for the results , below function will return the corresponding URL to the fileNo in the results of search
/*
	private static String getURLFromFileName(String fileName) {
		String[] parts = fileName.split(Pattern.quote("."));
		String FileNamePrefix = parts[0];
		FileNamePrefix = FileNamePrefix.replace("file", "");
		int lineNumberToRead = Integer.parseInt(FileNamePrefix);
		String urlResult = null;
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("/Users/Piyush/Documents/workspace/SampleLucene/downloaded files/downloadedURLs.txt")))) {
		    String line;
		    int lineCount = 0;
		    while ((line = br.readLine()) != null) {
		       lineCount++;
		       //System.out.println("URL Of the Result: "+ line);
		       urlResult = line;
		       if(lineCount == lineNumberToRead)
		    	   break;
		       }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlResult;
	}
	*/
	
	public List<SearchResult> calculateNewScore( List<SearchResult> searchResults, Map<String, Float> pageRank)
	{
		String url = "";
		float score = (float) 0.0;
		float pr = (float) 0.0;
		List<SearchResult> newScoreResult = new ArrayList<SearchResult>();
		
		for( SearchResult sr : searchResults )
		{
			url = sr.url;
			if( pageRank.get( url ) != null)
			{
				score = sr.score;
				pr = pageRank.get(url);
				score = score * pr;
				
				SearchResult nsr = new SearchResult( sr.fileName, sr.url, sr.title, sr.snippet, score );
				newScoreResult.add( nsr );
			}
		}
		
		Collections.sort( newScoreResult, new SearchResultUtil() );
		return newScoreResult;
	}

	//Generate the snippet for a given document, 
public static String generateSnippet(String queryString , Document resultDoc){
		
		int snippetStartingIndex = 0;
		int snippetEndIndex = 0;

		//Modify this line to show the No of Characters to be present in the Snippet (chars twice this value will be shown)
		int CharsCount = 150;

		String htmlSnippet = "";
	
		String documentBody = resultDoc.getField("text").stringValue();
		int termPosition = documentBody.toLowerCase().indexOf(queryString.toLowerCase());
		
		//If the entire query is not present, use the first term from query
		if(termPosition==-1){
			String firstTerm = "";
			String queryTokens[] = queryString.split(" ");
			if(queryTokens.length >1)
				firstTerm = queryTokens[0];	
			termPosition = documentBody.toLowerCase().indexOf(firstTerm.toLowerCase());
		}
		
		//If still the term is not found
		if(termPosition==-1 && documentBody.length()>CharsCount*2){
			snippetStartingIndex = 0;
			snippetEndIndex = CharsCount*2;
			htmlSnippet = documentBody.substring(snippetStartingIndex, snippetEndIndex); 
			return htmlSnippet;
		}
			
		//If the search term is appearing in the beginning of the document
		if(termPosition < CharsCount && documentBody.length()>CharsCount*2){
			snippetStartingIndex = 0;
			snippetEndIndex = CharsCount*2;
			htmlSnippet = documentBody.substring(snippetStartingIndex, snippetEndIndex); 
		}
		//If the search term is appearing at the end of the document
		else if(documentBody.length()-termPosition <CharsCount && documentBody.length()>CharsCount*2){
			snippetStartingIndex = documentBody.length() - (CharsCount*2);
			snippetEndIndex = documentBody.length()-1;
			htmlSnippet = documentBody.substring(snippetStartingIndex, snippetEndIndex); 
		}
		//If the search term is appearing somewhere in the middle of the document 
		else if(documentBody.length()>CharsCount*2){
			snippetStartingIndex = termPosition - CharsCount;
			snippetEndIndex = termPosition + CharsCount;
			htmlSnippet = documentBody.substring(snippetStartingIndex, snippetEndIndex); 
		}
		//In case of error or Document length not sufficient
		else 
			htmlSnippet = resultDoc.getField("text").stringValue(); //Piyush
		
		//Format the html snippet
		if(htmlSnippet.length()> 1){
			 String [] arr = htmlSnippet.split(Pattern.quote("."), 2);
			 if(arr.length > 1)
				 htmlSnippet =  arr[1];
			 htmlSnippet = htmlSnippet.trim();
		}
		return htmlSnippet;
	}
}
