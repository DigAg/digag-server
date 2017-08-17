package com.digag.service.Impl;

import com.digag.config.security.JwtTokenUtil;
import com.digag.domain.*;
import com.digag.domain.Collection;
import com.digag.domain.Repository.BrowseLogRepository;
import com.digag.domain.Repository.CollectionRepository;
import com.digag.domain.Repository.EntryRepository;
import com.digag.domain.Repository.UserRepository;
import com.digag.service.EntryService;
import com.digag.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
                            CollectionRepository collectionRepository, BrowseLogRepository browseLogRepository) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepositor;
        this.jwtTokenUtil = jwtTokenUtil;
        this.collectionRepository = collectionRepository;
        this.browseLogRepository = browseLogRepository;
    }

    private final EntryRepository entryRepository;

    private final BrowseLogRepository browseLogRepository;

    private final CollectionRepository collectionRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

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
    public JsonResult<Integer> updateCollectionCount(String id, HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        Integer integer = 0;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            User user = userRepository.findByAccount(jwtTokenUtil.getUsernameFromToken(authToken));
            Optional<Entry> entry = Optional.
                    ofNullable(entryRepository.findOne(id));
            if (entry.isPresent()) {
                Optional<Collection> collection = Optional.
                        ofNullable(collectionRepository.findByUidAndEid(user.getId(), id));
                if (collection.isPresent()) {
                    collectionRepository.delete(collection.get().getId());
                    Entry entry1 = entry.get();
                    entry1.setCollectionCount(entry.get().getCollectionCount() - 1);
                    entryRepository.save(entry1);
                } else {
                    collectionRepository.save(new Collection(user.getId(), id));
                    Entry entry1 = entry.get();
                    entry1.setCollectionCount(entry.get().getCollectionCount() + 1);
                    entryRepository.save(entry1);
                    integer++;
                }
            }
        }
        return JsonResult.<Integer>builder().data(integer).build();
    }

    @Override
    public JsonResult<Entry> findOne(String id) {
        return JsonResult.<Entry>builder().data(entryRepository.findOne(id)).build();
    }

    @Override
    public JsonResult<Page<Entry>> findAll(Integer page, Integer size, HttpServletRequest request) {

        String authHeader = request.getHeader(this.tokenHeader);
        Optional<BrowseLog> browseLog;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            User user = userRepository.findByAccount(jwtTokenUtil.getUsernameFromToken(authToken));
            browseLog = Optional.
                    ofNullable(browseLogRepository.findByUid(user.getId()));
            if (browseLog.isPresent()) {
                BrowseLog browseLog1 = browseLog.get();
                browseLog1.setCount(browseLog1.getCount() + 1);
                browseLog1.setLastTime(new Date(System.currentTimeMillis()));
                browseLogRepository.save(browseLog1);
            } else {
                browseLogRepository.save(new BrowseLog(request.getRemoteAddr(), user.getId(), 1, new Date(System.currentTimeMillis())));
            }
        } else {
            browseLog = Optional.
                    ofNullable(browseLogRepository.findByIp(request.getRemoteAddr()));
            if (browseLog.isPresent()) {
                BrowseLog browseLog1 = browseLog.get();
                browseLog1.setCount(browseLog1.getCount() + 1);
                browseLog1.setLastTime(new Date(System.currentTimeMillis()));
                browseLogRepository.save(browseLog1);
            } else {
                browseLogRepository.save(new BrowseLog(request.getRemoteAddr(), 1, new Date(System.currentTimeMillis())));
            }
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(page, size, sort);
        return JsonResult.<Page<Entry>>builder().data(entryRepository.findAll(pageable)).build();
    }

    @Override
    public JsonResult<Page<Entry>> findByUserName(Integer page, Integer size, String username) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(page, size, sort);
        return JsonResult.<Page<Entry>>builder().data(entryRepository.findByAuthor(username, pageable)).build();
    }


}
