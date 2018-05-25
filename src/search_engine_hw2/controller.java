package search_engine_hw2;

import java.io.File;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.util.IO;

public class controller {
	public static void main(String[] args) throws Exception {
		
		String [] checkDomainlist=  new String[args.length-1];// = {"https://www.nbcnews.com/","https://media2.s-nbcnews.com/"};
		for (int i=1;i<args.length;i++) { checkDomainlist[i-1] = args[i].substring( 0,args[i].indexOf( "/",9)); }
		String crawlStorageFolder = (new File(".").getCanonicalPath())+ "/data/crawl";
		int crawlpages = java.lang.Integer.parseInt(args[0]);
		
		
		int numberOfCrawlers = 7;
		int total_extracted =0;
		int total_unique =0;
		int total_unique_within=0;
		int total_unique_outside =0;
		
		int total_fetches_attempted =0;
		int total_fetches_succeed =0;
		int total_fetches_aborted =0;
		int tota_fetches_failed =0;
		
		int total_200 =0;
		int total_301 =0;
		int total_401 =0;
		int total_403 =0;
		int total_404 =0;
		
		int file1k =0;
		int file1k_10k =0;
		int file10k_100k =0;
		int file100k_1m =0;
		int filelarge1m=0;
		
		int html =0;
		int gif =0;
		int jpeg =0;
		int png =0;
		int pdf =0;
		CrawlConfig config = new CrawlConfig();
		
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(crawlpages);
		config.setIncludeBinaryContentInCrawling(true);// to crawl images/pdf... binary files
		//config.setMaxDepthOfCrawling(10);
		/*
		* Instantiate the controller for this crawl.
		*/
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		
		/*
		* For each crawl, you need to add some seed urls. These are the first
		* URLs that are fetched and then the crawler starts following links
		* which are found in these pages
		*/		
		for (int i=1; i< args.length;i++) {controller.addSeed(args[i]);}
		 
		 MyCrawler.configure( checkDomainlist, crawlStorageFolder);
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);
		
		List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        
		appendfile.deletefile(crawlStorageFolder+"/"+"fetch_NewsSite.csv");
		appendfile.deletefile(crawlStorageFolder+"/"+"urls_NewsSite.csv");
		appendfile.deletefile(crawlStorageFolder+"/"+"visit_NewsSite.csv");
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData;
            // data type
            gif+= stat.imagegif;
            png+= stat.imagepng;
            jpeg+= stat.imagejpeg;
            pdf+= stat.applicationpdf;
            html+= stat.texthtml;
            // file size
            file1k += stat.file_1kb;
            file1k_10k += stat.file_1kb10kb;
            file10k_100k+= stat.file_10kb100kb;
            file100k_1m+= stat.file_100kb1mb;
            filelarge1m+= stat.file_1mblarger;
            // status code
            total_200 += stat.status_200;
            total_301 += stat.status_301;
            total_401 += stat.status_401;
            total_403 += stat.status_403;
            total_404 += stat.status_404;
            // outgoin url
           
            // fetch stat.
            total_fetches_succeed  += stat.fetch_succeeded;
            total_fetches_aborted  += stat.fetch_aborted;
            tota_fetches_failed    += stat.fetch_failed;
            total_fetches_attempted += (stat.fetch_succeeded+  stat.fetch_aborted + stat.fetch_failed);
            
            // outgoing url
            total_extracted      += stat.getTotalLinks();
            total_unique_within  += stat.total_unique_domain_url;
            total_unique_outside += stat.total_unique_outdomain_url;
            total_unique         += (stat.total_unique_domain_url+ stat.total_unique_outdomain_url);
            
            appendfile.appendfile(crawlStorageFolder+"/"+"fetch_NewsSite.csv", crawlStorageFolder+"/"+String.valueOf(stat.id)+"fetch_NewsSite.txt");
            appendfile.appendfile(crawlStorageFolder+"/"+"urls_NewsSite.csv" , crawlStorageFolder+"/"+String.valueOf(stat.id)+"urls_NewsSite.txt");
            appendfile.appendfile(crawlStorageFolder+"/"+"visit_NewsSite.csv", crawlStorageFolder+"/"+String.valueOf(stat.id)+"visit_NewsSite.txt");
        }
        
        System.out.println("Fetch Statistics");
        System.out.println("================");
        System.out.println("# fetches attempted:"+total_fetches_attempted);
        System.out.println("# fetches succeeded:"+total_fetches_succeed);
        System.out.println("# fetches aborted:"+total_fetches_aborted);
        System.out.println("# fetches failed:"+tota_fetches_failed);
        System.out.print("\n\n");
        
        System.out.println("Outgoing URLS:");
        System.out.println("==============");
        System.out.println("Total URLs extracted:"+ total_extracted );
        System.out.println("# unique URLS extracted:"+total_unique);
        System.out.println("# unique URLS within News Site:"+ total_unique_within);
        System.out.println("# unique URLS outside New Site:" +total_unique_outside);
        System.out.print("\n\n");
        
        System.out.println("Status Codes");
        System.out.println("============");
        System.out.println("200 OK:"+total_200);
        System.out.println("301 Moved Permanently:"+total_301);
        System.out.println("401 Unauthorized:"+total_401);
        System.out.println("403 Forbidden:"+total_403);
        System.out.println("404 Not Found:"+total_404);
        System.out.print("\n\n");
        
        System.out.println("File Sizes");
        System.out.println("===========");
        System.out.println("< 1KB:"+file1k);
        System.out.println("1KB ~ < 10KB:"+file1k_10k);
        System.out.println("10KB ~ <100KB:"+file10k_100k);
        System.out.println("100KB ~ < 1MB:"+file100k_1m);
        System.out.println(">= 1MB:"+filelarge1m);
        
        System.out.println("Conten Types");
        System.out.println("==============");
        System.out.println("text/html:"+html);
        System.out.println("image/gif:"+gif);
        System.out.println("image/jpeg:"+jpeg);
        System.out.println("image/png:"+png);
        System.out.println("application/pdf:"+pdf);
        
        
        
	}
}
