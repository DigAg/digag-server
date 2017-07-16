package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.domain.Entry;
import com.digag.domain.Repository.EntryRepository;
import com.digag.domain.Repository.UserRepository;
import com.digag.domain.User;
import com.digag.service.EntryService;
import com.digag.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Yuicon on 2017/7/16.
 * https://github.com/Yuicon
 */
@Service()
public class EntryServiceImpl implements EntryService{

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public JsonResult<Entry> create(Entry entry, HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            User user = userRepository.findByAccount(jwtTokenUtil.getUsernameFromToken(authToken));
            entry.setAuthor(user.getUsername());
            entry.setCreatedAt(new Date(System.currentTimeMillis()));
            return JsonResult.<Entry>builder().data(entryRepository.save(entry)).build();
        }
        return JsonResult.<Entry>builder().error("创建条目失败!").build();
    }
}
