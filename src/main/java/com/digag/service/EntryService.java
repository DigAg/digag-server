package com.digag.service;

import com.digag.domain.Entry;
import com.digag.util.JsonResult;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yuicon on 2017/7/16.
 * https://github.com/Yuicon
 */
public interface EntryService {

    JsonResult<Entry> create (Entry entry, HttpServletRequest request);

    JsonResult<Entry> save (Entry entry);

    JsonResult<Integer> updateCollectionCount (String id, HttpServletRequest request);

    JsonResult<Entry> findOne (String id);

    JsonResult<Page<Entry>> findAll (Integer page, Integer size, HttpServletRequest request);

}
