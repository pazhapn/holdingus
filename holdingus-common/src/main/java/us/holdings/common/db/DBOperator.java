package us.holdings.common.db;

public enum DBOperator {
	EQUALS("=", " #" ), NOT_EQUALS("!=", "{ $ne : #}"), 
	EXISTS("exists", "{$exists : #}"), 
	IN("in", "{$in : #}"), GTE(">=", "{$gte :#}"), NIN("not in", "{$nin :#}");
	
	private final String display;
	private final String querySymbol;
	
	DBOperator(String userDisplay, String querySymbol) {
		this.display = userDisplay;
		this.querySymbol = querySymbol;
	}

	public String getDisplay() {
		return display;
	}

	public String getQuerySymbol() {
		return querySymbol;
	}
	
	
}
