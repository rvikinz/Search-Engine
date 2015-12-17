package indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ReadDocuments 
{
	public int docCount;
	public ArrayList<String> filePaths;
	public ArrayList<File> files;
	public String dirPath;
	public ArrayList<String> urlList;
	public ArrayList<WebDocuments> webDocuments;
	
	public ReadDocuments( String dirPath )
	{
		this.dirPath = dirPath;
		files = new ArrayList<File>();
	}
	
	public void readAllFilesFromDir()
	{
		try
		{
			System.out.println("dir path: " + dirPath );
			filePaths = new ArrayList<String>();
			File folder = new File( dirPath );
			String fileName = "";
			String filepath = "";
			
			for( File file:folder.listFiles() )
			{
				files.add(file);
				if( file.isFile() )
				{	
					fileName =  file.getName();
					if( fileName.toLowerCase().contains("html") || fileName.toLowerCase().contains("htm"))
					{
						filepath = dirPath + "/" + fileName;
						System.out.println("File Name: " + filepath );
						filePaths.add( filepath );
						docCount++;
					}
				}
			}
			
			getURLList();
		}
		catch( Exception ex )
		{
			System.out.println("Exception in ReadDocuments");
		}
	}
	
	public void getURLList()
	{
		urlList = new ArrayList<String>();
		try
		{
			FileReader fr = new FileReader( dirPath + "/downloadedURLs.txt" );
			BufferedReader br = new BufferedReader( fr );
			String line = null;
			
			while( (line = br.readLine() ) != null )
			{
				urlList.add( line );
			}
		}
		catch( IOException ex )
		{
			System.out.println(" Exception in getting URL list ");
		}
	}
	
	public void parseDocuments()
	{
		Document htmlFile;
		String title;
		String body;
		WebDocuments wd;
		String htmlMetaData = "";
		webDocuments = new ArrayList<WebDocuments>();
		
		try
		{
			for( File file : files )
			{
				htmlFile = Jsoup.parse( file, "UTF-8", "" ); 
				title = htmlFile.title();
				if( htmlFile.body() != null )
					body = htmlFile.body().text();
				else
					body = "";
				
			    htmlMetaData = getMetaDataForDocument(htmlFile);
			    String fileName = file.getName();
			    wd = new WebDocuments(title, body, fileName, htmlMetaData );
				webDocuments.add( wd );
				
			}
		}
		catch( IOException ex )
		{
			System.out.println("Exception in JSoup parsing");
		}
	}
	
	//get Metadata for the document passed
	public static String getMetaDataForDocument(org.jsoup.nodes.Document doc){
		String metaData = "";
		for(org.jsoup.nodes.Element meta : doc.select("meta")) {
			//System.out.println("Name: " + meta.attr("name") + " - Content: " + meta.attr("content"));
			metaData += meta.attr("content") + " ";
		}
		return metaData;
	}
}
