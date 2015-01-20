package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import org.junit.Before;
import org.junit.Test;

import com.github.zkclient.ZkClient;
import com.xdxing.plugins.elasticsearch.zkdiscovery.monitor.CSMonitor;
import com.xdxing.plugins.elasticsearch.zkdiscovery.monitor.CSMonitorImpl;

public class CSMonitorTest {
	
	ZkClient zkclient;
	String rest_host;
	CSMonitor monitor;
	
	@Before
	public void init() {
		zkclient = new ZkClient("192.168.84.154:2181,192.168.84.155:2181", 30 * 1000, 10 * 1000);
		rest_host = "http://192.168.84.156:9200";
		monitor = new CSMonitorImpl(rest_host, zkclient, "/estest");
	}
	
	@Test
	public void testRun() {
		monitor.start();
	}
}
