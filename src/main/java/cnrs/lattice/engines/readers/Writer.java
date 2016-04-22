package cnrs.lattice.engines.readers;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Joiner;

import cnrs.lattice.models.Word;
import cnrs.lattice.tools.utils.Tools;

public class Writer {
	
	public Writer(){
		
	}
	
	public String toMateGoldPOS(List<Word> words){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			String[] values = ArrayUtils.toArray(word.getIdSent(),
					word.getForm(),
					empty,
					word.getLemmaPredicted(),
					empty,
					word.getPosGold(),
					empty,
					empty,
					word.getGovernor(),
					empty,
					word.getSynFunction(),
					empty,
					empty);
			sb.append(joiner.join(values) + "\n");
		}
		return sb.toString();
	}
	
	public String toMateWapitiPPOS(List<Word> words){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			String[] values = ArrayUtils.toArray(word.getIdSent(),
					word.getForm(),
					empty,
					word.getLemmaPredicted(),
					word.getPosGold(),
					word.getPosPredictedWapiti(),
					empty,
					empty,
					word.getGovernor(),
					empty,
					word.getSynFunction(),
					empty,
					empty);
			sb.append(joiner.join(values) + "\n");
		}
		return sb.toString();
	}
	
	public String toWapitiGold(List<Word> words){
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		int current = 0;
		int total = words.size();
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			String[] values = ArrayUtils.toArray(
					word.getForm(),
					word.getLemmaPredicted(),
					word.getPosPredictedTreeTagger(),
					empty,
					empty,
					empty,
					word.getReg(),
					word.getPosGold());
			sb.append(joiner.join(values) + "\n");
		}
		return sb.toString();
	}
	
	public String toMaltGold(List<Word> words){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			String[] values = ArrayUtils.toArray(
					word.getIdSent(),
					word.getForm(),
					word.getLemmaPredicted(),
					empty,//si c'est pour utiliser malt et non juste malteval, autant produire le CPOSTAG et le mettre ici
					word.getPosGold(),
					empty,
					word.getGovernor(),
					word.getSynFunction(),
					empty,
					empty);
			sb.append(joiner.join(values) + "\n");
		}
		return sb.toString();
	}
	
	public String toMaltComplete(List<Word> words, boolean comparePOS){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			//compare lemmas. will take the predicted one per default
			String lemma = "";
			if(word.getLemmaPredicted().isEmpty()){
				lemma = word.getLemmaGold();
			}else{
				lemma = word.getLemmaPredicted();
			}
			//si on veut tricher pour pouvoir comparer les POS
			if(comparePOS){
				
				String[] values = ArrayUtils.toArray(
						word.getIdSent(),
						word.getForm(),
						lemma,
						empty,//si c'est pour utiliser malt et non juste malteval, autant produire le CPOSTAG et le mettre ici
						word.getPosGold(),
						word.getPosPredictedWapiti(),	//empty car FEATS  word.getPosPredictedWapiti(),
						word.getGovernor(),
						word.getSynFunction(),
						word.getGovernorP(),
						word.getSynFunctionP());
				sb.append(joiner.join(values) + "\n");
			}else{//si on veut utiliser normalement
				String[] values = ArrayUtils.toArray(
						word.getIdSent(),
						word.getForm(),
						word.getLemmaPredicted(),
						empty,//si c'est pour utiliser malt et non juste malteval, autant produire le CPOSTAG et le mettre ici
						word.getPosGold(),
						empty,	//empty car FEATS  word.getPosPredictedWapiti(),
						word.getGovernor(),
						word.getSynFunction(),
						word.getGovernorP(),
						word.getSynFunctionP());
				sb.append(joiner.join(values) + "\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * comvert to malt eval format with predicted values
	 * creation of an predicted file format so.
	 * @param words
	 * @param comparePOS
	 * @return
	 */
	public String toMaltEvalP(List<Word> words, boolean comparePOS){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	 
			//compare lemmas. will take the predicted one per default
			String lemma = "";
			if(word.getLemmaPredicted().isEmpty()){
				lemma = word.getLemmaGold();
			}else{
				lemma = word.getLemmaPredicted();
			}
			//si on veut tricher pour pouvoir comparer les POS
			if(comparePOS){
				
				String[] values = ArrayUtils.toArray(
						word.getIdSent(),
						word.getForm(),
						lemma,
						empty,//si c'est pour utiliser malt et non juste malteval, autant produire le CPOSTAG et le mettre ici
						word.getPosGold(),
						word.getPosPredictedWapiti(),	//empty car FEATS  word.getPosPredictedWapiti(),
						word.getGovernorP(),
						word.getSynFunctionP(),
						word.getGovernorP(),
						word.getSynFunctionP());
				sb.append(joiner.join(values) + "\n");
			}else{//si on veut utiliser normalement
				String[] values = ArrayUtils.toArray(
						word.getIdSent(),
						word.getForm(),
						word.getLemmaPredicted(),
						empty,//si c'est pour utiliser malt et non juste malteval, autant produire le CPOSTAG et le mettre ici
						word.getPosGold(),
						empty,	//empty car FEATS  word.getPosPredictedWapiti(),
						word.getGovernorP(),
						word.getSynFunctionP(),
						word.getGovernorP(),
						word.getSynFunctionP());
				sb.append(joiner.join(values) + "\n");
			}
		}
		return sb.toString();
	}
	
	public String toConll(List<Word> words){
		int current = 0;
		int total = words.size();
		String empty = "_";
		Joiner joiner = Joiner.on("\t");
		StringBuilder sb = new StringBuilder();
		for(Word word : words){
			current++;
			Tools.printProgress(total, current);
			if(word.getStatus()){
				sb.append("\n"); 
				continue;
			}	
			String[] values = ArrayUtils.toArray(
					word.getIdSrcmf(),
					word.getIdSent(),
					word.getForm(),
					word.getLemmaPredicted(),
					word.getPosPredictedTreeTagger(),
					word.getPosGold(),
					empty,
					word.getMorpho(),
					empty,
					word.getGovernor(),
					word.getGovernorP(),
					word.getSynFunction(),
					word.getSynFunctionP(),
					empty,
					empty
					)
					;
			sb.append(joiner.join(values) + "\n");
		}
		return sb.toString();
	}
}
