package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.mysb.core.pojo.entry.BuyerCart;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.CartService;
import com.mysb.core.utils.Constants;
import com.mysb.core.utils.CookieUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @RequestMapping("/addGoodsToCartList")
    public Result addCartToCartList(Long itemId, Integer num, String username,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取购物车列表
            List<BuyerCart> cartList = this.findCartList(username, request, response);
            //当前商品加入购物车列表
            cartList = cartService.addItemToCartList(cartList, itemId, num);
            //判断当前用户是否登录
            if ("null".equals(username) || username == null) {
                //如果没登录,则将购物车存放到cookie
                CookieUtil.setCookie(request, response,
                        Constants.BY_CART_LIST_COOKIE, JSON.toJSONString(cartList));
            } else {
                //登录则保存在redis
                cartService.setCartListToRedis(username, cartList);
            }
            return new Result(true, "添加成功");
        } catch (Exception e) {
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/findCartList")
    public List<BuyerCart> findCartList(String username,
                                        HttpServletRequest request, HttpServletResponse response) {
        //从cookie中获取购物车列表json格式字符串
        String cookieValue = CookieUtil.getCookieValue(request,
                Constants.BY_CART_LIST_COOKIE, "utf-8");
        //如果购物车列表json串为空则返回"[]"
        if (cookieValue == null || "".equals(cookieValue)) {
            cookieValue = "[]";
        }
        //将购物车列表json转换成对象
        List<BuyerCart> cartList = JSON.parseArray(cookieValue, BuyerCart.class);
        if ("null".equals(username) || username == null) {
            //未登录,返回cookie中的购物车列表对象
            System.out.println("cookie" + cookieValue);
            return cartList;
        } else {
            //已登录 从redis当中获取购物车列表对象
            List<BuyerCart> redisCartList = cartService.getCartListFromRedis(username);
            //判断cookie是否存在购物车列表
            if (cartList.size() > 0) {
                //如果cookie中存在购物车列表则和redisz中的购物车列表合并城一个对象
                redisCartList = cartService.mergeCookieCartListToRedisCartList(cartList, redisCartList);
                //删除cookie中购物车列表
                CookieUtil.deleteCookie(request, response, Constants.BY_CART_LIST_COOKIE);
                //将合并后的购物车列表存入redis中
                cartService.setCartListToRedis(username, redisCartList);
            }
            //返回购物车列表对象
            System.out.println("redis+" + redisCartList);
            return redisCartList;
        }

    }

    @RequestMapping("/deleteOneCartList")
    public Result deleteOneCartList(Long itemId, String username,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            //判断当前用户是否登录
            if ("null".equals(username) || username == null) {
                //如果没登录,则删除cookie
                return new Result(true, "请先登录后,再进行操作哦",false);
            } else {
                //登录则删除redis
                cartService.deleteOneCart(itemId,username);
            }
            return new Result(true, "删除成功",true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败",true);
        }
    }

    @RequestMapping("/deleteCartList")
    public Result deleteCartList(@RequestBody Map<String,List<Long>> reqMap, String username,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            //判断当前用户是否登录
            if ("null".equals(username) || username == null) {
                //如果没登录,则删除cookie
                return new Result(true, "请先登录后,再进行操作哦",false);
            } else {
                //登录则删除redis
                cartService.deleteCart(reqMap,username);
            }
            return new Result(true, "删除成功",true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败",true);
        }
    }


    @RequestMapping("/updateByNumCartList")
    public Result updateByNumCartList(Integer num,String username,Long itemId,HttpServletRequest request, HttpServletResponse response){
       try {
           //获取购物车列表
           List<BuyerCart> cartList = this.findCartList(username, request, response);
           //更新购物车
           List<BuyerCart> newCartList = cartService.updateByNumCartList(num,itemId,cartList);
           if(newCartList == null){
               return new Result(true, "该商品已下架,不能购买~");
           }
           //重新保存
           cartService.setCartListToRedis(username, newCartList);
           return new Result(true, "更新成功");
       }catch (Exception e){
           return new Result(false, "更新失败");
       }
    }

    @RequestMapping("/clearShop")
    public Result clearShop(String username,HttpServletRequest request, HttpServletResponse response){
       try {
           //获取购物车列表
           List<BuyerCart> cartList = this.findCartList(username, request, response);
           cartService.clearShop(cartList, username);
           return new Result(true, "已清除下架商品");
       }catch (Exception e){
           return new Result(false, "清除下架商品失败");
       }
    }
}
