package com.ilab.origin.common.mongo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MongoQueryManager {

private static Log log = LogFactory.getLog(MongoQueryManager.class.getName());
	
	@Autowired
	private MongoOperations operations;
	
	public List<?> executeQuery(Map<String, String> queryMap, Class<?> className){
		Query query = createQuery(queryMap);
		return operations.find(query, className);
	}
	
	public List<?> executeQuery(Map<String, String> queryMap, Class<?> className, String sortField){
		Query query = createQuery(queryMap);
		addSorting(sortField, query);
		return executeQuery(className, query);
	}
	
	public List<?> executeQuery(Map<String, String> queryMap, Class<?> className, String sortField, Integer pageNum, Integer pageSize){
		Query query = createQuery(queryMap);
		addSorting(sortField, query);
		addPagination(pageNum, pageSize, query);
		return executeQuery(className, query);
	}

	public void addPagination(Integer pageNum, Integer pageSize, Query query) {
		if(pageNum == null || pageSize == null || pageSize <=0) {
			log.info("Pagination not applied");
			return ;
		}
		final Pageable pageableRequest = new PageRequest(pageNum, pageSize);
		query.with(pageableRequest);
	}

	public List<?> executeQuery(Class<?> className, Query query) {
		return operations.find(query, className);
	}
	
	public long getCount(Query query, Class<?> className){
		return operations.count(query, className);
	}

	public void addSorting(String sortField, Query query) {
		query.with(new Sort(Sort.Direction.DESC, sortField));
	}

	public Query createQuery(Map<String, String> queryMap) {
		Query query = new Query();
		Criteria criteria = createQueryCriteria(queryMap);
		query.addCriteria(criteria);
		return query;
	}
	
	public Criteria createQueryCriteria(Map<String, String> queryMap){
		Criteria criteria = null;
		Set<String> keys = queryMap.keySet();
		for (String qfield : keys) {
			criteria = addToQuery(qfield,queryMap.get(qfield), criteria);
		}
		return criteria;
	}

	public  Criteria addToQuery(String fieldName, String value, Criteria criteria) {
		if(StringUtils.isEmpty(value)) return criteria;
		
		if(criteria == null) {
			criteria = Criteria.where(fieldName).is(value);
		}else {
			criteria = criteria.andOperator(Criteria.where(fieldName).is(value));
		}
		return criteria;
	}
	
	public Criteria addToInQuery(String fieldName, List<String> value, Criteria criteria) {
		if(StringUtils.isEmpty(value)) return criteria;
		
		if(criteria == null) {
			criteria = Criteria.where(fieldName).in(value);
		}else {
			criteria = criteria.andOperator(Criteria.where(fieldName).in(value));
		}
		return criteria;
	}
}
