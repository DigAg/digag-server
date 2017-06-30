package com.digag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Yuicon on 2017/5/14.
 * https://segmentfault.com/u/yuicon
 */
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User,String>{
    User findByUsername(final String username);

    User findByAccount(final String account);
}
