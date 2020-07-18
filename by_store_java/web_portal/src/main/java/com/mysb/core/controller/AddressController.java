package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.address.Address;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    @RequestMapping("/findByUsername")
    public PageResult findByUsername(String username, Integer page, Integer rows) {
        PageResult byUsername = addressService.findByUsername(page, rows, username);
        return byUsername;
    }

    @RequestMapping("/saveAddress")
    public Result saveAddress(String username, @RequestBody Address address) {
        try {
            addressService.saveAddress(username, address);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    @RequestMapping("/updateAddress")
    public Result updateAddress(@RequestBody Address address) {
        try {
            addressService.updateAddress(address);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }
    @RequestMapping("/deleteAddress")
    public Result deleteAddress(Long id) {
        try {
            addressService.deleteAddress(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/findById")
    public Address findById(Long id) {
        return addressService.findById(id);
    }

    @RequestMapping("/setDefault")
    public Result setDefault(String username, Long id) {
        try {
            addressService.setDefault(username, id);
            return new Result(true, "设置成功");
        } catch (Exception e) {
            return new Result(false, "设置失败");
        }
    }
}
