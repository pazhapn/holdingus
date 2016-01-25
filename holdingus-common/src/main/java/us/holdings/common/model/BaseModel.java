package us.holdings.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseModel {
	public static final String DOC_ID = "_id";
	
	@JsonProperty(DOC_ID) private String docId;

	public BaseModel(){}
	
	public BaseModel(String id){
		this.docId = id;
	}
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String id) {
		this.docId = id;
	}
}
