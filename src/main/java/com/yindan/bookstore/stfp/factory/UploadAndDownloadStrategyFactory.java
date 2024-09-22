package com.yindan.bookstore.stfp.factory;

import com.yindan.bookstore.stfp.strategy.Strategy;
import com.yindan.bookstore.stfp.strategy.strategyImpl.DownloadAndParseStrategy;
import com.yindan.bookstore.stfp.strategy.strategyImpl.SimpleUploadStrategy;

public class UploadAndDownloadStrategyFactory {

    //添加上传方式
    public static <T> Strategy<T> createUploadStrategy(String strategyType) {
        switch (strategyType) {
            //上传策略
            case "simple":
                return  (Strategy<T>) new SimpleUploadStrategy();
            case "download":
                return  (Strategy<T>) new DownloadAndParseStrategy();
            default:
                throw new IllegalArgumentException("Unsupported strategy type: " + strategyType);
        }
    }
}
