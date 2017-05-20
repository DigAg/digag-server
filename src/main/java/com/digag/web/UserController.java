package com.digag.web;

import com.digag.domain.User;
import com.digag.domain.UserRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Yuicon on 2017/5/14.
 * https://segmentfault.com/u/yuicon
 * 在 @PreAuthorize 中我们可以利用内建的 SPEL 表达式：比如 'hasRole()' 来决定哪些用户有权访问。
 * 需注意的一点是 hasRole 表达式认为每个角色名字前都有一个前缀 'ROLE_'。所以这里的 'ADMIN' 其实在
 * 数据库中存储的是 'ROLE_ADMIN' 。这个 @PreAuthorize 可以修饰Controller也可修饰Controller中的方法。
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @ApiOperation(value="获取用户列表")
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return repository.findAll();
    }

    @ApiOperation(value="新建用户")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    User addUser(@RequestBody User addedUser) {
        return repository.save(addedUser);
    }

    @ApiOperation(value="获取用户", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String")
    @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable String id) {
        return repository.findOne(id);
    }

    @ApiOperation(value="修改用户", notes="通过ID")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return repository.save(updatedUser);
    }

    @ApiOperation(value="删除用户", notes="通过ID")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    User removeUser(@PathVariable String id) {
        User deletedUser = repository.findOne(id);
        repository.delete(id);
        return deletedUser;
    }

    @ApiOperation(value="获取用户", notes="通过用户名")
    @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public User getUserByUsername(@RequestParam(value="username") String username) {
        return repository.findByUsername(username);
    }
}
