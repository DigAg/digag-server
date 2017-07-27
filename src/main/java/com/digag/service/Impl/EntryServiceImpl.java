package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.domain.Entry;
import com.digag.domain.Repository.EntryRepository;
import com.digag.domain.Repository.UserRepository;
import com.digag.domain.User;
import com.digag.service.EntryService;
import com.digag.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Yuicon on 2017/7/16.
 * https://github.com/Yuicon
 */
@Service()
@SuppressWarnings("all")
public class EntryServiceImpl implements EntryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EntryServiceImpl(EntryRepository entryRepository, UserRepository userRepositor, JwtTokenUtil jwtTokenUtil,
                            RedisTemplate redisTemplate) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepositor;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Entry> valueOperations;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisTemplate<String, Entry> redisTemplate;

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

    @Override
    public JsonResult<Entry> save(Entry entry) {
        return JsonResult.<Entry>builder().data(entryRepository.save(entry)).build();
    }

    @Override
    public JsonResult<Entry> findOne(String id) {
        return JsonResult.<Entry>builder().data(getEntry2Redis(id)).build();
    }

    @Override
    public JsonResult<Page<Entry>> findAll(Integer page, Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(page, size, sort);
//        if (page == 0 && size == 15) {
//            return JsonResult.<Page<Entry>>builder().data(getRecommendEntries(pageable)).build();
//        }
        return JsonResult.<Page<Entry>>builder().data(entryRepository.findAll(pageable)).build();
    }

    private Page<Entry> getRecommendEntries(Pageable pageable) {
        return null;
    }

    private Entry getEntry2Redis(String id){

        Optional<Entry> entry = Optional.ofNullable(valueOperations.get(id));

        if (entry.isPresent()) {
            return entry.get();
        }

        entry = Optional.ofNullable(entryRepository.findOne(id));
        if (entry.isPresent()) {
            putEntry2Redis(entry.get());
        }
        return entry.orElseGet(Entry::new);
    }

    private void putEntry2Redis(Entry entry) {
        valueOperations.set(entry.getId(), entry);
    }

}
