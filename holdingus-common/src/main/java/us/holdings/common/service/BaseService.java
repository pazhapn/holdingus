package us.holdings.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.holdings.common.dao.MongoDAO;
import us.holdings.common.model.BaseModel;

public class BaseService<T extends BaseModel, DAO extends MongoDAO<T>> {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected DAO dao;
	
	public BaseService(DAO dao) {
		this.dao = dao;
	}
	
	public void save(T model){
		dao.save(model);
	}
}
