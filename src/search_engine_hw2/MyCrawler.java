package search_engine_hw2;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	CrawlStat myCrawlStat;
	int id  ;
	String toappend_fetch;
	String toappend_visit;
	String toappend_urlnewsite;
	static String storageFolderName; 
	public static String [] checkDomainlist = {"https://www.nbcnews.com/","https://media2.s-nbcnews.com/"};
	public static File storageFolder;
	public MyCrawler() {
		toappend_fetch = toappend_visit =toappend_urlnewsite="";
		appendfile.deletefile(storageFolderName+"/"+String.valueOf(id)+"urls_NewsSite.txt");
		appendfile.deletefile(storageFolderName+"/"+String.valueOf(id)+"fetch_NewsSite.txt");
		appendfile.deletefile(storageFolderName+"/"+String.valueOf(id)+"visit_NewsSite.txt");
	}
	public boolean checkdomain( WebURL url) {
		for (String s: checkDomainlist) {
			if ( url.getURL().toString().contains( s ) )  return true;
		}
		return false;
	}
	
	public static void configure(String[] domain, String storageFolderName1) {
		checkDomainlist = domain;
		storageFolder = new File(storageFolderName1);
		storageFolderName = storageFolderName1;
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
        
        
    }
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|"
			+ "mp3|mp3|zip|gz))$");
			/**
			* This method receives two parameters. The first parameter is the page
			* in which we have discovered this new url and the second parameter is
			* the new url. You should implement this function to specify whether
			* the given url should be crawled or not (based on your crawling logic).
			* In this example, we are instructing the crawler to ignore urls that
			* have css, js, git, ... extensions and to only accept urls that start
			* with "http://www.viterbi.usc.edu/". In this case, we didn't need the
			* referringPage parameter to make the decision.
			*/
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		
		if   (checkdomain(url)) myCrawlStat.total_unique_domain_url++;
		else   myCrawlStat.total_unique_outdomain_url++;
		
		if (href.lastIndexOf('?')>0) href= href.substring(0, href.lastIndexOf('?'));
		return !FILTERS.matcher( href).matches()
		&& checkdomain(url);
		}
	@Override
	public void visit(Page page) {
		id = getMyId();
		String url = page.getWebURL().getURL();
		myCrawlStat.incProcessedPages();
		if  ( checkdomain(page.getWebURL()))  myCrawlStat.total_unique_domain_url++;
		int page_size =  (page.getContentData().length)/1000;
			     if (page_size<=1) myCrawlStat.file_1kb++;
			else if (page_size>1 && page_size<=10) myCrawlStat.file_1kb10kb++;
			else if (page_size>10 && page_size<=100) myCrawlStat.file_10kb100kb++;
			else if (page_size>100 && page_size<=1000) myCrawlStat.file_100kb1mb++;
			else if (page_size>1000 ) myCrawlStat.file_1mblarger++;
			
		String page_type =  page.getContentType();
		
		if      (page_type.contains("html")) { myCrawlStat.texthtml++;}
		else if (page_type.contains("gif"))  { myCrawlStat.imagegif++;}
		else if (page_type.contains("jpeg")) { myCrawlStat.imagejpeg++;}
		else if (page_type.contains("png"))  { myCrawlStat.imagepng++;}
		else if (page_type.contains("pdf"))  { myCrawlStat.applicationpdf++;}
			
		toappend_visit+= "\""+url+"\""+","+ String.valueOf(page_size)+", "+  page.getParseData().getOutgoingUrls().size()+","+page_type +System.lineSeparator();
	    //System.out.println(toappend_visit);
	     if  ( (toappend_visit.getBytes().length)/1000 >100)
	     {    
	    	 try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"visit_NewsSite.txt", toappend_visit);}  catch(IOException e) {;}
	    	 toappend_visit ="";
	     }
		
		
		if (page.getParseData() instanceof HtmlParseData) {
			myCrawlStat.incTotalLinks(page.getParseData().getOutgoingUrls().size());
			Set<WebURL>  outgoingurls =  page.getParseData().getOutgoingUrls();
			String s="";
			for (WebURL URL : outgoingurls ) {
				if ( checkdomain(URL) ) s = "\""+URL.getURL().toString()+"\""+", "+ "OK\n";
				else                    s = "\""+URL.getURL().toString()+"\""+", "+ "N_OK\n";
				
				toappend_urlnewsite+= s;
			     
			     if  ( (toappend_urlnewsite.getBytes().length)/1000 >100)
			     {    
			    	 try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"urls_NewsSite.txt", toappend_urlnewsite);} catch(IOException e) {;}
			    	 toappend_urlnewsite ="";
			     }
			}
		}
	}
	
	@Override
	protected void onContentFetchError(WebURL webUrl) {
		myCrawlStat.fetch_failed++;
	}
	@Override
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize){
		myCrawlStat.fetch_aborted++;
	}
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		
		     if(statusCode==200) {myCrawlStat.status_200++;}
		else if(statusCode==301) {myCrawlStat.status_301++; myCrawlStat.fetch_aborted++;}
		else if(statusCode==401) {myCrawlStat.status_401++; myCrawlStat.fetch_failed++;}
		else if(statusCode==403) {myCrawlStat.status_403++; myCrawlStat.fetch_failed++;}
		else if(statusCode==404) {myCrawlStat.status_404++; myCrawlStat.fetch_failed++;}
		     
		     if((200<=statusCode && statusCode<300) || statusCode ==308 )
		    	 myCrawlStat.fetch_succeeded++;
		     
		     toappend_fetch+=  "\""+webUrl.getURL()+"\""+", "+ String.valueOf(statusCode) +System.lineSeparator();
		     
		     if  ( (toappend_fetch.getBytes().length)/1000 >100)
		     {    
		    	 try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"fetch_NewsSite.txt", toappend_fetch);} catch(IOException e){;}
		    	 toappend_fetch="";
		     }
	}

	@Override
	public void onBeforeExit(){
		if (toappend_fetch.length()>0)       try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"fetch_NewsSite.txt", toappend_fetch);} catch(IOException e){;}
		if (toappend_urlnewsite.length()>0)  try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"urls_NewsSite.txt", toappend_urlnewsite);} catch(IOException e) {;}
		if (toappend_visit.length()>0)       try{ appendfile.appendfile_with_text( storageFolder.getAbsolutePath() + "/"+String.valueOf(id)+"visit_NewsSite.txt", toappend_visit);}  catch(IOException e) {;}
		System.out.println(storageFolder.getAbsolutePath());
	}
	/**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }
    @Override
    public void onStart() {
    	id          = myId;
    	myCrawlStat = new CrawlStat(id);
    }

}


