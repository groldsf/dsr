package routerHandlers;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import systemManager.InterfaceSystemManager;

import java.net.URI;
import java.net.URLDecoder;

public abstract class RouteHandler implements Handler<RoutingContext> {
	protected InterfaceSystemManager smanager;
	protected String apiFs;

	public RouteHandler(InterfaceSystemManager smanager, String apiFs) {
		this.smanager = smanager;
		this.apiFs = apiFs;
	}

	protected String generatePath(String path) throws Exception {
		path = new URI(URLDecoder.decode(path, "UTF-8")).normalize().toString();
		if (!path.startsWith(apiFs)) {
			throw new Exception();
		} else {
			return path.substring(apiFs.length());
		}
	}

	protected void origin(HttpServerRequest request, HttpServerResponse response) {
		if (request.headers().contains("Origin")) {
			response.putHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		}
	}
}
