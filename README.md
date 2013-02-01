For more information on this project:
https://chimpler.wordpress.com/2013/01/30/faceted-search-with-lucene/

To compile the project:
<pre>
	mvn clean compile assembly:single
</pre>

To run the indexer:
<pre>
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneIndexer index taxonomy books.json
</pre>

To run the searcher:
<pre>
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneSearcher 
index taxonomy story
</pre>

To run the advanced searcher:
<pre>
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneAdvancedSearcher index taxonomy book book_category novel
	java -cp target/facet-lucene-example-1.0-jar-with-dependencies.jar com.chimpler.example.FacetLuceneAdvancedSearcher index taxonomy book book_category novel/comedy
 </pre>
