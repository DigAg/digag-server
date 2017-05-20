package com.digag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Yuicon on 2017/5/20.
 * https://segmentfault.com/u/yuicon
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

    Role findByName(final String name);
}
