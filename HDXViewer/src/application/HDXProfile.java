package application;

public class HDXProfile {
	
	public HDXProfile() {}
	
	private int id;
	
	private double mz;
	
	private int charge;
	
	private String peptide;
	
	private String protein;
	
	private int posFrom;
	
	private int posTo;
	
	private double expMz;
	
	private double mzShift;
	
	private int startScan;
	
	private int endScan;
	
	private double apexScan;
	
	private double apexRt;
	
	private String second30;
	
	private String minute10;
	
	private String minute60;

	public int getId() {
		return this.id;
	}
	
	public double getMz() {
		return this.mz;
	}
	
	public int getCharge() {
		return this.charge;
	}
	
	public String getPeptide() {
		return this.peptide;
	}
	
	public String getProtein() {
		return this.protein;
	}
	
	public int getPosFrom() {
		return this.posFrom;
	}
	
	public int getPosTo() {
		return this.posTo;
	}
	
	public double getExpMz() {
		return this.expMz;
	}
	
	public double getMzShift() {
		return this.mzShift;
	}
	
	public int getStartScan() {
		return this.startScan;
	}
	
	public int getEndScan() {
		return this.endScan;
	}
	
	public double getApexScan() {
		return this.apexScan;
	}
	
	public double getApexRt() {
		return this.apexRt;
	}
	
	public String getSecond30() {
		return this.second30;
	}
	
	public String getMinute10() {
		return this.minute10;
	}
	
	public String getMinute60() {
		return this.minute60;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setMz(double mz) {
		this.mz = mz;
	}
	
	public void setCharge(int charge) {
		this.charge = charge;
	}
	
	public void setPeptide(String peptide) {
		this.peptide = peptide;
	}
	
	public void setProtein(String protein) {
		this.protein = protein;
	}
	
	public void setPosFrom(int posFrom) {
		this.posFrom = posFrom;
	}
	
	public void setPosTo(int posTo) {
		this.posTo = posTo;
	}
	
	public void setExpMz(double expMz) {
		this.expMz = expMz;
	}
	
	public void setMzShift(double mzShift) {
		this.mzShift = mzShift;
	}
	
	public void setStartScan(int startScan) {
		this.startScan = startScan;
	}
	
	public void setEndScan(int endScan) {
		this.endScan = endScan;
	}
	
	public void setApexScan(double apexScan) {
		this.apexScan = apexScan;
	}
	
	public void setApexRt(double apexRt) {
		this.apexRt = apexRt;
	}
	
	public void setSecond30(String second30) {
		this.second30 = second30;
	}
	
	public void setMinute10(String minute10) {
		this.minute10 = minute10;
	}
	
	public void setMinute60(String minute60) {
		this.minute60 = minute60;
	}
	
	public String toString() {
		return "" + id + mz + charge + peptide + protein;
	}
}
