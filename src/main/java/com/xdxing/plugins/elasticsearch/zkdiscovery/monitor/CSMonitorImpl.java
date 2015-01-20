package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import com.github.zkclient.IZkStateListener;
import com.github.zkclient.ZkClient;
import com.xdxing.plugins.utils.JsonUtil;

public class CSMonitorImpl implements CSMonitor, Observer, IZkStateListener {
	
	private ESLogger logger = Loggers.getLogger(this.getClass());
	
	private String root = "/es";
	
	private String rest_host = "http://localhost:9200";
	
	private ZkClient zkclient;
	
	private boolean running = false;

	private CSMonitorClusterState lastClusterState;
	
	public CSMonitorImpl(String rest_host, ZkClient zkclient) {
		this.zkclient = zkclient;
		this.rest_host = rest_host;
	}
	
	/**
	 * 
	 * @param rest_host: es node rest address, eg: http://192.168.84.154
	 * @param root: zookeeper root path to save es report data
	 * @param zkclient: zookeeper client
	 */
	public CSMonitorImpl(String rest_host, ZkClient zkclient, String root) {
		this.zkclient = zkclient;
		this.rest_host = rest_host;
		this.root = root;
	}

	@Override
	public void run() {
		if (!running) {
			this.running = true;
			this.watching(this);
			logger.info("ElasticSearch cluster monitor is running...");
		}
	}
	
	@Override
	public void watching(final Observer clusterStateObserver) {
		
		// 监测集群状态
		new Runnable() {
			
			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(1000 * 3);
					} catch (InterruptedException e) {
					}
					
					boolean master = ClusterStateUtils.localIsMaster(rest_host);
					if (!master) {
						// 只有Master节点负责收集数据
						logger.trace("Scanning... Nothing done, because you are not a master.");
						continue;
					}
					
					// 通过本地http地址获取集群状态
					CSMonitorClusterState clusterState = ClusterStateUtils.getClusterState(rest_host);
					if (isUpdated(clusterState)) {
						logger.info("A change cluster state was found on by watcher. Next, update cluster state go on.");
						clusterStateObserver.update(null, clusterState);
					}
				}
			}
			
		}.run();
		
	}

	@Override
	public void update(Observable o, Object newClusterState) {
		this.publish((CSMonitorClusterState)newClusterState);
	}

	@Override
	public boolean publish(CSMonitorClusterState clusterState) {
		String root_nodes = root + "/nodes";
		// TODO: 这里没有做事务
		// clear data
		if (this.zkclient.exists(root)) {
			this.zkclient.deleteRecursive(root);
		}
		this.zkclient.createPersistent(root_nodes, true);
		
		this.zkclient.createPersistent(root + "/cluster_name", clusterState.getCluster_name().getBytes());
		this.zkclient.createPersistent(root + "/master_node", clusterState.getMaster_node().getBytes());
		
		// zookeeper 写临时节点数据
		for (CSMonitorClusterStateNode n : clusterState.getNodes()) {
			if (null != n) {
				boolean n_is_master = n.getId().equals(clusterState.getMaster_node());
				String path = String.format("%s/%s", root_nodes, StringUtils.substringBetween(n.getHttp_address(), "/", ":")) 
						+ (n_is_master ? "[Master]" : "");

				this.zkclient.createEphemeral(path, JsonUtil.serialize(n).getBytes());
				
			}
		}
		logger.info("Cluster[{}] state published...", clusterState.getCluster_name());
		return true;
	}
	
	@Override
	public boolean isUpdated(CSMonitorClusterState currentClusterState) {
		if (null == currentClusterState) {
			return false;
		}
		
		boolean isUpdated = false;
		if (null == lastClusterState) {
			isUpdated = true;
		} else {
			isUpdated = lastClusterState.getNodes().size() != currentClusterState.getNodes().size();
		}
		lastClusterState = currentClusterState;
		return isUpdated;
	}

	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		if (state == KeeperState.Disconnected) {
			// this.publish(lastClusterState); // 发布最近一次的集群状态
		}
	}

	@Override
	public void handleNewSession() throws Exception {
		logger.warn("Handle a new session.");
		this.publish(lastClusterState); // 发布最近一次的集群状态
	}

	@Override
	public void start() {
		this.run();
	}

	@Override
	public void stop() {
		this.running = false;
	}

}
