package com.digag.domain.Repository;

import com.digag.domain.BrowseLog;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yuicon on 2017/8/9.
 * https://github.com/Yuicon
 */
@RepositoryRestResource
@CacheConfig(cacheNames = "browseLogs")
public interface BrowseLogRepository  extends JpaRepository<BrowseLog,String> {

    @Cacheable()
    BrowseLog findByIp(String ip);

    @Cacheable()
    BrowseLog findByUid(String uid);

    @Transactional
    @CacheEvict(allEntries=true)
    <S extends BrowseLog> S  save(S s);

}
