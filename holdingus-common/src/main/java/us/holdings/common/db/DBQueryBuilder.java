package us.holdings.common.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DBQueryBuilder {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(DBQueryBuilder.class);
	
	protected abstract MongoCollection getCollection();
	
	protected FindOne getById(String id){
		return getCollection().findOne("{_id: '"+id+"'}");
	}
	protected long count(DBParam...dbParams){
		if(dbParams != null){
			StringBuilder sb = new StringBuilder();
			sb.append(getQueryString(dbParams));
			Object[] paramValues = getParamValues(dbParams);
			if(paramValues.length > 0)
				return getCollection().count(sb.toString(), paramValues);
			else
				return getCollection().count(sb.toString());
		}else{
			return getCollection().count();
		}
	}
	
	public void update(Map<String, Object> fieldsToUpdate, DBParam...queryParams){
		update(fieldsToUpdate, true, queryParams);
	}
	
	public void update(Map<String, Object> fieldsToUpdate, boolean updateMultipleDocs, DBParam...queryParams){
		if(!fieldsToUpdate.isEmpty()){
			String query = getQueryString(queryParams);
			Object[] queryValues = getParamValues(queryParams);
			Object[] updateValues = new Object[fieldsToUpdate.size()];
			String update = getUpdateString(fieldsToUpdate, updateValues);
			//log.debug("{}, {}", query, LogUtil.writeFull(queryParams));
			//log.debug("{}, {}", update, LogUtil.writeFull(fieldsToUpdate));
			if(updateMultipleDocs){
				getCollection().update(query, queryValues).multi().with(update, updateValues);
			}else{
				getCollection().update(query, queryValues).with(update, updateValues);
			}
		}
	}
	
	public void remove(DBParam...dbParams){
		String query = getQueryString(dbParams);
		Object[] queryValues = getParamValues(dbParams);
		if(queryValues.length > 0){
			//if(log.isDebugEnabled()) log.debug("existsCount query {}, params size {}", sb.toString(), paramValues.length);
			getCollection().remove(query, queryValues);
		}else{
			//if(log.isDebugEnabled()) log.debug("existsCount query {}", sb.toString());
			getCollection().remove(query);
		}
	}
	protected Find find(int skip, int limit, List<String> fieldsToRetrieve, 
			Map<String, String> orderByFields, DBParam... jongoOperatorParams) {
		String query = getQueryString(jongoOperatorParams);
		Object[] paramValues = getParamValues(jongoOperatorParams);
		Find find = getCollection().find(query, paramValues);
		/*
		if(log.isDebugEnabled())log.debug("query {}, paramValues {}", 
				DataSerializer.writeValueAsString(query), 
				DataSerializer.writeValueAsString(paramValues));
				*/
		if(fieldsToRetrieve != null && fieldsToRetrieve.size() > 0){
			find.projection(getFields(fieldsToRetrieve));
		}
		if(orderByFields != null && orderByFields.size() > 0){
			//if(log.isDebugEnabled())log.debug("getOrderBy(orderByFields) {}", DataSerializer.writeValueAsString(getOrderBy(orderByFields)));
			find.sort(getOrderBy(orderByFields));
		}
		if(limit > 0){
			return find.skip(skip).limit(limit);
		}else{
			return find;			
		}
	}
	
	protected FindOne findOne(List<String> fieldsToRetrieve, 
			Map<String, String> orderByFields, DBParam... jongoOperatorParams){
		String query = getQueryString(jongoOperatorParams);
		Object[] paramValues = getParamValues(jongoOperatorParams);
		//if(log.isDebugEnabled())log.debug("query {}, {}",query, paramValues);
		FindOne findOne = getCollection().findOne(query, paramValues);
		if(fieldsToRetrieve != null && fieldsToRetrieve.size() > 0){
			findOne.projection(getFields(fieldsToRetrieve));
		}
		return findOne;
	}
	
	private String getOrderBy(Map<String, String> orderByFields){
		int size = orderByFields.size();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("{ _id: 1,");
		for(String key: orderByFields.keySet()){
			sb.append(key).append(": ")
			.append(orderByFields.get(key));
			if(i < (size -1)) sb.append(", ");
			i++;
		}
		sb.append(" }");
		return sb.toString();
	}
	public static String getUpdateString(Map<String, Object> fieldsToUpdate, Object[] updateValues){
		int i = 0, size = fieldsToUpdate.size();
		StringBuilder sb = new StringBuilder();
		sb.append("{$set: {");
		for(String key: fieldsToUpdate.keySet()){
			updateValues[i] = fieldsToUpdate.get(key);
			sb.append(key).append(": ").append("#");
			if(i < (size - 1)) sb.append(", ");
			i++;
		}
		sb.append("}}");
		return sb.toString();
	}
	public static String getQueryString(DBParam... dbParams){
		int size = dbParams.length;
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(DBParam param: dbParams){
			sb.append(param.getName()).append(": ").append(param.getOperator().getQuerySymbol());
			if(i < (size -1)) sb.append(", ");
			i++;
		}
		sb.append("}");
		return sb.toString();
	}
	
	private Object[] getParamValues(DBParam... jongoOperatorParams){
		List<Object> paramValues = new ArrayList<Object>();
		for(DBParam operatorParam: jongoOperatorParams){
			if(operatorParam.getValue() != null){
				paramValues.add(operatorParam.getValue());
			}
		}
		return paramValues.toArray();
	}
	
	private String getFields(List<String> fields){
		int size = fields.size();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		if(fields.size() == 0) sb.append("{ _id: 1, "); else sb.append("{ ");
		for(String field: fields){
			sb.append(field).append(": 1");
			if(i < (size -1)) sb.append(", ");
			i++;			
		}
		sb.append(" }");
		return sb.toString();
	}
}
