Zookeeper plugin for elasticsearch
===========================

功能：ElasticSearch可通过该插件自动向zookeeper汇报ElasticSearch Cluster中每一个节点状态插件

# 配置
	com.xdxing.plugins.elasticsearch.zkdiscovery:
	   enabled: true
	   servers: localhost:2181
	   sessionTimeout: 30000
	   connectionTimeout: 10000
	   path: /es

# 安装
bin/plugin --url file:./elasticsearch-zkdiscovery-1.0.zip --install zkdiscovery

# 卸载
bin/plugin --remove zkdiscovery

# DEMO

* Zookeeper数据treeview：
<pre>
	/es
	  |- cluster_name
	  |- nodes
	  		|- ephe0000000000
	  		|- ephe0000000001
	  		|- ephe0000000002
	  		|- ......
</pre>

* 一个节点data示例：
```JSON
{
    "id": "ikZacSqRQtmbzm0dglz5QA",
    "name": "es132",
    "hostname": "dbs",
    "http_address": "inet[/192.168.42.132:9200]",
    "transport_address": "inet[/192.168.42.132:9300]",
    "version": "1.2.1",
    "start_time": "2014-12-11 15:05:58",
    "settings": {
        "path.home": "/usr/local/elasticsearch",
        "foreground": "yes",
        "com.xdxing.plugins.elasticsearch.zkdiscovery.enabled": "true",
        "com.xdxing.plugins.elasticsearch.zkdiscovery.sessionTimeout": "30000",
        "index.analysis.analyzer.ik_smart.type": "ik",
        "script.disable_dynamic": "false",
        "cluster.name": "es_cluster",
        "com.xdxing.plugins.elasticsearch.zkdiscovery.servers": "localhost:2181",
        "index.analysis.analyzer.ik.type": "org.elasticsearch.index.analysis.IkAnalyzerProvider",
        "index.analysis.analyzer.ik.alias.0": "news_analyzer_ik",
        "index.analysis.analyzer.ik.alias.1": "ik_analyzer",
        "index.analysis.analyzer.ik_max_word.type": "ik",
        "com.xdxing.plugins.elasticsearch.zkdiscovery.path": "/es",
        "node.name": "es132",
        "com.xdxing.plugins.elasticsearch.zkdiscovery.connectionTimeout": "10000",
        "index.analysis.analyzer.ik_smart.use_smart": "true",
        "index.analysis.analyzer.ik_max_word.use_smart": "false",
        "index.analysis.analyzer.default.type": "ik",
        "name": "es132",
        "path.logs": "/usr/local/elasticsearch/logs"
    },
    "__CURRENT_CONNECT_TIME": "2014-12-11 15:05:59",
    "__RECONNECT_COUNT": 0,
    "__HOST_NAME": "dbs",
    "__HOST_ADDRESS": "127.0.0.1",
    "__UUID": "a826728c-a41d-4fbe-93c7-9064e01efb7a"
}
```
