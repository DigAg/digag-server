package com.digag.web;

import com.digag.domain.Entry;
import com.digag.domain.Repository.EntryRepository;
import com.digag.service.EntryService;
import com.digag.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yuicon on 2017/7/9.
 * https://github.com/Yuicon
 */
@RestController
@RequestMapping("/entries")
@SuppressWarnings("all")
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EntryService entryService;

    @ApiOperation(value="获取条目列表")
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult<Page<Entry>> getEntries(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return JsonResult.<Page<Entry>>builder().data(entryRepository.findAll(pageable)).build();
    }

    @ApiOperation(value="创建条目")
    @ApiImplicitParam(name = "entry", value = "条目", required = true,
            dataType = "Entry")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult<Entry> saveEntry(@RequestBody Entry entry, HttpServletRequest request) {
        return entryService.create(entry, request);
    }

    @ApiOperation(value="获取单条条目")
    @ApiImplicitParam(name = "id", value = "条目ID", required = true,
            dataType = "String")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult<Entry> getEntry(@PathVariable String id) {
        return entryService.findOne(id);
    }

    @ApiOperation(value="删除单条条目")
    @ApiImplicitParam(name = "id", value = "条目ID", required = true,
            dataType = "String")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult<String> deleteEntry(@PathVariable String id) {
        entryRepository.delete(id);
        return JsonResult.<String>builder().data("删除成功").build();
    }

    @ApiOperation(value="更新单条条目")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResult<Entry> updateEntry(@PathVariable String id, @RequestBody Entry entry) {
        entry.setId(id);
        return entryService.save(entry);
    }

}
