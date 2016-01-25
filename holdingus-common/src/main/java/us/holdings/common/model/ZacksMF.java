package us.holdings.common.model;

public class ZacksMF extends BaseModel{
	
	public static String MF_SYMBOL = "mfSymbol";
	public static String SYMBOL = "symbol";
	public static String AS_OF_DATE = "asOfDate";
	public static String SHARES = "shares";
	public static String MARKET_VALUE = "marketValue";
	public static String WEIGHT = "weight";
	public static String COMPANY_NAME = "companyName";
	public static String ISSUE = "issue";
	
	private String mfSymbol;
	private String symbol;
	private int asOfDate;
	private long shares;
	private long marketValue;
	private int weight;
	private String companyName;
	private String issue;
	
	public ZacksMF(){}
	
	public ZacksMF(String symbol, String mfSymbol, int asOfDate){
		super(symbol+"-"+mfSymbol+"-"+asOfDate);
		this.symbol = symbol;
		this.mfSymbol = mfSymbol;
		this.asOfDate = asOfDate;
	}
	
	public String getMfSymbol() {
		return mfSymbol;
	}

	public void setMfSymbol(String mfSymbol) {
		this.mfSymbol = mfSymbol;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}

	public int getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(int asOfDate) {
		this.asOfDate = asOfDate;
	}

	public long getShares() {
		return shares;
	}

	public void setShares(long shares) {
		this.shares = shares;
	}

	public long getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(long marketValue) {
		this.marketValue = marketValue;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
