package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import java.util.Map;

public class CSMonitorClusterStateNode {

	private String id;
	
	private String name;
	
	private String transport_address;
	
	private String http_address;
	
	private String host;

	private String ip;
	
	private String version;
	
	private Map<String, Object> settings;
	

	public CSMonitorClusterStateNode() {
		super();
	}
	
	public CSMonitorClusterStateNode(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransport_address() {
		return transport_address;
	}

	public void setTransport_address(String transport_address) {
		this.transport_address = transport_address;
	}

	public String getHttp_address() {
		return http_address;
	}

	public void setHttp_address(String http_address) {
		this.http_address = http_address;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, Object> settings) {
		this.settings = settings;
	}
	
}
