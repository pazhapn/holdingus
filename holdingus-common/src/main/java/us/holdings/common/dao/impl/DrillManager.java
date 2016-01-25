package us.holdings.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DrillManager extends JdbcDaoSupport{

	public DrillManager() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Map<String, Object>> list(String sql){		 
		return getJdbcTemplate().queryForList(sql);
	}

}
