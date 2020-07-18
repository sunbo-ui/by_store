package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.track.Track;
import com.mysb.core.service.TrackService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/track")
@RestController
public class TrackController {
    @Reference
    private TrackService trackService;

    @RequestMapping("/saveTrack")
    public Result saveTrack(@RequestBody Track track){
        try{
            Track newTrack = new Track();
            newTrack.setGoodsId(track.getGoodsId());
            newTrack.setPicUrl(track.getPicUrl());
            newTrack.setUserId(track.getUserId());
            newTrack.setTitle(track.getTitle());
            newTrack.setPrice(track.getPrice());
            newTrack.setCreateTime(new Date());
            System.out.println(newTrack);
            trackService.saveTrack(newTrack);
            return new Result(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }


}
