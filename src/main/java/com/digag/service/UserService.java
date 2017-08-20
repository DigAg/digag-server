package com.digag.service;

import com.digag.domain.User;
import com.digag.util.JsonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yuicon on 2017/8/8.
 * https://github.com/Yuicon
 */
public interface UserService {

    JsonResult<User> getCurrentUser(HttpServletRequest request);

}
