# ElasticSearch ZKDiscovery2.1

—— Zookeeper plugin for elasticsearch
------------


## Features

ElasticSearch可通过该插件自动向zookeeper汇报ElasticSearch Cluster中每一个节点状态插件

* zkdiscovery-2.1(master)
	- 改进了/es/nodes目录下节点数据的格式，增加了本地节点设置项:settings。
* zkdiscovery-2.0
	- 改进了数据的一致性。修改了状态数据采集源，改用ES的/_cluster/state描述ES的状态数据，实现了Zookeeper和Zen的一致性。
	- 改进了数据采集者，减少了网络流量。修改了数据采集者为Master，以前是全部节点汇报自己节点状态数据，改进后由Master节点汇报全部节点状态数据。
	- 新增了对集群脑裂现象的支持（一种妥协的方式），在集群发生脑裂现象以后，每个小集群最后选举出Master会复写ZK上ES状态数据。
	- 新增了master_node节点：/es/master_node，指出了当前集群谁是master，并改进了/es/nodes节点下临时节点的Name显示为IP地址，可直观看出节点IP。
* zkdiscovery-1.0
	- ElasticSearch自动发现插件可自动向zookeeper汇报ES节点状态数据，包括主机名称、IP地址、端口号等。
	- 实现了在zk session过期后可节点再注册的功能

|  Version  | ES Version  |
| :-------: | :---------: |
| 2.1       | 1.2.1       |
| 2.0       | 1.2.1       |
| 1.0       | 1.2.1       |

## 配置
	com.xdxing.plugins.elasticsearch.zkdiscovery:
	   enabled: true
	   servers: localhost:2181
	   sessionTimeout: 30000
	   connectionTimeout: 10000
	   path: /es

## 安装
* maven clean package
* Copy elasticsearch-zkdiscovery-2.0.zip to directory installed elasticsearch.
* bin/plugin --url file:./elasticsearch-zkdiscovery-2.0.zip --install zkdiscovery

## 卸载
bin/plugin --remove zkdiscovery

## DEMO

* Zookeeper数据treeview：
<pre>
	/es
	  |- cluster_name
	  |- master_node
	  |- nodes
	  		|- 192.168.1.100
	  		|- 192.168.1.101
	  		|- 192.168.1.102
	  		|- ......
</pre>

* 一个节点上报的节点数据示例：
```JSON
{
    "http_address": "inet[/192.168.84.155:9200]",
    "ip": "192.168.84.155",
    "transport_address": "inet[/192.168.84.155:9300]",
    "name": "slave162",
    "id": "zJfHyEQ0TQ25arViU7wE5g",
    "host": "slave162",
    "version": "1.2.1",
    "settings": {
        "node": {
            "name": "slave162"
        },
        "name": "slave162",
        "path": {
            "data": "...",
            "logs": "...",
            "home": "..."
        },
        "cluster": {
            "routing": {
                "allocation": {
                    "node_concurrent_recoveries": "8"
                }
            },
            "name": "elasticsearch_single_test"
        },
        "discovery": {
            "zen": {
                "minimum_master_nodes": "1"
            }
        },
        "foreground": "yes",
        ...
    }
}
```
