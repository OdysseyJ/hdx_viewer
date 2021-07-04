package application;

public class DdeuAnal {
	public DdeuAnal() {}
	
	private String id;
	
	private String mz;
	
	private String charge;
	
	private String peptide;
	
	private String protein;
	
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
	
	public String getProtein() {
		return this.protein;
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
	
	public void setProtein(String protein) {
		this.protein = protein;
	}
	
	public String toString() {
		return "" + id + mz + charge + peptide + protein;
	}
}
