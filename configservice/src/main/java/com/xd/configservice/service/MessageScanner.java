package com.xd.configservice.service;

import com.xd.configservice.model.ReleaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: lixiaodong
 * @Date: 2020/6/14 下午7:33
 */

@Slf4j
public class MessageScanner implements InitializingBean {

    private ScheduledExecutorService executorService ;

    private List<ReleaseMessageListener> listeners;

    public MessageScanner(){
        listeners = new CopyOnWriteArrayList();
        executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("开始执行扫描任务");
        scanMessage();
    }

    private void scanMessage() {
        executorService.scheduleAtFixedRate(()->{
            log.info("扫描中");
            ReleaseMessage releaseMessage = new ReleaseMessage();
            releaseMessage.setMessage("成功");
            for (ReleaseMessageListener listener :listeners){
                listener.handleMessage(releaseMessage);
            }
        },1000,3000,TimeUnit.MILLISECONDS);
    }


    /**
     * add message listeners for release message
     * @param listener
     */
    public void addMessageListener(ReleaseMessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

}
