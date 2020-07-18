package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    private SellerService sellerService;

    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer rows, @RequestBody Seller seller){
        return sellerService.findPage(page,rows,seller,0l);
    }

    @RequestMapping("/findPageStatus")
    public PageResult findPageStatus(Integer page, Integer rows, @RequestBody Seller seller){
        return sellerService.findPage(page,rows,seller,1l);
    }

    @RequestMapping("/findByStatus")
    public List<Seller> findByStatus(Long status){
        return sellerService.findByStatus(status);
    }

    @RequestMapping("/findById")
    public Seller findById(String sellerId){
        return sellerService.findById(sellerId);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId, String status){
        try {
            sellerService.updateStatus(sellerId,status);
            return new Result(true,"修改状态成功");
        }catch (Exception e){
            return new Result(false,"修改状态失败");
        }
    }
}
