package com.digag.web;

import com.digag.config.security.JwtAuthenticationRequest;
import com.digag.config.security.JwtAuthenticationResponse;
import com.digag.domain.User;
import com.digag.service.AuthService;
import com.digag.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yuicon on 2017/5/20.
 * https://segmentfault.com/u/yuicon
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "登录")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
//    })
    @ApiImplicitParam(name = "authenticationRequest", value = "JWT登录验证类", required = true,
            dataType = "JwtAuthenticationRequest")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonResult createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        return authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @ApiOperation(value = "刷新Token")
    @ApiImplicitParam(name = "request", value = "请求信息（带有tokenHeader）", required = true,
            dataType = "HttpServletRequest")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @ApiOperation(value = "注册")
    @ApiImplicitParam(name = "addedUser", value = "用户实体", required = true, dataType = "User")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResult register(@RequestBody User addedUser) throws AuthenticationException {
        return authService.register(addedUser);
    }

}
