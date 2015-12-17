package pageRank;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * This class represents all the Pages in Graph Data Structure
 */
public class WebGraph 
{
	// Adjacency list representation of each node which contains list of outgoing edges
	public static Map<String, ArrayList<String> > WebGraph;
	
	// Adjacency list representation of each node which contains list of incoming nodes
	public static Map<String, ArrayList<String>>  mInComingLinks;
	
	public WebGraph()
	{
		WebGraph 		= new HashMap<String, ArrayList<String>>();
		mInComingLinks 	= new HashMap<String, ArrayList<String>>();
	}
	
	/*
	 * Creating Graph Data Structure to store pages and there links in WebGraph map of all the pages available
	 */
	public void createWebGrpahOfAllPages( ArrayList<String> filePaths, ArrayList<String> urlList )
	{
		for( int i=0; i<filePaths.size(); i++ )
		{
			parseFileForHyperLinks( filePaths.get(i), urlList.get(i) );
		}
		createIncomingPagesMapOnApage();
	}
	
	public void parseFileForHyperLinks( String filePath, String mainURL )
	{
		try
		{
			ArrayList<String> outGoingURLs = new ArrayList<String>();
			File file = new File( filePath );
			Document doc = Jsoup.parse( file, "UTF-8", "" );
			Elements links = doc.select( "a[href]" );
			
			for( Element link : links )
			{
				String url = link.attr("abs:href");
				if( url.toLowerCase().contains(".edu") )
				{
					outGoingURLs.add( url );
					System.out.println(" url: " + link.attr("abs:href") );
				}	
			}
			
			WebGraph.put( mainURL, outGoingURLs );
			mInComingLinks.put( mainURL, new ArrayList<String>() );
		}
		catch( Exception ex )
		{
			System.out.println("Exception in creating WebGraph");
		}
	}
	
	/*
	 * Creating Graph Data Structure to store incoming links for a page in mInComingLinks of all the available pages
	 */
	public void createIncomingPagesMapOnApage()
	{
		ArrayList<String> urlList;
		ArrayList<String> urls;
		
		for( String url: WebGraph.keySet() )
		{
			urlList = WebGraph.get(url);
			for( String link : urlList )
			{
				urls = mInComingLinks.get(link);
				if( urls == null )
				{
					urls = new ArrayList<String>();
					urls.add(url);
					mInComingLinks.put( link, urls );
				}
				else
				{
					urls.add( url );
					mInComingLinks.put( link, urls );
				}
			}
		}
	}
	
	// Print Graphs
	public void printGraph( Map<String, ArrayList<String>> graph )
	{
		System.out.println("Printing WebGraph");
		for( String url : graph.keySet() )
		{
			System.out.print( url + "  --> " );
			ArrayList<String> urlList = graph.get(url);
			for( String link : urlList )
			{
				System.out.print( "  " + link );
			}
			System.out.println();
		}
	}
}
