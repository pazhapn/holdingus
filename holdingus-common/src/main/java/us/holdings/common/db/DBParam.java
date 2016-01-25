package us.holdings.common.db;


public class DBParam {
	 
	private final String paramName;
	private final Object paramValue;
	private final DBOperator operator;
	
	public DBParam(String paramName, DBOperator operator, Object paramValue){
		this.paramName = paramName;
		this.operator = operator;
		this.paramValue = paramValue;
	}

	public String getName() {
		return paramName;
	}

	public Object getValue() {
		return paramValue;
	}
	
	public DBOperator getOperator() {
		return operator;
	}
}
