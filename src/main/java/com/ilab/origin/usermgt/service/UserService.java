package com.ilab.origin.usermgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.UserRepository;
import com.ilab.origin.validator.model.Result;


@RestController
@CrossOrigin(origins = "*")
public class UserService {

	@Autowired
    UserRepository repository;
	
	@RequestMapping(value = "/user/findby-mobileNum" , method = { RequestMethod.GET, RequestMethod.POST })
	public User getUser(@RequestParam(value="mobile", defaultValue="9999999999") String mobile){
		return repository.findByMobileNumber(mobile);
	}
		
	@PostMapping("/user/save")	
	public User saveUser(@RequestBody User user){		
		System.out.println(" saving user :" + user);
		return repository.save(user);
	}
	
	@RequestMapping(value = "/users" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<User> getAllUsers(){
		return repository.findAll();
	}
	
	@RequestMapping(value="/user/login" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result isValidUser(@RequestParam(value="userId") String userId , @RequestParam(value="password") String password){
		Result rs = new Result();
		rs.setIsSuccess(false);
		rs.setStatus("FAIL");
		User u = repository.findByUserId(userId); 
		if(u != null && u.getPassword().equals(password)){
			rs.setIsSuccess(true);
			rs.setToken("***234sdf*");
			rs.setStatus("SUCCESS");
		}			
		
		return rs;
	} 
}
