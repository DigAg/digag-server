package com.digag.domain.Repository;

import com.digag.domain.Collection;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yuicon on 2017/7/29.
 * https://github.com/Yuicon
 */
@RepositoryRestResource
@CacheConfig(cacheNames = "collections")
public interface CollectionRepository extends JpaRepository<Collection, String> {

    @Cacheable()
    List<Collection> findByUid(String uid);

    @Cacheable()
    List<Collection> findByEid(String eid);


    Collection findByUidAndEid(String uid, String eid);

    @Transactional
    @CacheEvict(allEntries=true)
    <S extends Collection> S save(S s);

    @Transactional
    @CacheEvict(allEntries=true)
    void delete(String id);

}
