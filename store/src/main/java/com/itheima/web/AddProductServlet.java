package com.itheima.web;

import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import com.itheima.utils.UUIDUtils;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(urlPatterns = "/addProduct")
public class AddProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product =new Product();
        Map<String, String[]> map = getMap(request);
        try {
            BeanUtils.populate(product,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setPflag(0);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        product.setPdate(date);
        product.setPid(UUIDUtils.getUUID());
        ProductService productService=new ProductService();
        productService.addProduct(product);
        Result result=new Result(Result.SUCCESS,"","查询成功");
        response.getWriter().print(JSONObject.fromObject(result));
    }

    private Map<String,String[]> getMap(HttpServletRequest request){
        Map<String,String[]> map=new HashMap<>();
        try {
            DiskFileItemFactory factory=new DiskFileItemFactory();
            ServletFileUpload servletFileUpload=new ServletFileUpload(factory);
            List<FileItem> list = servletFileUpload.parseRequest(request);
            for (FileItem item : list) {
               if(item.isFormField()){
                   String name = item.getFieldName();
                   String value = item.getString("utf-8");
                   map.put(name,new String[]{value});
               }else{
                   String filename = item.getName();
                   String houzhui=filename.split("\\.")[1];
                   filename=UUIDUtils.getUUID()+"."+houzhui;
                   InputStream input = item.getInputStream();
                   String upload = ResourceBundle.getBundle("upload").getString("uploadDir");
                   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                   String date = sdf.format(new Date());
                   File file=new File(upload,date);

                   if (!file.exists()){
                       file.mkdirs();
                   }
                   FileOutputStream fos=new FileOutputStream(new File(file,filename));
                   IOUtils.copy(input,fos);
                   fos.close();
                   input.close();
                   item.delete();

                   map.put("pimage",new String[]{"resources/products/"+date+"/"+filename});
               }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return map;

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


}
