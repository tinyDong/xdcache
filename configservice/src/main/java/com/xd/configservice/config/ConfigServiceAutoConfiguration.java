package com.xd.configservice.config;

import com.xd.configservice.controller.LongPullController;
import com.xd.configservice.service.MessageScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lixiaodong
 * @Date: 2020/6/14 下午7:44
 */
@Configuration
public class ConfigServiceAutoConfiguration {

    @Configuration
    static class MessageScannerConfiguration {
        private final LongPullController longPullController;

        public MessageScannerConfiguration(
                final LongPullController longPullController){
            this.longPullController = longPullController;
        }

        @Bean
        public MessageScanner messageScanner() {
            MessageScanner releaseMessageScanner = new MessageScanner();
            releaseMessageScanner.addMessageListener(longPullController);
            return releaseMessageScanner;
        }
    }

}
