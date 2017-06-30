package com.digag.config.security;

import com.digag.domain.Role;
import com.digag.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yuicon on 2017/5/14.
 * https://segmentfault.com/u/yuicon
 */
final class JwtUserFactory {

    private JwtUserFactory() {
    }

    static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getAccount(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles().stream().map(Role::getName).collect(Collectors.toList())),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
