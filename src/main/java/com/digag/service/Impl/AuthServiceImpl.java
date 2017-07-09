package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.config.security.JwtUser;
import com.digag.domain.Role;
import com.digag.domain.Repository.RoleRepository;
import com.digag.domain.User;
import com.digag.domain.Repository.UserRepository;
import com.digag.service.AuthService;
import com.digag.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * Created by Yuicon on 2017/5/20.
 * https://segmentfault.com/u/yuicon
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public JsonResult register(User userToAdd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null){
            userRole = roleRepository.save(new Role("ROLE_USER"));
        }
        userToAdd.setRoles(Collections.singletonList(userRole));

        JsonResult jsonResult;

        try {
            jsonResult = new JsonResult.JsonResultBuilder<User>().data(userRepository.save(userToAdd)).build();
        } catch (DataIntegrityViolationException e) {
            logger.debug(e.getMessage());
            jsonResult = new JsonResult.JsonResultBuilder<User>().error(e.getRootCause().getMessage()).build();
        }

        return jsonResult;
    }

    @Override
    public JsonResult login(String username, String password) {

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        JsonResult jsonResult;
        try {
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            jsonResult = new JsonResult.JsonResultBuilder<String>().data(jwtTokenUtil.generateToken(userDetails)).build();
        } catch (BadCredentialsException e) {
            logger.debug(e.getMessage());
            jsonResult = new JsonResult.JsonResultBuilder<String>().error("帐号或密码错误").build();
        }

        return jsonResult;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
