package com.uvsq.Lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

public class PersonalizedAnalyser extends StopwordAnalyzerBase {

	private int maxTokenLength = 255;
	private static Version VERSION = Version.LUCENE_4_10_1;
	  public PersonalizedAnalyser() {
	    super(VERSION, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	  }
	  
	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		final StandardTokenizer src = new StandardTokenizer(VERSION, reader);
	    src.setMaxTokenLength(maxTokenLength);
	    TokenStream tok = new StandardFilter(VERSION, src);
	    tok = new LowerCaseFilter(VERSION, tok);
	    tok = new StopFilter(VERSION, tok, stopwords);
	    return new TokenStreamComponents(src, tok) {
	      @Override
	      protected void setReader(final Reader reader) throws IOException {
	        src.setMaxTokenLength(PersonalizedAnalyser.this.maxTokenLength);
	        super.setReader(reader);
	      }
	    };
	    }
	
	@Override
	  protected Reader initReader(String fieldName, Reader reader) {
	    NormalizeCharMap.Builder builder = new NormalizeCharMap.Builder();
	    builder.add(".", " ");
	    builder.add("_", " ");
	    builder.add("-", " ");
	    NormalizeCharMap normMap = builder.build();
	    return new MappingCharFilter(normMap, reader);
	  }
	
}
