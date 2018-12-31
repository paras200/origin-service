package com.ilab.origin.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userId));
        } else {
            return JwtUserFactory.create(user);
        }

        //throw new UsernameNotFoundException(userId);
    }
}
