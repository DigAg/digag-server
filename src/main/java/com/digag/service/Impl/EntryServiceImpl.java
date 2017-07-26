package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.domain.Entry;
import com.digag.domain.Repository.EntryRepository;
import com.digag.domain.Repository.UserRepository;
import com.digag.domain.User;
import com.digag.service.EntryService;
import com.digag.util.JsonResult;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                            JedisPool jedisPool) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepositor;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jedisPool = jedisPool;
    }

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final JedisPool jedisPool;

    private final RuntimeSchema<Entry> schema = RuntimeSchema.createFrom(Entry.class);

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
        PageImpl<Entry> entryPage = null;

        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "RecommendEntries";
                List<byte[]> bytes = jedis.lrange(key.getBytes(), 0, 15);
                if (!bytes.isEmpty()) {
                    List list = new ArrayList<>();
                    bytes.stream().forEach(entry -> {
                        Entry message = schema.newMessage();
                        ProtostuffIOUtil.mergeFrom(entry, message, schema);
                        list.add(message);
                    });
                    entryPage = new PageImpl<Entry>(list);
                } else {
                    entryPage = (PageImpl<Entry>) entryRepository.findAll(pageable);
                    int timeout = 60 * 60;
                    entryPage.forEach(entry -> {
                        byte[] newBytes = ProtostuffIOUtil.toByteArray(entry, schema,
                                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                        jedis.rpush(key.getBytes(), newBytes);
                    });
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return entryPage;
    }

    private Entry getEntry2Redis(String id) {
        Entry entry = schema.newMessage();
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "Entry:" + id;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    ProtostuffIOUtil.mergeFrom(bytes, entry, schema);
                    return entry;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        entry = entryRepository.findOne(id);
        if (entry != null) {
            String key = "Entry:" + entry.getId();
            putEntry2Redis(key, entry);
        }
        return entry;
    }

    private String putEntry2Redis(String key, Entry entry) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                byte[] bytes = ProtostuffIOUtil.toByteArray(entry, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
                return jedis.setex(key.getBytes(), timeout, bytes);
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "error";
    }

}
