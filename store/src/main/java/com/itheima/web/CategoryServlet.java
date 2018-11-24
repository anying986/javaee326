package com.itheima.web;

import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/category")
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService =new CategoryService();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = categoryService.findAll();
        Result result=new Result(Result.SUCCESS,list,"查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }



}
