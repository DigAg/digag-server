package com.digag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Yuicon on 2017/7/8.
 * https://github.com/Yuicon
 */
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,String> {
}
