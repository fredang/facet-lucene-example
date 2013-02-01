For more information on this project:
https://chimpler.wordpress.com/2013/01/30/faceted-search-with-lucene/

To compile the project:
	mvn clean compile assembly:single

To run the indexer:
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneIndexer index taxonomy books.json

To run the searcher:
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneSearcher 
index taxonomy story

To run the advanced searcher:
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneAdvancedSearcher index taxonomy book book_category novel
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneAdvancedSearcher index taxonomy book book_category novel/comedy
 