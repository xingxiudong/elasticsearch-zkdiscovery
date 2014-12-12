package com.xdxing.plugins.elasticsearch.zkdiscovery;

import java.util.Collection;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.rest.RestModule;
import org.elasticsearch.river.RiversModule;

import com.xdxing.plugins.elasticsearch.zkdiscovery.module.ZookeeperModule;
import com.xdxing.plugins.elasticsearch.zkdiscovery.module.ZookeeperRiverModule;
import com.xdxing.plugins.elasticsearch.zkdiscovery.rest.ZookeeperRestAction;
import com.xdxing.plugins.elasticsearch.zkdiscovery.service.ZookeeperService;

public class ZookeeperPlugin extends AbstractPlugin {
    @Override
    public String name() {
        return "ESZkdiscoveryPlugin";
    }

    @Override
    public String description() {
        return "This is a elasticsearch zookeeper discovery plugin.";
    }

    // for Rest API
    public void onModule(final RestModule module) {
        module.addRestAction(ZookeeperRestAction.class);
    }

    // for River
    public void onModule(final RiversModule module) {
        module.registerRiver("zookeeper", ZookeeperRiverModule.class);
    }

    // for Service
    @Override
    public Collection<Class<? extends Module>> modules() {
        final Collection<Class<? extends Module>> modules = Lists
                .newArrayList();
        modules.add(ZookeeperModule.class);
        return modules;
    }

    // for Service
    @SuppressWarnings("rawtypes")
    @Override
    public Collection<Class<? extends LifecycleComponent>> services() {
        final Collection<Class<? extends LifecycleComponent>> services = Lists
                .newArrayList();
        services.add(ZookeeperService.class);
        return services;
    }
}
