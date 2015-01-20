package com.xdxing.plugins.elasticsearch.zkdiscovery.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;

import com.xdxing.plugins.utils.JsonUtil;

public class ClusterStateUtils {
	
	public static CSMonitorClusterStateNode getLocalNode(final String rest) {
		List<CSMonitorClusterStateNode> nodes = getNodes(rest, "/_nodes/_local/nodes");
		return null == nodes || nodes.size() == 0 ? null : nodes.get(0);
	}
	
	public static List<CSMonitorClusterStateNode> getAllNodes(final String rest) {
		return getNodes(rest, "/_nodes/http");
	}

	@SuppressWarnings("unchecked")
	public static List<CSMonitorClusterStateNode> getNodes(final String rest, final String uri) {
		List<CSMonitorClusterStateNode> nodes = new ArrayList<CSMonitorClusterStateNode>();
		
		String source = httpGet(rest + uri);
		Map<String, Object> __map = JsonUtil.deserialize(source, new TypeReference<Map<String, Object>>(){});
		if (null != __map) {
			Map<String, Object> _nodes = (Map<String, Object>) __map.get("nodes");
			for (String key : _nodes.keySet()) {
				Map<String, String> node = (Map<String, String>) _nodes.get(key);
				
				CSMonitorClusterStateNode _node = new CSMonitorClusterStateNode(key);
				_node.setName(node.get("name"));
				_node.setHost(node.get("host"));
				_node.setIp(node.get("ip"));
				_node.setHttp_address(node.get("http_address"));
				_node.setTransport_address(node.get("transport_address"));
				_node.setVersion(node.get("version"));
				
				nodes.add(_node);
			}
		}
		return nodes;
	}

	public static CSMonitorClusterState getClusterState(final String rest) {
		CSMonitorClusterState clusterState = new CSMonitorClusterState();
		
		String source = httpGet(rest + "/_cluster/state/master_node");
		Map<String, String> cluster_state = JsonUtil.deserialize(source, new TypeReference<Map<String, String>>(){});
		if (null != cluster_state) {
			clusterState.setCluster_name(cluster_state.get("cluster_name"));
			clusterState.setMaster_node(cluster_state.get("master_node"));
			clusterState.setNodes(getAllNodes(rest));
		}
		return clusterState;
	}

	public static boolean localIsMaster(final String rest) {
		CSMonitorClusterState clusterState = getClusterState(rest);
		CSMonitorClusterStateNode localNode = getLocalNode(rest);
		return StringUtils.equals(localNode.getId(), clusterState.getMaster_node());
	}
	
	public static String httpGet(final String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
		}
		return getHtmlContent(u, "UTF-8");
	}
	
	public static String getHtmlContent(URL url, String encode) {
		StringBuffer contentBuffer = new StringBuffer();

		int responseCode = -1;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// IE代理进行下载
			con.setConnectTimeout(60000);
			con.setReadTimeout(60000);
			// 获得网页返回信息码
			responseCode = con.getResponseCode();
			if (responseCode == -1) {
				System.out.println(url.toString() + " : connection is failure...");
				con.disconnect();
				return null;
			}
			if (responseCode >= 400) // 请求失败
			{
				System.out.println("请求失败:get response code: " + responseCode);
				con.disconnect();
				return null;
			}

			InputStream inStr = con.getInputStream();
			InputStreamReader istreamReader = new InputStreamReader(inStr, encode);
			BufferedReader buffStr = new BufferedReader(istreamReader);

			String str = null;
			while ((str = buffStr.readLine()) != null)
				contentBuffer.append(str);
			inStr.close();
		} catch (IOException e) {
			e.printStackTrace();
			contentBuffer = null;
			System.out.println("error: " + url.toString());
		} finally {
			con.disconnect();
		}
		return contentBuffer.toString();
	}
}
