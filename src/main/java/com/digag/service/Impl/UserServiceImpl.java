package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.domain.Entry;
import com.digag.domain.Repository.UserRepository;
import com.digag.domain.User;
import com.digag.service.UserService;
import com.digag.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.digag.util.Util.md5Hex;

/**
 * Created by Yuicon on 2017/8/20.
 * https://github.com/Yuicon
 */
@Service()
@SuppressWarnings("all")
public class UserServiceImpl implements UserService{

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final JwtTokenUtil jwtTokenUtil;

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public JsonResult<User> getCurrentUser(HttpServletRequest request) {

        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            User user = userRepository.findByAccount(jwtTokenUtil.getUsernameFromToken(authToken));
            if (user == null) {
                return JsonResult.<User>builder().error("token错误").build();
            }
            user.setAccount(md5Hex(user.getAccount()));
            return JsonResult.<User>builder().data(user).build();
        }
        return JsonResult.<User>builder().error("token错误").build();
    }
}
