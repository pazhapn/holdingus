package us.holdings.backend.service;

import us.holdings.common.dao.MongoDAO;
import us.holdings.common.model.ZacksMF;
import us.holdings.common.service.BaseService;

public class ZacksMFService extends BaseService<ZacksMF, MongoDAO<ZacksMF>>{

	public ZacksMFService(MongoDAO<ZacksMF> dao) {
		super(dao);
	}

}
