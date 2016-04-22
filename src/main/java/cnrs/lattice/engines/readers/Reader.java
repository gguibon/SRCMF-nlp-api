package cnrs.lattice.engines.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cnrs.lattice.models.Word;
import cnrs.lattice.tools.utils.Tools;

public class Reader {
	
	private static int idSrcmf;
	private static int idSent;
	private static int form ;
	private static int lemmaGold;
	private static int lemmaPredicted ;
	private static int posPredictedTreeTagger ;
	private static int posPredictedWapiti;
	private static int posGold ;
	private static int morpho ;
	private static int governor ;
	private static int governorP ;
	private static int synFunction ;
	private static int synFunctionP ;
	private static int reg;
//	private static List<Word> words = new ArrayList<Word>();

	
//	public List<Word> readPath(String path) throws IOException{
//		return read(Tools.readFile(path, StandardCharsets.UTF_8));
//	} 
	
	public List<Word> readConll(String str) throws IOException{
		this.idSrcmf = 0;
		this.idSent = 1;
		this.form = 2;
		this.lemmaPredicted = 3;
		this.posPredictedTreeTagger = 4;
		this.posGold = 5;
		this.morpho = 7;
		this.governor = 9;
		this.governorP = 8;
		this.synFunction = 11;
		this.synFunctionP = 10;
		this.reg = 12; 
		
		return read(str, "conll");
	}
	
	public List<Word> readConllLakme(String str) throws IOException{
		this.idSrcmf = 0;
		this.idSent = 1;
		this.form = 2;
		this.lemmaGold = 3;
		this.lemmaPredicted = 4;
		this.posGold = 5;
		this.posPredictedWapiti = 6;
		this.governor = 9;
		this.governorP = 10;
		this.synFunction = 11;
		this.synFunctionP = 12; 
		
		return read(str, "conlllakme");
	}
	
//	public List<Word> readConllLakme(String str) throws IOException{
//		this.idSrcmf = 0;
//		this.idSent = 1;
//		this.form = 2;
//		this.lemmaGold = 3;
//		this.lemmaPredicted = 4;
//		this.posGold = 5;
//		this.posPredictedWapiti = 6;
//		this.governor = 9;
//		this.governorP = 10;
//		this.synFunction = 11;
//		this.synFunctionP = 12; 
//		
//		return read(str, "conlllakme");
//	}
	
	public List<Word> readMate(String str) throws IOException{
		//FEAT, PFEAT, FILLPRED, PRED, et APREDs sont ignorés (car absents du SRCMF). Changer si besoin.
		//ID FORM LEMMA PLEMMA POS PPOS FEAT PFEAT HEAD PHEAD DEPREL PDEPREL FILLPRED PRED APREDs
		this.idSent = 0;
		this.form = 1;
		this.lemmaGold = 2;
		this.lemmaPredicted = 3;
		this.posPredictedWapiti = 5;
		this.posGold = 4;
		this.governor = 8;
		this.governorP = 9;
		this.synFunction = 10;
		this.synFunctionP = 11; 
		return read(str, "mate");
	}
	
	
	public List<Word> readWapitiResults(List<Word> words, String str) throws IOException{
		//FORM PLEMMATTagger PPOSTTagger _ _ _ REG GPOS PPOS
		this.form = 0;
		this.lemmaPredicted = 1;
		this.posGold = 7;
		this.posPredictedTreeTagger = 2;
		this.reg = 6;
		this.posPredictedWapiti = 8;
		return read(str, "wapitiResults");
	}
	
	public List<Word> readWapiti(String str) throws IOException{
		//FORM PLEMMATTagger PPOSTTagger _ _ _ REG GPOS PPOS
		this.form = 0;
		this.lemmaPredicted = 1;
		this.posGold = 7;
		this.posPredictedTreeTagger = 2;
		this.reg = 6;
		return read(str, "wapiti");
	}
	
	private List<Word> read(String content, String mode) throws IOException{
		List<Word> words = new ArrayList<Word>();
		List<String> lines = Tools.StringToList(content);
		int current = 0;
		int total = lines.size();
		switch(mode){
			case "mate" :
				for(String line : lines){
					current++;
					Tools.printProgress(total, current);
					if(line.length() == 0){
						Word w = Word.create().isNewLine();
						words.add(w);
						continue;
					}
					String[] cols = line.split("\t");
					Word w = Word.create();
					w.setIdSent(cols[idSent]);
					w.setForm(cols[form]);
					w.setLemmaGold(cols[lemmaGold]);
					w.setLemmaPredicted(cols[lemmaPredicted]);
					w.setPosGold(cols[posGold]);
					w.setPosPredictedWapiti(cols[posPredictedWapiti]);
					w.setGovernor(cols[governor]);
					w.setGovernorP(cols[governorP]);
					w.setSynFunction(cols[synFunction]);
					w.setSynFunctionP(cols[synFunctionP]);
					words.add(w);
				}
				break;
			case "wapiti" :
				for(String line : lines){
					current++;
					Tools.printProgress(total, current);
					if(line.length() == 0){
						Word w = Word.create().isNewLine();
						words.add(w);
						continue;
					}
					String[] cols = line.split("\t");
					Word w = Word.create();
					w.setForm(cols[form]);
					w.setLemmaGold(cols[lemmaPredicted]);
					w.setPosPredictedTreeTagger(cols[posPredictedTreeTagger]);
					w.setPosGold(cols[posGold]);
					w.setReg(cols[reg]);
					words.add(w);
				}
				break;		
				
			case "wapitiResults" :
				for(String line : lines){
					current++;
					Tools.printProgress(total, current);
					if(line.length() == 0){
						Word w = Word.create().isNewLine();
						words.add(w);
						continue;
					}
					String[] cols = line.split("\t");
					Word w = Word.create();
					w.setForm(cols[form]);
					w.setLemmaGold(cols[lemmaPredicted]);
					w.setPosPredictedTreeTagger(cols[posPredictedTreeTagger]);
					w.setPosGold(cols[posGold]);
					w.setReg(cols[reg]);
					w.setPosPredictedWapiti(cols[posPredictedWapiti]);
					words.add(w);
				}
				break;	
				
			case "conll" :
				for(String line : lines){
					current++;
					Tools.printProgress(total, current);
					if(line.length() == 0){
						Word w = Word.create().isNewLine();
						words.add(w);
						continue;
					}
					String[] cols = line.split("\t");
					Word w = Word.create();
					w.setIdSrcmf(cols[idSrcmf]);
					w.setIdSent(cols[idSent]);
					w.setForm(cols[form]);
					w.setLemmaPredicted(cols[lemmaPredicted]);
					w.setPosPredictedTreeTagger(cols[posPredictedTreeTagger]);
					w.setPosGold(cols[posGold]);
					w.setMorpho(cols[morpho]);
					w.setGovernor(cols[governor]);
					w.setGovernorP(cols[governorP]);
					w.setSynFunction(cols[synFunction]);
					w.setSynFunctionP(cols[synFunctionP]);
					w.setReg(cols[reg]);
					words.add(w);
				}
				break;
				
			case "conlllakme" :
				for(String line : lines){
					current++;
					Tools.printProgress(total, current);
					if(line.length() == 0){
						Word w = Word.create().isNewLine();
						words.add(w);
						continue;
					}
					String[] cols = line.split("\t");
					Word w = Word.create();
					w.setIdSrcmf(cols[idSrcmf]);
					w.setIdSent(cols[idSent]);
					w.setForm(cols[form]);
					w.setLemmaGold(cols[lemmaGold]);
					w.setLemmaPredicted(cols[lemmaPredicted]);
					w.setPosPredictedWapiti(cols[posPredictedWapiti]);
					w.setPosGold(cols[posGold]);
					w.setGovernor(cols[governor]);
					w.setGovernorP(cols[governorP]);
					w.setSynFunction(cols[synFunction]);
					w.setSynFunctionP(cols[synFunctionP]);
					words.add(w);
				}
				break;
		}
		return words;
	}
	
//	public Reader putValues(List<Word> wordsEntry) throws Exception{
//		int current = 0;
//		int total = wordsEntry.size();
//		for(Word w : wordsEntry){
//			
//			Word word = words.get(current);
//			current++;
//			Tools.printProgress(total, current);
//			if(word.getStatus()!=true)continue;
//			word.setIdSrcmf(override(word.getIdSrcmf(),w.getIdSrcmf()));
//			word.setIdSent(override(word.getIdSent(),w.getIdSent()));
//			word.setForm(override(word.getForm(),w.getForm()));
//			word.setGovernor(override(word.getGovernor(),w.getGovernor()));
//			word.setGovernorP(override(word.getGovernorP(),w.getGovernorP()));
//			word.setLemmaGold(override(word.getLemmaGold(),w.getLemmaGold()));
//			word.setLemmaPredicted(override(word.getLemmaPredicted(),w.getLemmaPredicted()));
//			word.setMorpho(override(word.getMorpho(), w.getMorpho()));
//			word.setPosGold(override(word.getPosGold(), w.getPosGold()));
//			word.setPosPredictedTreeTagger(override(word.getPosPredictedTreeTagger(), w.getPosPredictedTreeTagger()));
//			word.setPosPredictedWapiti(override(word.getPosPredictedWapiti(), w.getPosPredictedWapiti()));
//			word.setReg(override(word.getReg(), w.getReg()));
//			word.setSynFunction(override(word.getSynFunction(), w.getSynFunction()));
//			word.setSynFunctionP(override(word.getSynFunctionP(), w.getSynFunctionP()));
//		}
//
//		return this;
//	}
	
	private String override(String str, String str2Add){
		if(!str2Add.equals("_"))str = str2Add;
		return str;
	}
	
//	public List<Word> getWords(){
//		return words;
//	}
	
}
