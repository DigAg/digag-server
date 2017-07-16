package com.digag.service;

import com.digag.domain.Entry;
import com.digag.util.JsonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yuicon on 2017/7/16.
 * https://github.com/Yuicon
 */
public interface EntryService {

    JsonResult<Entry> create (Entry entry, HttpServletRequest request);
}
