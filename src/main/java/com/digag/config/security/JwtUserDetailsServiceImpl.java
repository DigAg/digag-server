package com.digag.config.security;

import com.digag.domain.User;
import com.digag.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Yuicon on 2017/5/14.
 * https://segmentfault.com/u/yuicon
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 提供一种从用户名可以查到用户并返回的方法
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
