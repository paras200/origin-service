package com.ilab.origin.validator.model;

import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;

public class LoginResult extends Result{

	private User user;
	private Merchant merchant;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
	
}
