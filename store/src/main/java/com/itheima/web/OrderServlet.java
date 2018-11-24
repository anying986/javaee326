package com.itheima.web;

import com.itheima.domain.*;
import com.itheima.service.OrderService;
import com.itheima.utils.PaymentUtil;
import com.itheima.utils.UUIDUtils;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends BaseServlet {
    private OrderService orderService=new OrderService();

    public void callback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取支付平台,支付成功后返回的数据
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");
        //返回是唯一性密钥
        String hmac = request.getParameter("hmac");
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");
        //verifyCallback验证方法,返回的所有参数,和密码再次计算对比
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (isValid) {
            // 响应数据有效
            if (r9_BType.equals("1")) {
                // 浏览器重定向
                request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");

            } else if (r9_BType.equals("2")) {
                // 服务器点对点 --- 支付公司通知你
                System.out.println("付款成功！222");
                // 修改订单状态 为已付款
                // 回复支付公司
                response.getWriter().print("success");
            }

            //修改订单状态
            //修改的付款状态,改成1,已经付款,订单编号
            orderService.updateState(r6_Order);

        } else {
            // 数据无效
            System.out.println("数据被篡改！");
        }
        //转发: 支付成功提示success.jsp
        request.getRequestDispatcher("/success.jsp").forward(request,response);
    }


    public void pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String address=request.getParameter("address");//收货地址
        String name=request.getParameter("name");//收货人
        String telephone=request.getParameter("telephone");//联系电话
        String oid=request.getParameter("oid");//订单编号

        User user =(User) request.getSession().getAttribute("user");
        //通过id获取order
        // 修改订单数据,修改订单的数据
        Orders order = orderService.orderById(user.getUid(),oid);

        order.setAddress(address);
        order.setName(name);
        order.setTelephone(telephone);

        //更新order
        orderService.updateOrder(order);


        // 组织发送支付公司需要哪些数据
        String pd_FrpId = request.getParameter("pd_FrpId");//银行校验码
        String p0_Cmd = "Buy";
        //获取商户唯一编号
        String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
        //订单编号
        String p2_Order = oid;
        //支付金额order.getTotal()+""
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        // 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
        // 第三方支付可以访问网址
        String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        // 加密hmac 需要密钥
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);


        //发送给第三方
        StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
        sb.append("p0_Cmd=").append(p0_Cmd).append("&");
        sb.append("p1_MerId=").append(p1_MerId).append("&");
        sb.append("p2_Order=").append(p2_Order).append("&");
        sb.append("p3_Amt=").append(p3_Amt).append("&");
        sb.append("p4_Cur=").append(p4_Cur).append("&");
        sb.append("p5_Pid=").append(p5_Pid).append("&");
        sb.append("p6_Pcat=").append(p6_Pcat).append("&");
        sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
        sb.append("p8_Url=").append(p8_Url).append("&");
        sb.append("p9_SAF=").append(p9_SAF).append("&");
        sb.append("pa_MP=").append(pa_MP).append("&");
        sb.append("pd_FrpId=").append(pd_FrpId).append("&");
        sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
        sb.append("hmac=").append(hmac);
        // System.out.println(sb);
        //页面调转到第三方平台支付页面
        //response.sendRedirect(sb.toString());
        Result re = new Result(Result.SUCCESS,sb.toString(),"支付");
        response.getWriter().print(JSONObject.fromObject(re));
    }

    public void orderById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            Result result=new Result(Result.NOLOGIN,"","未登录");
            response.getWriter().print(JSONObject.fromObject(result));
            return;
        }
        Orders orders = orderService.orderById(user.getUid(), oid);
        Result result=new Result(Result.SUCCESS,orders,"查询成功");
        response.getWriter().print(JSONObject.fromObject(result));

    }

    public void orderByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            Result result=new Result(Result.NOLOGIN,"","未登录");
            response.getWriter().print(JSONObject.fromObject(result));
            return;
        }
        PageBean<Orders> pageBean = orderService.orderByPage(user.getUid(), currentPage, pageSize);
        Result result=new Result(Result.SUCCESS,pageBean,"查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            Result result=new Result(Result.NOLOGIN,"","未登录");
            response.getWriter().print(JSONObject.fromObject(result));
            return;
        }

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart.getMap().size()==0){
            Result result=new Result(Result.FAILURE,"","购物车是空的");
            response.getWriter().print(JSONObject.fromObject(result));
            return;
        }

        Orders orders=new Orders();
        String oid = UUIDUtils.getUUID();
        orders.setOid(oid);
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(new Date());
        orders.setOrdertime(s);
        orders.setTotal(cart.getTotal());
        orders.setState(Constant.ORDEDRS_WEIFUKUAN);
        orders.setUid(user.getUid());


        Collection<CartItem> list = cart.getMap();
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartItem cartItem : list) {
            OrderItem orderItem=new OrderItem();
            orderItem.setCount(cartItem.getCount());
            orderItem.setOid(oid);
            orderItem.setPid(cartItem.getProduct().getPid());
            orderItem.setSubTotal(cartItem.getSubTotal());
            orderItemList.add(orderItem);
        }

        orderService.submitOrder(orders,orderItemList);

        cart.clearCart();
        Result result=new Result(Result.SUCCESS,oid,"订单生成成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

}
