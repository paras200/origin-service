package com.ilab.origin.mobileapp.service;

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

import com.ilab.origin.email.EmailClient;
import com.ilab.origin.mobileapp.common.AppResult;
import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.mobileapp.repo.AppUserRepository;
import com.ilab.origin.password.PasswordService;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/app-user")
public class AppUserService {

	private static Log log = LogFactory.getLog(AppUserService.class.getName());
	
	@Autowired
	private AppUserRepository userRepo;
	
	@Autowired
	private EmailClient emailClient;
	
	@Autowired
	private PasswordService passService;
	
	@PostMapping("/register-newuser")	
	public AppResult registerNewUser(@RequestBody AppUser user) throws OriginException{		
		AppResult ar = new AppResult();
		log.info(" saving user :" + user);
		AppUser oldUser = userRepo.findByUserId(user.getUserId());
		if(oldUser != null) {
			ar.setIsSuccess(false);
			ar.setMessage("User id already exits");
			ar.setStatus(Result.STATUS_FAILUER);
			//throw new OriginException(Result.STATUS_FAILUER + " User id already exits");
			log.error(user.getUserId() + " : User registration fails as user id already exits");
			return ar;
		}
		ar.setIsSuccess(true);
		ar.setStatus(Result.STATUS_SUCCESS);
		ar.setAppUser(userRepo.save(user));
		return ar;
	}
	
	@PostMapping(value="/login")
	public AppResult isValidUser(@RequestBody AppUser user){
		String userId = user.getUserId();
		String password = user.getPassword();
		AppResult rs = new AppResult();
		rs.setIsSuccess(false);
		rs.setStatus(Result.STATUS_FAILUER);
		AppUser u = userRepo.findByUserId(userId); 
		if(u != null && u.getPassword().equals(password)){
			rs.setIsSuccess(true);
			rs.setStatus(Result.STATUS_SUCCESS);
			u.setPassword("****");
			rs.setAppUser(u);
			log.info("login successful for user : " + user.getUserId());
		}else {
			rs.setMessage("Please chcek, either userId or passowrd is not correct");
			log.info("login failed for user : " + user.getUserId());
		}
		return rs;
	} 
	
	@RequestMapping(value = "/all-users" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<AppUser> getAllUsers(){
		return userRepo.findAll();
	}
	
	@RequestMapping(value = "/send-temp-password" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result sendTempPassword(@RequestParam(value="userId") String userId){
		AppUser appuser = userRepo.findByUserId(userId);
		Result rs = new Result();
		if(appuser == null) {
			rs.setStatus(Result.STATUS_FAILUER);
			rs.setMessage("User Id  : "+ userId+"  doesn't exits");
		}else {
			appuser.setIsTempPassword(true);
			String tempPass = passService.generatePasswordSequence();
			appuser.setPassword(tempPass);
			userRepo.save(appuser);
			
			emailClient.sendTemporaryPassword(userId, tempPass);
			rs.setStatus(Result.STATUS_SUCCESS);
			rs.setMessage("Email is sent on email id : "+ userId+" ");
		}
		return rs;
	}
	
	@PostMapping("/change-password")	
	public AppUser saveUser(@RequestParam(value="userId") String userId , @RequestParam(value="password") String password){		
		log.info(" change password for user :" + userId);
		AppUser user = userRepo.findByUserId(userId);
		if(user != null) {
			user.setIsTempPassword(false);
			user.setPassword(password);
			return userRepo.save(user);
		}
		return null;
	}
}
