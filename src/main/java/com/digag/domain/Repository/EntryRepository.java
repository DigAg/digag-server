package com.digag.domain.Repository;

import com.digag.domain.Entry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Yuicon on 2017/7/8.
 * https://github.com/Yuicon
 */
@RepositoryRestResource
@CacheConfig(cacheNames = "entries")
public interface EntryRepository extends JpaRepository<Entry,String> {

    @Cacheable()
    Page<Entry> findAll(Pageable var1);

    @Cacheable()
    Page<Entry> findByAuthor(String author, Pageable var1);

    @Transactional
    @CacheEvict(allEntries=true)
    <S extends Entry> S  save(S s);

    @Cacheable()
    Entry findOne(String id);

}
