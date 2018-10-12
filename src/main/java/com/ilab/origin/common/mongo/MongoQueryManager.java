package com.ilab.origin.common.mongo;

import java.util.ArrayList;
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

	public List<?> executeQuery(Query query, Class<?> className, String sortField, Integer pageNum, Integer pageSize){
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
	
	public Query createQuery(Criteria criteria) {
		Query query = new Query();
		query.addCriteria(criteria);
		return query;
	}
	
	public Query createQuery(List<Criteria> criteriaList) {
		Criteria criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]))  ;
		Query query = new Query();
		query.addCriteria(criteria);
		return query;
	}
	
	public List<?> executeQuery(Class<?> className, List<Criteria> criteriaList) {
		Criteria criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]))  ;
		Query query = new Query();
		query.addCriteria(criteria);
		return operations.find(query, className);
	}
	
	public Criteria createQueryCriteria(Map<String, String> queryMap){
		
		List<Criteria> cList = createCriteriaList(queryMap);
		Criteria criteria = new Criteria().andOperator(cList.toArray(new Criteria[cList.size()]))  ;
		return criteria;
	}

	public List<Criteria> createCriteriaList(Map<String, String> queryMap) {
		List<Criteria> cList = new ArrayList<>();
		Set<String> keys = queryMap.keySet();        
		for (String qfield : keys) {
			cList.add(Criteria.where(qfield).is(queryMap.get(qfield)));
		}
		return cList;
	}

	public  List<Criteria> addToQuery(String fieldName, String value, List<Criteria> criteriaList) {
		if(StringUtils.isEmpty(value)) return criteriaList;
		
		if(criteriaList == null) {
			criteriaList = new ArrayList<>();
		}
		criteriaList.add(Criteria.where(fieldName).is(value));
		return criteriaList;
	}
	
    public Criteria createCriteria(List<Criteria> cList){
		Criteria criteria = new Criteria().andOperator(cList.toArray(new Criteria[cList.size()]))  ;
		return criteria;
	}
	
    public  List<Criteria> addToInQuery(String fieldName, List<String> value, List<Criteria> criteriaList) {
		if(value == null || value.size() == 0) return criteriaList;
		
		if(criteriaList == null) {
			criteriaList = new ArrayList<>();
		}
		criteriaList.add(Criteria.where(fieldName).in(value));
		return criteriaList;
	}
    
	public Criteria addToInQuery(String fieldName, List<String> value) {
		if(StringUtils.isEmpty(value)) return null;
		
		return Criteria.where(fieldName).in(value);
	}

}
