package indexing;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import searchServlet.SearchEngineServlets;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class Indexer 
{
	public static StandardAnalyzer analyzer = new StandardAnalyzer();
	
	public void createIndex( ArrayList<WebDocuments> pages )
	{
		System.out.println("-------Creating Index-----------");
		IndexWriter writer 	= null;
		File index 			= new File( SearchEngineServlets.INDEX_DIR );
		Document luceneDoc;
		try
		{
			//Before creating the index, delete any previous indexing files
			FileUtils.cleanDirectory(new File( SearchEngineServlets.INDEX_DIR ) );
			
			IndexWriterConfig indexConfig = new IndexWriterConfig( analyzer );
			Path path = Paths.get( SearchEngineServlets.INDEX_DIR );
			writer = new IndexWriter( FSDirectory.open( path ), indexConfig );
			
			System.out.println("Indexing to direcotry: " + index + " .." );
			
			for( WebDocuments page: pages )
			{
				luceneDoc = new Document();
				Field titleField = new Field( "title", page.title, Field.Store.YES, Field.Index.ANALYZED );
				
				Field metaField = new Field("meta", page.metaData, Field.Store.YES, Field.Index.ANALYZED);
				
				titleField.setBoost(2.0f);
				metaField.setBoost(1.5f);
				luceneDoc.add( new Field( "text", page.body, Field.Store.YES, Field.Index.ANALYZED ));
				luceneDoc.add( new Field( "fileName", page.fileName, Field.Store.YES, Field.Index.NO ));
				luceneDoc.add( titleField );
				luceneDoc.add( metaField );
				writer.addDocument( luceneDoc );
			}
		}
		catch( Exception ex )
		{
			System.out.println("Exception in Creating Index");
		}
		finally 
		{
			if (writer !=null)
				try 
				{
					writer.close();
				} 
				catch (CorruptIndexException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
		}
	}
}

