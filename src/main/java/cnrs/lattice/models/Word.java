package cnrs.lattice.models;


//@SuppressWarnings("unused")
public class Word {

	private boolean isNewLine = false;
	private String idSrcmf = "_";
	private String idSent = "_";
	private String form = "_";
	private String lemmaGold = "_";
	private String lemmaPredicted = "_";
	private String posGold = "_";
	private String posPredictedWapiti = "_";
	private String posPredictedTreeTagger = "_";
	private String governor = "_";
	private String governorP = "_";
	private String synFunction = "_";
	private String synFunctionP = "_";
	private String morpho = "_";
	private String reg = "_";
	
	private Word(){
		idSrcmf = "";
		idSent = "";
		form = "";
		lemmaGold = "";
		lemmaPredicted = "";
		posGold = "";
		posPredictedWapiti = "";
		posPredictedTreeTagger = "";
		governor = "";
		synFunction = "";
		morpho = "";
		reg = "";
	}
	
	public static Word create() {
		return new Word();
	}
	
	public Word isNewLine(){
		this.isNewLine = true;
		return this;
	}
	
	public void setIdSrcmf(String idSrcmf){
		this.idSrcmf = idSrcmf;
	}
	
	public void setIdSent(String idSent){
		this.idSent = idSent;
	}
	
	public void setForm(String form){
		this.form = form;
	}
	
	public void setLemmaGold(String lemmaGold){
		this.lemmaGold = lemmaGold;
	}
	
	public void setLemmaPredicted(String lemmaPredicted){
		this.lemmaPredicted = lemmaPredicted;
	}
	
	public void setPosGold(String posGold){
		this.posGold = posGold;
	}
	
	public void setPosPredictedWapiti(String posPredictedWapiti){
		this.posPredictedWapiti = posPredictedWapiti;
	}
	
	public void setPosPredictedTreeTagger(String posPredictedTreeTagger){
		this.posPredictedTreeTagger = posPredictedTreeTagger;
	}
	
	public void setGovernor(String governor){
		this.governor = governor;
	}
	
	public void setGovernorP(String governorP){
		this.governorP = governorP;
	}
	
	public void setSynFunction(String synFunction){
		this.synFunction = synFunction;
	}
	
	public void setSynFunctionP(String synFunctionP){
		this.synFunctionP = synFunctionP;
	}
	
	public void setMorpho(String morpho){
		this.morpho = morpho;
	}
	
	public void setReg(String reg){
		this.reg = reg;
	}
	
	//// geters
	
	public boolean getStatus(){
		return this.isNewLine;
	}
	
	public String getIdSrcmf(){
		return this.idSrcmf;
	}
	
	public String getIdSent(){
		return this.idSent;
	}
	
	public String getForm(){
		return this.form;
	}
	
	public String getLemmaGold(){
		return this.lemmaGold;
	}
	
	public String getLemmaPredicted(){
		return this.lemmaPredicted;
	}
	
	public String getPosGold(){
		return this.posGold;
	}
	
	public String getPosPredictedWapiti(){
		return this.posPredictedWapiti;
	}
	
	public String getPosPredictedTreeTagger(){
		return this.posPredictedTreeTagger;
	}
	
	public String getGovernor(){
		return this.governor;
	}
	
	public String getGovernorP(){
		return this.governorP;
	}
	
	public String getSynFunction(){
		return this.synFunction;
	}
	
	public String getSynFunctionP(){
		return this.synFunctionP;
	}
	
	public String getMorpho(){
		return this.morpho;
	}
	
	public String getReg(){
		return this.reg;
	}
}
