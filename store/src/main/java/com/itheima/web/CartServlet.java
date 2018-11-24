package com.itheima.web;

import com.itheima.domain.Cart;
import com.itheima.domain.CartItem;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/cart")
public class CartServlet extends BaseServlet {
    private ProductService productService=new ProductService();

    public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.clearCart();
        Result result=new Result(Result.SUCCESS,"","清空成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void delCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.removeCart(pid);
        Result result=new Result(Result.SUCCESS,"","删除成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void showCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        Result result=new Result(Result.SUCCESS,cart,"展示购物车");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        int count = Integer.parseInt(request.getParameter("count"));
        Product product = productService.findById(pid);
        CartItem cartItem=new CartItem();
        cartItem.setCount(count);
        cartItem.setProduct(product);
        Cart cart=getCart(request);
        cart.addCart(cartItem);
        Result result=new Result(Result.SUCCESS,"","添加成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    private Cart getCart(HttpServletRequest request){
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart==null){
            session.setAttribute("cart",new Cart());
            cart = (Cart) session.getAttribute("cart");
        }
        return cart;
    }

}
