package us.holdings.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.QueryModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mongodb.DBCursor;

import us.holdings.common.db.DBOperator;
import us.holdings.common.db.DBParam;
import us.holdings.common.db.DBQueryBuilder;
import us.holdings.common.db.DBStore;
import us.holdings.common.model.BaseModel;

public class MongoDAO<T extends BaseModel> extends DBQueryBuilder {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final Class<T> claz;	
	private final MongoCollection collection;	
	private final DBStore dbStore;
	
	public MongoDAO(Class<T> claz, DBStore dbStore) {
		this(claz, dbStore, claz.getName());
	}
	
	public MongoDAO(Class<T> claz, DBStore dbStore, String collectionName) {
		super();
		this.claz = claz;
		this.dbStore = dbStore;
		this.collection = dbStore.getCollection(collectionName);
	}

	@Override
	protected MongoCollection getCollection() {
		return collection;
	}
	
	public Class<T> getClaz() {
		return claz;
	}
	
	public void runCommand(String command){
		dbStore.runCommand(command);
	}
	public MongoCursor<T> getCursor(final int batchSize){
		return getCollection().find().sort("{_id: 1}").with(new QueryModifier() {			
			@Override
			public void modify(DBCursor cursor) {
				// TODO Auto-generated method stub
		         cursor.batchSize(batchSize);
		     }
		}).as(claz);
	}
	public MongoCursor<T> getCursorByQuery(final int batchSize, String query, String sort){
		return getCollection().find(query).sort(sort).with(new QueryModifier() {			
				@Override
				public void modify(DBCursor cursor) {
					// TODO Auto-generated method stub
			         cursor.batchSize(batchSize);
			     }
			}).as(claz);
	}
	public void save(T model){
		getCollection().save(model);
		//this.dbModelListener.indexModel(model);
	}
	
	public void save(List<T> models){
		for(T model: models){
			save(model);
		}
		//this.dbModelListener.indexModel(model);
	}
	/*
	public void insert(List<T> models){
		for(T model: models){
			setModelId(model);
		}
		getCollection().insert(models.toArray());
		//this.dbModelListener.indexModel(model);
	}
	*/
	public void insert(T model){
		getCollection().insert(model);
		//this.dbModelListener.indexModel(model);
	}
	public void delete(T model) {
		remove(new DBParam("_id", DBOperator.EQUALS, model.getDocId()));
		//this.dbModelListener.deleteModel(model);
	}
	
	public void delete(String id) {
		remove(new DBParam("_id", DBOperator.EQUALS, id));
		//this.dbModelListener.deleteModel(model);
	}
	
	public long countById(T model) {
		return count(new DBParam("_id", DBOperator.EQUALS, model.getDocId()));
	}
	
	public long count(DBParam... jongoOperatorParams){
		return count(jongoOperatorParams);
	}
	
	public long countAll(){
		return count();
	}
	
	public T get(String id){
		return getById(id).as(claz);
	}
	
	public T get(String id, List<String> fieldsToRetrieve){
		return findOne(fieldsToRetrieve, null,
				new DBParam("_id", DBOperator.EQUALS, id))
				.as(claz);
	}
	
	public List<T> multiGet(List<String> ids){
		return Lists.newArrayList(find(0, 0, null, null, new DBParam("_id", DBOperator.IN, ids)).as(claz).iterator());
	}
	
	public List<T> multiGet(String field, List<String> fieldValues, List<String> fieldsToRetrieve){
		return Lists.newArrayList(find(0, 0, fieldsToRetrieve, null, new DBParam(field, DBOperator.IN, fieldValues)).as(claz).iterator());
	}
	
	public T get(String field, String value){
		return findOne(null, null, 
				new DBParam(field, DBOperator.EQUALS, value))
				.as(claz);
	}

	public List<T> list(DBParam... jongoOperatorParams){
		return Lists.newArrayList(find(0, 0, null, null, jongoOperatorParams).as(claz).iterator());
	}

	public List<T> list(int skip, int limit, DBParam... jongoOperatorParams){
		return Lists.newArrayList(find(skip, limit, null, null, jongoOperatorParams).as(claz).iterator());
	}
	
	public List<T> listByPage(int pageNumStartingWithOne, int limit, DBParam... jongoOperatorParams){
		return Lists.newArrayList(find((pageNumStartingWithOne -1) * limit, limit, null, null, jongoOperatorParams).as(claz).iterator());
	}
	
	public List<T> listRemaining(int pageNumStartingWithOne, int limit){
		Map<String, String> orderByFields = new HashMap<>();
        orderByFields.put("_id", "1");
        DBParam jongoOperatorParam = new DBParam("_id", DBOperator.EXISTS, true);
		return Lists.newArrayList(find((pageNumStartingWithOne -1) * limit, limit, null, orderByFields, jongoOperatorParam).as(claz).iterator());
	}
	
	public List<T> listOrderBy(int skip, int limit, Map<String, String> orderByFields, DBParam... jongoOperatorParams){
		return Lists.newArrayList(find(skip, limit, null, orderByFields, jongoOperatorParams).as(claz).iterator());
	}

	public void increment(String id, String fieldToIncrement, int incValue){
		getCollection().update("{_id: '"+id+"'}").with("{$inc: {"+fieldToIncrement+": #}}", incValue);
	}

	public void update(String queryField, List<String> ids, String fieldToUpdate, String updateValue){
		getCollection().update("{"+queryField+": {$in:#}}", ids).multi().with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(List<String> ids, String fieldToUpdate, String updateValue){
		getCollection().update("{_id: {$in:#}}", ids).multi().with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(List<String> ids, String fieldToUpdate, int updateValue){
		getCollection().update("{_id: {$in:#}}", ids).multi().with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(String id, String fieldToUpdate, Map<String, String> updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(String id, String fieldToUpdate, boolean updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(String id, String fieldToUpdate, long updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}

	public void update(String id, String fieldToUpdate, int updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}
/*
	public WriteConcern update(String id, String fieldToUpdate, String updateValue){
		return getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue).getLastConcern();
		//getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": '"+updateValue+"'}}");
	}
*/
	public void update(String id, String fieldToUpdate, Date updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$set: {"+fieldToUpdate+": #}}", updateValue);
	}
	public void addToSet(String id, String fieldToUpdate, Date updateValue){
		getCollection().update("{_id: '"+id+"'}").with("{$addToSet: {"+fieldToUpdate+": #}}", updateValue);
	}
}
