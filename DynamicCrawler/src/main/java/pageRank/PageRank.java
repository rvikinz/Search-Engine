package pageRank;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageRank 
{
	public static Map<String, ArrayList<String>> 	Graph;
	public static Map<String, ArrayList<String>> 	nodeIncomingLinks;
	public static Map<String, Float> 			 	PageRank;
	public static int 								totalNodes;
	public static final float 						dampingFactor 	=  (float) 0.7;
	public static final float 						epsilon 		=  (float) 0.001;
	
	public PageRank(Map<String, ArrayList<String>>webGraph, Map<String, ArrayList<String>>webGraphIncomingLinks )
	{
		Graph 				= webGraph;
		nodeIncomingLinks 	= webGraphIncomingLinks;
		totalNodes 			= Graph.size();
		PageRank 			= new HashMap<String, Float>();
		initializingPageRankOfNodes();
	}
	
	// Initializing PageRank of all the nodes to 1
	public void initializingPageRankOfNodes()
	{
		for( String url : Graph.keySet() )
		{
			PageRank.put( url, (float) 1.0 );
		}
	}
	
	// Calculating PageRank values for each Page
	public void calculatePageRank( PrintWriter writer )
	{
		float firstTermPR = getFirstTermPageRank();
		float secndTermPR = (float) 0.0;
		float pageRank    = (float) 0.0;
		int iteration 	  = 0;
		Map<String, Float> newPageRank = new HashMap<String, Float>();
		
		do
		{
			if( iteration != 0 )
				updateNewPageRank( newPageRank );
			
			for( String url: Graph.keySet() )
			{
				secndTermPR = getSecondTermPageRank( url );
				pageRank = (float) ( firstTermPR + secndTermPR );
				newPageRank.put( url, pageRank );
				writer.print(url);
				writer.print( " - ");
				writer.println( pageRank );
				
			}
			
			printFinalPageRank( iteration++ );

		}while ( ! isConvergenceReached( newPageRank ) );
	}
	
	// check for Convergence value whether it is reached or not?
	public boolean isConvergenceReached( Map<String, Float>newPageRank )
	{
		float pr 	= (float) 0.0;
		float newpr = (float) 0.0;
		boolean convergenceReached = true;
		
		for( String url: PageRank.keySet() )
		{
			pr = PageRank.get(url);
			newpr = newPageRank.get(url);
			
			if( !(Math.abs( pr - newpr) < epsilon ) )
			{
				convergenceReached = false;
			}
		}
		return convergenceReached;
	}
	
	// Calculating 1st term in PageRank Algorithm ( 1 - d ) / N , d = dampingFactor, N = no of documents
	public float getFirstTermPageRank()
	{
		float f = (float) (1 - dampingFactor);
		f = (float) f/totalNodes;
		return f;
	}
	
	public float getSecondTermPageRank( String vertex )
	{
		float totPageRank = (float) 0.0;
		float nodePageRank = (float) 0.0;
		int numOfOutGoingLink = 0;
		
		ArrayList<String> incomingLinks = nodeIncomingLinks.get(vertex);
		ArrayList<String> outGoingLinkForNode;
		
		for( String link: incomingLinks )
		{
			nodePageRank = PageRank.get( link );
			outGoingLinkForNode = Graph.get(link);
			numOfOutGoingLink = outGoingLinkForNode.size();
			nodePageRank = (float) ( nodePageRank / numOfOutGoingLink );
			
			totPageRank = (float) ( totPageRank + nodePageRank );
		}
		totPageRank = (float) ( totPageRank * dampingFactor );
		return totPageRank;
	}
	
	public void updateNewPageRank( Map<String, Float> newPageRank )
	{
		for( String url : newPageRank.keySet() )
		{
			PageRank.put( url, newPageRank.get(url) );
		}
	}
	
	public void printFinalPageRank( int iteration )
	{
		System.out.println("-- Page Rank --" + " " + iteration + "  iteration" );
		for( String url: PageRank.keySet() )
		{
			System.out.println("PR( " + url + " ) - " + PageRank.get(url));
		}
	}

}
