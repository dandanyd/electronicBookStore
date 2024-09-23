package com.yindan.bookstore.stfp.factory;

import com.yindan.bookstore.stfp.strategy.Strategy;
import com.yindan.bookstore.stfp.strategy.strategyImpl.DownloadAndParseStrategy;
import com.yindan.bookstore.stfp.strategy.strategyImpl.SimpleUploadStrategy;
import org.springframework.stereotype.Component;

@Component
public class UploadAndDownloadStrategyFactory {

    //添加上传方式
    public static Strategy createUploadStrategy(String strategyType) {
        switch (strategyType) {
            //上传策略
            case "simple":
                return  new SimpleUploadStrategy();
            case "download":
                return  new DownloadAndParseStrategy();
            default:
                throw new IllegalArgumentException("Unsupported strategy type: " + strategyType);
        }
    }
}
