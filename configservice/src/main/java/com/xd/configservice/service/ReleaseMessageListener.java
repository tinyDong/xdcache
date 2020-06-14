package com.xd.configservice.service;

import com.xd.configservice.model.ReleaseMessage;

/**
 * @Author: lixiaodong
 * @Date: 2020/6/14 下午7:12
 */
public interface ReleaseMessageListener {
    void handleMessage(ReleaseMessage message);
}
