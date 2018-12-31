package com.ilab.origin.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ilab.origin.usermgt.model.User;

public final class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		//Fix this all users will have to role defined, plus change the date parameter (last of the JWT user)
		List<Authority> roleList = new ArrayList<>();
		Authority authority = new Authority();
		authority.setName(AuthorityName.ROLE_USER);
		roleList.add(authority);
		Authority authority1 = new Authority();
		authority.setName(AuthorityName.ROLE_ADMIN);
		roleList.add(authority1);
		return new JwtUser(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPassword(), mapToGrantedAuthorities(roleList), user.getIsActive(), new Date());
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
		try {
		List<GrantedAuthority> collect = authorities.stream()
				.map(authority -> new SimpleGrantedAuthority("ROLE_ADMIN")).collect(Collectors.toList());
		return collect;
		} catch(Exception e ) {
			e.printStackTrace();
		}

		return null;
	}
}
