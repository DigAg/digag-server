package com.digag.web;

import com.digag.domain.Article;
import com.digag.domain.Repository.ArticleRepository;
import com.digag.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Yuicon on 2017/7/11.
 * https://github.com/Yuicon
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @ApiOperation(value="获取文章列表")
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult<List<Article>> getArticles() {
        return JsonResult.<List<Article>>builder().data(articleRepository.findAll()).build();
    }

    @ApiOperation(value="创建文章")
    @ApiImplicitParam(name = "article", value = "文章", required = true,
            dataType = "Article")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult<Article> saveArticle(Article article) {
        return JsonResult.<Article>builder().data(articleRepository.save(article)).build();
    }

    @ApiOperation(value="获取单条文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true,
            dataType = "String")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult<Article> getArticle(@PathVariable String id) {
        return JsonResult.<Article>builder().data(articleRepository.findOne(id)).build();
    }

    @ApiOperation(value="删除单条文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true,
            dataType = "String")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult<String> deleteArticle(@PathVariable String id) {
        articleRepository.delete(id);
        return JsonResult.<String>builder().data("删除成功").build();
    }

    @ApiOperation(value="更新单条文章")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResult<Article> updateArticle(@PathVariable String id, @RequestBody Article article) {
        article.setId(id);
        return JsonResult.<Article>builder().data(articleRepository.save(article)).build();
    }
}
