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
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


/**
 * Created by Yuicon on 2017/7/16.
 * https://github.com/Yuicon
 */
@Service()
@SuppressWarnings("all")
public class EntryServiceImpl implements EntryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RuntimeSchema<Entry> schema = RuntimeSchema.createFrom(Entry.class);

    @Autowired
    private JedisPool jedisPool;

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
            putEntry2Redis(entry);
        }
        return entry;
    }

    private String putEntry2Redis(Entry entry) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "Entry:" + entry.getId();
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
