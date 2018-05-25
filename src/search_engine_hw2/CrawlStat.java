package search_engine_hw2;

public class CrawlStat {
	
    private int totalProcessedPages;
    private long totalLinks;
    public  int id;
    public  int total_unique_outdomain_url;
    public  int total_unique_domain_url;
    public  int status_200;
    public  int status_301;
    public  int status_401;
    public  int status_403;
    public  int status_404;
    public  int file_1kb;
    public  int file_1kb10kb;
    public  int file_10kb100kb;
    public  int file_100kb1mb;
    public  int file_1mblarger;
    public  int texthtml;
    public  int imagegif;
    public  int imagejpeg;
    public  int imagepng;
    public  int applicationpdf;
    public  int fetch_aborted;
    public  int fetch_succeeded;
    public  int fetch_failed;
    public CrawlStat(int id) {
    	this.fetch_failed=this.fetch_succeeded= this.fetch_aborted=this.totalProcessedPages= this.total_unique_outdomain_url = this.total_unique_domain_url = this.status_200 =
    			this.status_301 = this.status_401 = this.status_403 = this.status_404 = this.file_100kb1mb = this.file_10kb100kb
    			= this.file_1kb = this.file_1kb10kb = this.texthtml = this.imagegif = this.imagejpeg = this.imagepng =0;
    	this.id = id;
    	
    }
    public int getTotalProcessedPages() {
        return totalProcessedPages;
    }

    public void setTotalProcessedPages(int totalProcessedPages) {
        this.totalProcessedPages = totalProcessedPages;
    }

    public void incProcessedPages() {
        this.totalProcessedPages++;
    }

    public long getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(long totalLinks) {
        this.totalLinks = totalLinks;
    }

 

    public void incTotalLinks(int count) {
        this.totalLinks += count;
    }

    
}