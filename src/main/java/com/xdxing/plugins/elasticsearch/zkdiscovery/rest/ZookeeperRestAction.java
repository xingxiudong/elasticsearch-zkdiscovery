package com.xdxing.plugins.elasticsearch.zkdiscovery.rest;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
//import org.elasticsearch.rest.XContentRestResponse;
//import org.elasticsearch.rest.XContentThrowableRestResponse;
//import org.elasticsearch.rest.action.support.RestXContentBuilder;

public class ZookeeperRestAction extends BaseRestHandler {

	@Inject
	public ZookeeperRestAction(final Settings settings, final Client client, final RestController restController) {
		super(settings, client);

		restController.registerHandler(RestRequest.Method.GET, "/{index}/{type}/_zookeeper", this);
		restController.registerHandler(RestRequest.Method.GET, "/{index}/_zookeeper", this);
	}

	@Override
	public void handleRequest(final RestRequest request, final RestChannel channel) {
		// try {
		// final XContentBuilder builder = RestXContentBuilder
		// .restContentBuilder(request);
		// builder.startObject();
		// builder.field("index", request.param("index"));
		// builder.field("type", request.param("type"));
		// builder.field("description", "This is a zookeeper response: "
		// + new Date().toString());
		// builder.endObject();
		// channel.sendResponse(new XContentRestResponse(request, OK, builder));
		// } catch (final IOException e) {
		// try {
		// channel.sendResponse(new XContentThrowableRestResponse(request,
		// e));
		// } catch (final IOException e1) {
		// logger.error("Failed to send a failure response.", e1);
		// }
		// }
	}

}
