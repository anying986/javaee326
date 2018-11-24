package com.itheima.web;

import com.itheima.domain.Category;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.exception.NoDelException;
import com.itheima.service.CategoryService;
import com.itheima.service.ProductService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/admin")
public class AdminServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();
    private ProductService productService=new ProductService();

    public void showProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        PageBean<Product> pageBean = productService.findAll(currentPage, pageSize);
        Result result = new Result(Result.SUCCESS,pageBean, "删除成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void delCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        try {
            categoryService.delCategory(cid);
            Result result = new Result(Result.SUCCESS,"", "删除成功");
            response.getWriter().print(JSONObject.fromObject(result));
        } catch (NoDelException e) {
            e.printStackTrace();
            Result result = new Result(Result.FAILURE,"", "分类下有商品,不可删除");
            response.getWriter().print(JSONObject.fromObject(result));
        }
    }

    public void updateCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        String cname = request.getParameter("cname");
        categoryService.updateCategory(cid,cname);
        Result result = new Result(Result.SUCCESS,"", "更新成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void findById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category category = categoryService.findById(cid);
        Result result = new Result(Result.SUCCESS,category, "查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cname = request.getParameter("cname");
        categoryService.addCategory(cname);
        Result result = new Result(Result.SUCCESS,"", "添加成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = categoryService.findAll();
        Result result = new Result(Result.SUCCESS, list, "查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

}
