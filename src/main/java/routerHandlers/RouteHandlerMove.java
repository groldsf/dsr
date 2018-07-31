package routerHandlers;

import io.vertx.core.file.CopyOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import responses.Responder;
import systemManager.InterfaceSystemManager;

public class RouteHandlerMove extends RouteHandler {
	public RouteHandlerMove(InterfaceSystemManager smanager, String apiFs) {
		super(smanager, apiFs);
	}

	@Override
	public void handle(RoutingContext routingContext) {
		HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();
		try {
			origin(request, response);
			String realPath = generatePath(request.path());
			String newPath = routingContext.request().getParam("newName");
			smanager.move(realPath, newPath, new CopyOptions(), res -> {
				if (res.succeeded()) {
					response.end(new Responder(true).toJson().toString());
				} else {
					response.end(new Responder(false, res.cause()).toJson().toString());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			response.end(new Responder(false, "bad path").toJson().toString());
		}
	}
}
