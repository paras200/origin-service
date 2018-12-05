package com.ilab.origin.security;

import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
public class SecurityConfig1 {//extends WebSecurityConfigurerAdapter {

	/*		protected void configure(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance()).
					withUser("adminui").password("originui").roles("UIADMIN").and()
					.withUser("adminapp").password("originapp").roles( "UIADMIN","APPADMIN");
		}

		// Authorization : Role -> Access
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic().and().authorizeRequests()
					.antMatchers("/swagger/**").permitAll()
					.antMatchers("/**").hasRole("UIADMIN").and()
					.csrf().disable().headers().frameOptions().disable();
		}*/

}
