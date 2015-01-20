package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import java.util.ArrayList;
import java.util.List;

public class CSMonitorClusterState {

	private String cluster_name;
	
	private String master_node;
	
	private List<CSMonitorClusterStateNode> nodes;

	public String getCluster_name() {
		return cluster_name;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

	public String getMaster_node() {
		return master_node;
	}

	public void setMaster_node(String master_node) {
		this.master_node = master_node;
	}

	public List<CSMonitorClusterStateNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<CSMonitorClusterStateNode> nodes) {
		this.nodes = nodes;
	}

	public void appendNodes(CSMonitorClusterStateNode node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList<CSMonitorClusterStateNode>();
		}
		this.nodes.add(node);
	}
	
}
