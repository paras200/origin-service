package com.ilab.origin.feedback.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ilab.origin.feedback.mode.ContactInfo;

public interface ContactInfoRepo extends MongoRepository<ContactInfo, String>{

}
