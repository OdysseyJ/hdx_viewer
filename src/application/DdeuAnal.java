package application;

public class DdeuAnal {
	public DdeuAnal() {}
	
	private String id;
	
	private String mz;
	
	private String charge;
	
	private String peptide;
	
	private String D2OLabelfirst;
	
	private String FirstDdeuNum;
	
	private String FirstDdeuPercent;
	
	private String SecondDdeuNum;
	
	private String SecondDdeuPercent;
	
	private String PredictedDdeu;
	
	private String StartScan;
	
	private String EndScan;
	
	private String StartRT;
	
	private String EndRT;
	
	private String ObservedDdeu;
	
	private String MatchedScore;
	
	public String getId() {
		return this.id;
	}
	
	public String getMz() {
		return this.mz;
	}
	
	public String getCharge() {
		return this.charge;
	}
	
	public String getPeptide() {
		return this.peptide;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setMz(String mz) {
		this.mz = mz;
	}
	
	public void setCharge(String charge) {
		this.charge = charge;
	}
	
	public void setPeptide(String peptide) {
		this.peptide = peptide;
	}
	
	public String toString() {
		return "" + id + mz + charge + peptide;
	}

	public String getD2OLabelfirst() {
		return D2OLabelfirst;
	}

	public void setD2OLabelfirst(String d2oLabelfirst) {
		D2OLabelfirst = d2oLabelfirst;
	}

	public String getFirstDdeuNum() {
		return FirstDdeuNum;
	}

	public void setFirstDdeuNum(String firstDdeuNum) {
		FirstDdeuNum = firstDdeuNum;
	}

	public String getFirstDdeuPercent() {
		return FirstDdeuPercent;
	}

	public void setFirstDdeuPercent(String firstDdeuPercent) {
		FirstDdeuPercent = firstDdeuPercent;
	}

	public String getSecondDdeuNum() {
		return SecondDdeuNum;
	}

	public void setSecondDdeuNum(String secondDdeuNum) {
		SecondDdeuNum = secondDdeuNum;
	}

	public String getSecondDdeuPercent() {
		return SecondDdeuPercent;
	}

	public void setSecondDdeuPercent(String secondDdeuPercent) {
		SecondDdeuPercent = secondDdeuPercent;
	}

	public String getPredictedDdeu() {
		return PredictedDdeu;
	}

	public void setPredictedDdeu(String predictedDdeu) {
		PredictedDdeu = predictedDdeu;
	}

	public String getStartScan() {
		return StartScan;
	}

	public void setStartScan(String startScan) {
		StartScan = startScan;
	}

	public String getEndScan() {
		return EndScan;
	}

	public void setEndScan(String endScan) {
		EndScan = endScan;
	}

	public String getStartRT() {
		return StartRT;
	}

	public void setStartRT(String startRT) {
		StartRT = startRT;
	}

	public String getEndRT() {
		return EndRT;
	}

	public void setEndRT(String endRT) {
		EndRT = endRT;
	}

	public String getObservedDdeu() {
		return ObservedDdeu;
	}

	public void setObservedDdeu(String observedDdeu) {
		ObservedDdeu = observedDdeu;
	}

	public String getMatchedScore() {
		return MatchedScore;
	}

	public void setMatchedScore(String matchedScore) {
		MatchedScore = matchedScore;
	}
}
