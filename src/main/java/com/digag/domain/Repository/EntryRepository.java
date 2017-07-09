package com.digag.domain.Repository;

import com.digag.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Yuicon on 2017/7/8.
 * https://github.com/Yuicon
 */
@RepositoryRestResource
public interface EntryRepository extends JpaRepository<Entry,String> {
}
