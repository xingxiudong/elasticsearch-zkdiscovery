package com.xdxing.plugins.elasticsearch.zkdiscovery.module;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.river.River;

import com.xdxing.plugins.elasticsearch.zkdiscovery.river.ZookeeperRiver;

public class ZookeeperRiverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(River.class).to(ZookeeperRiver.class).asEagerSingleton();
    }
}
