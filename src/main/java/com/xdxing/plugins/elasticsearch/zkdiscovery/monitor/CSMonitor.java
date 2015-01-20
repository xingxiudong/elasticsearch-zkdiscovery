package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import java.util.Observer;

/**
 * ES集群状态监控器
 * @author xiudong
 *
 */
public interface CSMonitor extends Runnable {

	/**
	 * 观察ES集群状态
	 */
	void watching(Observer clusterStateObserver);
	
	/**
	 * 发布新的ES集群状态
	 */
	boolean publish(CSMonitorClusterState clusterState);
	
	/**
	 * ES集群状态是否发生了变更
	 */
	boolean isUpdated(CSMonitorClusterState newClusterState);

	/**
	 * 启动
	 */
	void start();

	/**
	 * 关闭
	 */
	void stop();
}
