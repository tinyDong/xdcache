package com.xd.configservice.controller;

import com.xd.configservice.model.ReleaseMessage;
import com.xd.configservice.service.ReleaseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: lixiaodong
 * @Date: 2020/6/14 下午4:11
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class LongPullController implements ReleaseMessageListener {


    Map<String,List<DeferredResult>> keyMap = new ConcurrentHashMap<>();

    @RequestMapping("/addMsg")
    public String longPullIngTest(@RequestParam("key") String key){

        return "success";
    }


    @RequestMapping("/subscribe")
    public DeferredResult<String> subscribeConfig(@RequestParam("key") String key){

        DeferredResult deferredResult = new DeferredResult();
        if (keyMap.containsKey(key)){
            keyMap.get(key).add(deferredResult);
        }else {
            List<DeferredResult> deferredResults = new ArrayList<>();
            deferredResults.add(deferredResult);
            keyMap.put(key,deferredResults);
        }

        deferredResult.onTimeout(()->{
            log.info("超时");
        });
        deferredResult.onCompletion(()->{
            keyMap.get("123").remove(deferredResult);
            log.info("完成");
        });
        return deferredResult;
    }

    @Override
    public void handleMessage(ReleaseMessage message) {
        log.info("处理消息中");
        if (keyMap.size() == 0){
            return;
        }
        List<DeferredResult> deferredResults = keyMap.get("123");
        log.info("DeferredResult = {}",deferredResults);

        for (DeferredResult deferredResult : deferredResults){
            log.info("开始循环");
            deferredResult.setResult(message.getMessage());
        }
    }
}
