package com.xdxing.plugins.elasticsearch.zkdiscovery;

/**
 * ElasticSearch Zookeeper 插件配置
 *
 * @date 2014年6月19日 上午10:33:37
 */
public class ZookeeperConfig {

	/**
	 * 是否开启ElasticSearch Zookeeper 插件支持
	 */
	private Boolean enabled = false;
	
	/**
	 * Zookeeper Server 连接字符串，如：192.168.1.100:8161,192.168.1.101:8161,...
	 */
	private String connectString;
	
	/**
	 * 客户端与Zookeeper server连接超时时间
	 */
	private Integer connectionTimeout = 1000 * 10;
	
	/**
	 * 客户端与Zookeeper server保持心跳时间
	 */
	private Integer sessionTimeout = 1000 * 5;
	
	/**
	 * ElasticSearch Zookeeper 插件在Zookeeper上汇报节点路径，以"/"开头
	 */
	private String path = "/es";

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String servers) {
		this.connectString = servers;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ZookeeperConfig [enabled=" + enabled + ", servers=" + connectString + ", connectionTimeout="
				+ connectionTimeout + ", sessionTimeout=" + sessionTimeout + ", path=" + path + "]";
	}
	
}
