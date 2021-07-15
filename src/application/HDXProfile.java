package application;

public class HDXProfile {
	
	public HDXProfile() {}
	
	private String id;
	
	private String mz;
	
	private String charge;
	
	private String peptide;
	
	private String protein;
	
	private String posFrom;
	
	private String posTo;
	
	private String expMz;
	
	private String mzShift;
	
	private String startScan;
	
	private String endScan;
	
	private String apexScan;
	
	private String apexRt;
	
	private String[] conditions;

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
	
	public String getPosFrom() {
		return this.posFrom;
	}
	
	public String getPosTo() {
		return this.posTo;
	}
	
	public String getExpMz() {
		return this.expMz;
	}
	
	public String getMzShift() {
		return this.mzShift;
	}
	
	public String getStartScan() {
		return this.startScan;
	}
	
	public String getEndScan() {
		return this.endScan;
	}
	
	public String getApexScan() {
		return this.apexScan;
	}
	
	public String getApexRt() {
		return this.apexRt;
	}
	
	public String getCondition(int idx) {
		if(conditions.length > idx)
			return this.conditions[idx];
		else
			return null;
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
	
	public void setPosFrom(String posFrom) {
		this.posFrom = posFrom;
	}
	
	public void setPosTo(String posTo) {
		this.posTo = posTo;
	}
	
	public void setExpMz(String expMz) {
		this.expMz = expMz;
	}
	
	public void setMzShift(String mzShift) {
		this.mzShift = mzShift;
	}
	
	public void setStartScan(String startScan) {
		this.startScan = startScan;
	}
	
	public void setEndScan(String endScan) {
		this.endScan = endScan;
	}
	
	public void setApexScan(String apexScan) {
		this.apexScan = apexScan;
	}
	
	public void setApexRt(String apexRt) {
		this.apexRt = apexRt;
	}
	
	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}
	
	public String toString() {
		return "" + id + mz + charge + peptide + protein;
	}
}
