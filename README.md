
This assignment is based on crawler4j, https://github.com/yasserg/crawler4j. Please also include the crawler4j-4.1-jar-with-dependencies.jar in the library while building the project.

From the given seed pages, it can report 
1. The extracted pages within the domain of seed pages or not in data/crawl/urls.Newssite.csv.
2. The visited pages and their size, number of outlinks in data/crawl/visit_NewsSite.csv.
3. The attemp fetch pages and their corresponding status code in data/crawl/fetch_NewsSite.csv. 
4. statistics about the searching results, including the distribution of sizes of retrieval documents, the response distribution of each fetch, the number of successful/failure fetches, and the type of the documents.

In search_engine_hw2 folder, use the comand to compile 

javac src/search_engine_hw2/*.java

Change to the folder With class files, use command line to run

java controller [# search pages] [seed pages whose staring with http:// or https://]

like java controller 100 https://www.nbcnews.com/





