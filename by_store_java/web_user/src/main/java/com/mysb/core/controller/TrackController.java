package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.TrackService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/track")
@RestController
public class TrackController {
    @Reference
    private TrackService trackService;

    @RequestMapping("/findPage")
    public PageResult findPage(Integer page,Integer pageSize,String username){
        return trackService.findPage(page,pageSize,username);
    }

    @RequestMapping("/deleteTrack")
    public Result deleteTrack(Long id){
        try{
            trackService.deleteTrack(id);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/deleteAll")
    public Result deleteAll(String username){
        try{
            trackService.deleteAll(username);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/deleteSelect")
    public Result deleteSelect(String username, @RequestBody Map<String, List<Long>> map){
        try{
             trackService.deleteSelect(username,map);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

}
