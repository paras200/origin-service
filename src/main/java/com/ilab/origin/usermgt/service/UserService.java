package com.ilab.origin.usermgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.UserRepository;


@RestController
public class UserService {

	@Autowired
    UserRepository repository;
	
	@RequestMapping("/user/findby-mobileNum")
	public User getUser(@RequestParam(value="mobile", defaultValue="9999999999") String mobile){
		return repository.findByMobileNumber(mobile);
	}
		
	@PostMapping("/user/save")	
	public User saveUser(@RequestBody User user){		
		System.out.println(" saving user :" + user);
		return repository.save(user);
	}
	
	@RequestMapping("/users")
	public List<User> getAllUsers(){
		return repository.findAll();
	}
}
