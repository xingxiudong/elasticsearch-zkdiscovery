package com.xdxing.plugins.elasticsearch.zkdiscovery.service;

//import org.elasticsearch.ElasticsearchException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.collect.ImmutableSet;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.service.NodeService;

import com.github.zkclient.ZkClient;
import com.xdxing.plugins.elasticsearch.zkdiscovery.ZookeeperConfig;
import com.xdxing.plugins.utils.JsonUtil;
import com.xdxing.plugins.zkdiscovery.ZkClientDiscovery;

public class ZookeeperService extends AbstractLifecycleComponent<ZookeeperService> {
	
	private Settings settings;
	private ClusterName clusterName;
	private NodeService nodeService;
	private boolean send = false;
	
	private ZookeeperConfig zookeeperConfig;
	private ZkClient zkClient;
	
    private ZookeeperConfig getZookeeperConfig(Settings settings) {
        String enabled = settings.get("com.xdxing.plugins.elasticsearch.zkdiscovery.enabled");
        String servers = settings.get("com.xdxing.plugins.elasticsearch.zkdiscovery.servers");
        String connectionTimeout = settings.get("com.xdxing.plugins.elasticsearch.zkdiscovery.connectionTimeout");
        String sessionTimeout = settings.get("com.xdxing.plugins.elasticsearch.zkdiscovery.sessionTimeout");
        String path = settings.get("com.xdxing.plugins.elasticsearch.zkdiscovery.path");
        
        if (StringUtils.isBlank(servers)) {
        	logger.error("The arguments servers can't be empty. You must set it.");
        	return null;
        }
        
        ZookeeperConfig conf = new ZookeeperConfig();
        
        conf.setConnectString(servers);
        if(StringUtils.isNotBlank(enabled)) {
        	conf.setEnabled(Boolean.valueOf(enabled.trim()));
        }
        if(StringUtils.isNotBlank(connectionTimeout)) {
        	conf.setConnectionTimeout(Integer.valueOf(connectionTimeout.trim()));
        }
        if(StringUtils.isNotBlank(sessionTimeout)) {
        	conf.setSessionTimeout(Integer.valueOf(sessionTimeout.trim()));
        }
        if(StringUtils.isNotBlank(path)) {
        	conf.setPath(path);
        }
        
        logger.info("ZookeeperConfig: " + conf.toString());
    	return conf;
    }
    
    public void initZkClient() {
    	if (null == this.zookeeperConfig || !this.zookeeperConfig.getEnabled()) {
        	logger.error("ZkClient initialize failure, because zookeeper config is none, or it's not enabled.");
        	return;
        }
    	if (this.zkClient != null) {
    		return;
    	}
    	this.zkClient = new ZkClient(
    			this.zookeeperConfig.getConnectString(), 
    			this.zookeeperConfig.getConnectionTimeout(), 
    			this.zookeeperConfig.getSessionTimeout());
    }
    
    public void initAndSendToZooKeeper(String json) {
    	if (null == this.zkClient) {
    		return;
    	}
    	json = null == json ? "" : json;
    	
    	String root = this.zookeeperConfig.getPath().trim();
    	if (StringUtils.endsWith(root, "/")) {
    		root = StringUtils.substringBeforeLast(root, "/");
    	}
    	String nodes_path = String.format("%s/nodes", root);
    	String cluster_name_path = String.format("%s/cluster_name", root);
    	
    	if (!this.zkClient.exists(root)) {
    		this.zkClient.createPersistent(root, true);
    	}
    	if (!this.zkClient.exists(nodes_path)) {
    		this.zkClient.createPersistent(nodes_path);
    	}
    	if (!this.zkClient.exists(cluster_name_path)) {
    		this.zkClient.createPersistent(cluster_name_path);
    	}
    	
    	if (null != clusterName) {
    		this.zkClient.writeData(cluster_name_path, clusterName.value().getBytes());
    	}
    	this.zkClient.writeData(nodes_path, "".getBytes());
    	
    	// 发送临时节点到Zookeeper
    	Map<String, Object> dataMap = JsonUtil.deserialize(json, new TypeReference<Map<String, Object>>() {{}
		});
		ZkClientDiscovery disc = ZkClientDiscovery.getInstance();
		disc.setZkClient(zkClient);
		disc.setParentPath(nodes_path);
		disc.setData(dataMap);
		disc.setCallback(null);
		disc.run();
    }
	
    @Inject
    public ZookeeperService(Settings settings, ClusterName clusterName, NodeService nodeService) {
        super(settings);
        logger.info("CREATE ZookeeperService");
        
        this.settings = settings;
        this.clusterName = clusterName;
        this.nodeService = nodeService;
        
        this.zookeeperConfig = getZookeeperConfig(this.settings);
    }

    @Override
    protected void doStart() {//throws ElasticsearchException {
        logger.info("START ZookeeperService");
        initZkClient();

        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!send) {
						send();
						Thread.sleep(1000);
					}
				}
				catch (Exception e) {
					logger.error("", e);
				}
			}
		}).start();
    }
    
    private void send() {
    	if (nodeService.info() == null 
    			|| nodeService.info().getNode() == null
    			|| nodeService.info().getHttp() == null
    			|| nodeService.info().getHttp().address() == null
    			|| nodeService.info().getHttp().address().publishAddress() == null
    			|| nodeService.info().getTransport() == null
    			|| nodeService.info().getTransport().address() == null
    			|| nodeService.info().getTransport().address().publishAddress() == null
    			|| nodeService.info().getVersion() == null) {
    		return;
    	}
    	
        try {
        	String start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        String cluster_name= clusterName.value();
	        String id = nodeService.info().getNode().getId();
	        String name = nodeService.info().getNode().getName();
	        String hostname= nodeService.info().getHostname();
	        String http_address = nodeService.info().getHttp().address().publishAddress().toString();
	        String transport_address = nodeService.info().getTransport().address().publishAddress().toString();
	        String version = nodeService.info().getVersion().toString();

	        String s = "";
	        ImmutableSet<Entry<String, String>> set = nodeService.info().getSettings().getAsMap().entrySet();
	        for (Entry<String, String> element : set) {
	        	String key = parseJson(element.getKey());
	        	String value = parseJson(element.getValue());
	        	if ("".equals(s)) {
	        		s = "\"" + key + "\": \"" + value + "\"";
	        	} else {
	        		s += ", \"" + key + "\": \"" + value + "\"";
	        	}
	        }
	        
	        String json = "{\"id\": \"%s\", " +
	        		"\"name\": \"%s\", " +
	        		"\"hostname\": \"%s\", " +
	        		"\"http_address\": \"%s\", " +
	        		"\"transport_address\": \"%s\", " +
	        		"\"version\": \"%s\", " +
	        		"\"start_time\": \"%s\", " +
	        		"\"settings\": {%s}}";
	        json = String.format(json, 
	        		parseJson(id), 
	        		parseJson(name),
	        		parseJson(hostname),
	        		parseJson(http_address),
	        		parseJson(transport_address),
	        		parseJson(version), 
	        		start_time,
	        		s);
	        
	        logger.info("cluster_name: " + cluster_name);
	        logger.info("info: " + json);
	        send = true;
	        
	        initAndSendToZooKeeper(json);
        }
        catch (Exception e) {
        	logger.error("", e);
        }
    }

    @Override
    protected void doStop() {//throws ElasticsearchException {
        logger.info("STOP ZookeeperService");

        if (null != this.zkClient) {
        	this.zkClient.close();
        }
    }

    @Override
    protected void doClose() {//throws ElasticsearchException {
        logger.info("CLOSE ZookeeperService");

        if (null != this.zkClient) {
        	this.zkClient.close();
        }
    }
    
    private String parseJson(String s) {
    	return s.replaceAll("\\\"", "\\\\\\\"");
    }
}