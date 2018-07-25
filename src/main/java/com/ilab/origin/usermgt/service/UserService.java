package com.ilab.origin.usermgt.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.UserRepository;
import com.ilab.origin.validator.model.Result;


@RestController
@CrossOrigin(origins = "*")
public class UserService {

	private static Log log = LogFactory.getLog(UserService.class.getName());
	
	@Autowired
    UserRepository userRepo;
	
	@RequestMapping(value = "/user/findby-mobileNum" , method = { RequestMethod.GET, RequestMethod.POST })
	public User getUser(@RequestParam(value="mobile") String mobile){
		return userRepo.findByMobileNumber(mobile);
	}
		
	@PostMapping("/user/save")	
	public User saveUser(@RequestBody User user){		
		log.info(" saving user :" + user);
		return userRepo.save(user);
	}
	
	@PostMapping("/user/register-newuser")	
	public User registerNewUser(@RequestBody User user) throws OriginException{		
		log.info(" saving user :" + user);
		User oldUser = userRepo.findByUserId(user.getUserId());
		if(oldUser != null) {
			throw new OriginException(Result.STATUS_FAILUER + " User id already exits");
		}
		return userRepo.save(user);
	}
	
	@RequestMapping(value = "/all-users" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	@PostMapping(value="/user/login")
	public Result isValidUser(@RequestParam(value="userId") String userId , @RequestParam(value="password") String password){
		Result rs = new Result();
		rs.setIsSuccess(false);
		rs.setStatus(Result.STATUS_FAILUER);
		User u = userRepo.findByUserId(userId); 
		if(u != null && u.getPassword().equals(password)){
			rs.setIsSuccess(true);
			rs.setToken("***234sdf*");
			rs.setStatus(Result.STATUS_SUCCESS);
		}			
		
		return rs;
	} 
}
