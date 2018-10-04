package com.ilab.origin.mobileapp.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.mobileapp.model.AppUser;



public interface AppUserRepository extends MongoRepository<AppUser, String> {

	public AppUser findByMobileNumber(String mobileNumber);
	
	public AppUser findByUserId(String userId);

}
