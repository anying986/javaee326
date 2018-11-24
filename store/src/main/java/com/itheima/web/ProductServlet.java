package com.itheima.web;

import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/product")
public class ProductServlet extends BaseServlet {
    private ProductService productService=new ProductService();

    public void findNewHot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, List<Product>> map = productService.findNewHot();
        Result result = new Result(Result.SUCCESS, map, "查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void findById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        Product product = productService.findById(pid);
        Result result = new Result(Result.SUCCESS, product, "查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize=12;
        PageBean<Product> pageBean = productService.findByPage(cid, currentPage, pageSize);
        Result result=new Result(Result.SUCCESS,pageBean,"查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

}
