package com.ilab.origin.usermgt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.email.EmailClient;
import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.mobileapp.repo.AppUserRepository;
import com.ilab.origin.password.PasswordService;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.UserRepository;
import com.ilab.origin.validator.model.LoginResult;
import com.ilab.origin.validator.model.Result;


@RestController
@CrossOrigin(origins = "*")
public class UserService {

	private static Log log = LogFactory.getLog(UserService.class.getName());
	
	@Autowired
    private UserRepository userRepo;

	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@Autowired
	private MerchantRepository merchantRepo;
	
	@Autowired
	private EmailClient emailClient;
	
	@Autowired
	private AppUserRepository appUserRepo;
	
	@Autowired
	private PasswordService passService;
	
	//TODO - remove this API
	@PostMapping("/user/save")	
	public User saveUser(@RequestBody User user){		
		log.info(" saving user :" + user);
		user.setIsTempPassword(false);
		return userRepo.save(user);
	}
	
	@PostMapping("/user/change-password")	
	public User saveUser(@RequestParam(value="userId") String userId , @RequestParam(value="password") String password){		
		log.info(" change password for user :" + userId);
		User user = userRepo.findByUserId(userId);
		if(user != null) {
			user.setIsTempPassword(false);
			user.setPassword(password);
			return userRepo.save(user);
		}
		return null;
	}
	
	@PostMapping("/user/register-newuser")	
	public User registerNewUser(@RequestBody User user) throws OriginException{		
		log.info(" saving user :" + user);
		user.setUserType(User.USER_TYPE_ADMIN);
		User oldUser = userRepo.findByUserId(user.getUserId());
		if(oldUser != null) {
			throw new OriginException(Result.STATUS_FAILUER + " User id already exits");
		}
		return userRepo.save(user);
	}
	
	@RequestMapping(value = "/user/findby-userId" , method = { RequestMethod.GET, RequestMethod.POST })
	public User getUserByUserId(@RequestParam(value="userId") String userId){
		User merchantUser = userRepo.findByUserId(userId);
		if(merchantUser == null) {
			AppUser appU = appUserRepo.findByUserId(userId);
			if(appU != null) {
				merchantUser = new User();
				merchantUser.setEmail(appU.getUserId());
				merchantUser.setFirstName(appU.getFirstName());
				merchantUser.setLastName(appU.getLastName());
				merchantUser.setLocation(appU.getLocation());
				merchantUser.setMobileNumber(appU.getMobileNumber());
				//merchantUser.set
			}
		}
		return merchantUser;
	}
	
	@RequestMapping(value = "/user/all-users" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	@PostMapping(value="/user/login")
	public LoginResult isValidUser(@RequestBody User user){
		String userId = user.getUserId();
		String password = user.getPassword();
		LoginResult rs = new LoginResult();
		rs.setIsSuccess(false);
		rs.setStatus(Result.STATUS_FAILUER);
		User u = userRepo.findByUserId(userId); 
		if(u != null && u.getPassword().equals(password)){
			rs.setIsSuccess(true);
			rs.setToken("***234sdf*");
			rs.setStatus(Result.STATUS_SUCCESS);
			rs.setUser(u);
			if(!(StringUtils.isEmpty(u.getMerchantId()))) {
				Merchant merchant = merchantRepo.findById(u.getMerchantId());
				rs.setMerchant(merchant);
			}
		}			
		return rs;
	} 
	
	@RequestMapping(value = "/user/generic-query" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<?> getUsers(@RequestBody Map<String, String> queryMap){
		List<?> results =  mongoQueryMgr.executeQuery(queryMap, User.class);
		log.info("result size retruned : " + results.size());
		return results;
	}
	
	@PostMapping("/link-merchant-user")	
	public Result adduser(@RequestParam(value="userIdList") List<String> userIdList,@RequestParam(value="merchantId") String merchantId){	
		List<User> uList = new ArrayList<>();
		for (String userId : userIdList) {
			// send email
			User user = new User();
			user.setUserId(userId);
			user.setUserCode(UUID.randomUUID().toString());
			user.setMerchantId(merchantId);
			user.setIsActive(false);
			uList.add(user);
		}
		List<User> savedUserList = userRepo.save(uList);
		for (User user : savedUserList) {
			emailClient.sendUserRegistrationLink(user.getUserId(), user.getUserCode());
		}
		return new Result(Result.STATUS_SUCCESS);
	}
	
	@PostMapping("/user/register-merchant-user")	
	public User registerMerchantUser(@RequestBody User user) throws OriginException{	
		if(StringUtils.isEmpty(user.getMerchantId())) {
			throw new OriginException("merchant Id can't be empty");
		}
		user.setUserType(User.USER_TYPE_ADMIN);
		user.setIsActive(true);
		User oldUser = userRepo.findByUserId(user.getUserId());
		if(oldUser != null) {
			throw new OriginException(Result.STATUS_FAILUER + " User id already exits");
		}
		return userRepo.save(user);
	}
	
	@RequestMapping(value = "/user/send-temp-password" , method = { RequestMethod.GET, RequestMethod.POST })
	public Result sendTempPassword(@RequestParam(value="userId") String userId){
		User merchantUser = userRepo.findByUserId(userId);
		Result rs = new Result();
		if(merchantUser == null) {
			rs.setStatus(Result.STATUS_FAILUER);
			rs.setMessage("User Id  : "+ userId+"  doesn't exits");
		}else {
			merchantUser.setIsTempPassword(true);
			String tempPassword = passService.generatePasswordSequence();
			merchantUser.setPassword(tempPassword);
			userRepo.save(merchantUser);
			
			emailClient.sendTemporaryPassword(userId, tempPassword);
			rs.setStatus(Result.STATUS_SUCCESS);
			rs.setMessage("Email is sent on email id : "+ userId+" ");
		}
		return rs;
	}
}
