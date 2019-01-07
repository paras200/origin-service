package com.ilab.origin.security;

public enum AuthorityName {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");
    
    AuthorityName(String name){
        this.name = name;
    }
    private String name;
	  
    public String getName(){
        return this.name;
    }

}