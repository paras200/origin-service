package com.ilab.origin.usermgt.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.usermgt.model.User;



public interface UserRepository extends MongoRepository<User, String> {

	public User findByMobileNumber(String mobileNumber);
	
	public User findByUserId(String userId);

}
