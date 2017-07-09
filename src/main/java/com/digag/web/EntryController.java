package com.digag.web;

import com.digag.domain.Entry;
import com.digag.domain.Repository.EntryRepository;
import com.digag.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * Created by Yuicon on 2017/7/9.
 * https://github.com/Yuicon
 */
@RestController
@RequestMapping("/entries")
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;

    @ApiOperation(value="获取条目列表")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult getEntries() {
        return new JsonResult.JsonResultBuilder<List<Entry>>().data(entryRepository.findAll()).build();
    }

    @ApiOperation(value="创建条目")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult saveEntry(Entry entry) {
        return new JsonResult.JsonResultBuilder<Entry>().data(entryRepository.save(entry)).build();
    }

    @ApiOperation(value="获取单条条目")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult getEntry(@PathVariable String id) {
        return new JsonResult.JsonResultBuilder<Entry>().data(entryRepository.findOne(id)).build();
    }

    @ApiOperation(value="删除单条条目")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteEntry(@PathVariable String id) {
        entryRepository.delete(id);
        return new JsonResult.JsonResultBuilder<String>().data("删除成功").build();
    }

    @ApiOperation(value="更新单条条目")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResult updateEntry(@PathVariable String id, @RequestBody Entry entry) {
        entry.setId(id);
        return new JsonResult.JsonResultBuilder<Entry>().data(entryRepository.save(entry)).build();
    }




}
