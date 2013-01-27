package com.chimpler.example;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.index.CategoryDocumentBuilder;
import org.apache.lucene.facet.index.params.DefaultFacetIndexingParams;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Frederic Dang Ngoc
 */
class FacetLuceneIndexer {
	private static Version LUCENE_VERSION = Version.LUCENE_40;
	public static void main(String args[]) throws Exception {
		if (args.length != 3) {
			System.err.println("Parameters: [index directory] [taxonomy directory] [json file]");
			System.exit(1);
		}
		
		String indexDirectory = args[0];
		String taxonomyDirectory = args[1];
		String jsonFileName = args[2];
		
		IndexWriterConfig writerConfig = new IndexWriterConfig(LUCENE_VERSION, new WhitespaceAnalyzer(LUCENE_VERSION));
		writerConfig.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(indexDirectory)), writerConfig);

		TaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter(new MMapDirectory(new File(taxonomyDirectory)), OpenMode.CREATE);
		CategoryDocumentBuilder categoryDocumentBuilder = new CategoryDocumentBuilder(taxonomyWriter, new DefaultFacetIndexingParams());

		String content = IOUtils.toString(new FileInputStream(jsonFileName));
		JSONArray bookArray = new JSONArray(content);
		
		Field idField = new IntField("id", 0, Store.YES);
		Field titleField = new TextField("title", "", Store.YES);
		Field authorsField = new TextField("authors", "", Store.YES);
		Field bookCategoryField = new TextField("book_category", "", Store.YES);

		for(int i = 0 ; i < bookArray.length() ; i++) {
			Document document = new Document();

			JSONObject book = bookArray.getJSONObject(i);
			int id = book.getInt("id");
			String title = book.getString("title");
			String bookCategory = book.getString("book_category");
			
    		List<CategoryPath> categoryPaths = new ArrayList<CategoryPath>();
			String authorsString = "";
			JSONArray authors = book.getJSONArray("authors");
			for(int j = 0 ; j < authors.length() ; j++) {
				String author = authors.getString(j);
				if (j > 0) {
					authorsString += ", ";
				}
				categoryPaths.add(new CategoryPath("author", author));
				authorsString += author;
			}
			categoryPaths.add(new CategoryPath("book_category" + bookCategory, '/'));
			categoryDocumentBuilder.setCategoryPaths(categoryPaths);
			categoryDocumentBuilder.build(document);
			
			idField.setIntValue(id);
			titleField.setStringValue(title);
			authorsField.setStringValue(authorsString);
			bookCategoryField.setStringValue(bookCategory);
			
			document.add(idField);
			document.add(titleField);
			document.add(authorsField);
			document.add(bookCategoryField);
			
			indexWriter.addDocument(document);
			
			System.out.printf("Book: id=%d, title=%s, book_category=%s, authors=%s\n",
				id, title, bookCategory, authors);
		}
		taxonomyWriter.commit();
		taxonomyWriter.close();
		
		indexWriter.commit();
		indexWriter.close();
	}
}