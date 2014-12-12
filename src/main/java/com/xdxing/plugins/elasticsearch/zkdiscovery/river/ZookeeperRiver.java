package com.xdxing.plugins.elasticsearch.zkdiscovery.river;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.river.AbstractRiverComponent;
import org.elasticsearch.river.River;
import org.elasticsearch.river.RiverName;
import org.elasticsearch.river.RiverSettings;

public class ZookeeperRiver extends AbstractRiverComponent implements River {
    private final Client client;

    private ZookeeperRiverLogic riverLogic;

    @Inject
    public ZookeeperRiver(final RiverName riverName, final RiverSettings settings,
            final Client client) {
        super(riverName, settings);
        this.client = client;

        logger.info("CREATE ZookeeperRiver");

        // TODO Your code..

    }

    @Override
    public void start() {
        logger.info("START ZookeeperRiver");

        riverLogic = new ZookeeperRiverLogic();
        new Thread(riverLogic).start();
    }

    @Override
    public void close() {
        logger.info("CLOSE ZookeeperRiver");

        // TODO Your code..
    }

    private class ZookeeperRiverLogic implements Runnable {

        @Override
        public void run() {
            logger.info("START ZookeeperRiverLogic: " + client.toString());

            // TODO Your code..
        }
    }
}
