package com.xdxing.plugins.elasticsearch.zkdiscovery.module;

import org.elasticsearch.common.inject.AbstractModule;

import com.xdxing.plugins.elasticsearch.zkdiscovery.service.ZookeeperService;

public class ZookeeperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ZookeeperService.class).asEagerSingleton();
    }
}