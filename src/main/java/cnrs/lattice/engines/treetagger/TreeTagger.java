package cnrs.lattice.engines.treetagger;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class TreeTagger {

	private static String TTLEMME = "";
	private static String TTPOS = "";
	private static String TREETAGGER_HOME = "/home/gael/Documents/EN_Term/TreeTagger";
	
	/**
	 * Apply TreeTagger in order to predict for each word, its lemma and part of speech.
	 * 
	 * @param map
	 * @return a HashMap<String, String[]> with id as key, and lemma in tab[0] and pos in tab[1]
	 * @throws IOException
	 * @throws TreeTaggerException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String, String[]> lemmatiserTT(HashMap<String, String> map) throws IOException, TreeTaggerException{
	System.setProperty("treetagger.home",
				TREETAGGER_HOME);
	TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
	 try {
	     tt.setModel(TREETAGGER_HOME + "/models/stein-oldfrench.par:iso8859-1");
	     tt.setHandler(new TokenHandler<String>() {
	         public void token(String token, String pos, String lemma) {
	        	 TTLEMME = lemma;
	        	 TTPOS = pos;
	         }
	     });
	     Iterator it = map.entrySet().iterator();
	     HashMap<String, String[]> mapLemmas = new HashMap<String, String[]>();
	     while (it.hasNext()) {
	    	 Map.Entry pair = (Map.Entry)it.next();
	    	 tt.process(asList( pair.getValue() ));
	    	 String[] arrayLemmaPos = {TTLEMME, TTPOS};
	    	 mapLemmas.put(pair.getKey().toString(), arrayLemmaPos);
	    	 TTLEMME = "";
	    	 TTPOS = "";
	    	 it.remove();
	     }
	     //pour vérifier la hashmap
//	     Iterator it2 = mapLemmas.entrySet().iterator();
//	     while (it2.hasNext()){
//	    	 Map.Entry entry = (Map.Entry)it2.next();
//	    	 String[] values = (String[]) entry.getValue();
//	    	 System.out.printf("%s\t%s\t%s\n", entry.getKey(), values[0], values[1]);
//	     }
	     return mapLemmas;
	 }
	 finally {
	     tt.destroy();
	 }
	}

}
