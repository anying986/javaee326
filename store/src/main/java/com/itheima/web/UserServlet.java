package com.itheima.web;


import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.UUIDUtils;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet(urlPatterns = "/user")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    public void addUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        User user=new User();

        try {
            BeanUtils.populate(user,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setUid(UUIDUtils.getUUID());
        userService.addUser(user);
        Result result=new Result(Result.SUCCESS,"","注册成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void login(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.login(username, password);
        if (user==null){
            Result result=new Result(Result.FAILURE,"","登录失败");
            response.getWriter().print(JSONObject.fromObject(result));
        }else{
            request.getSession().setAttribute("user",user);
            Cookie cookie=new Cookie("username",username);
            cookie.setPath(request.getContextPath());
            cookie.setDomain("itheima326.com");
            response.addCookie(cookie);
            Result result=new Result(Result.SUCCESS,"","登录成功");
            response.getWriter().print(JSONObject.fromObject(result));
        }
    }

    public void loginOut(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.getSession().invalidate();
        Cookie cookie=new Cookie("username",null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        cookie.setDomain("itheima326.com");
        response.addCookie(cookie);
        Result result=new Result(Result.SUCCESS,"","退出成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }


}
