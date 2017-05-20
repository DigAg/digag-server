package com.digag.service;

import com.digag.domain.User;

/**
 * Created by Yuicon on 2017/5/20.
 * https://segmentfault.com/u/yuicon
 */
public interface AuthService {

    User register(User userToAdd);

    String login(String username, String password);

    String refresh(String oldToken);
}
