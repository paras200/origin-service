package com.ilab.origin.mobileapp.common;

import com.ilab.origin.mobileapp.model.AppUser;
import com.ilab.origin.validator.model.Result;

public class AppResult extends Result{

	private AppUser appUser;

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}
	
	
}
