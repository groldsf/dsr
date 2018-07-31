package routerHandlers;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import responses.Responder;
import systemManager.InterfaceSystemManager;

public class RouteHandlerDelete extends RouteHandler {

	public RouteHandlerDelete(InterfaceSystemManager smanager, String apiFs) {
		super(smanager, apiFs);
	}


	@Override
	public void handle(RoutingContext routingContext) {
		HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();
		try {
			origin(request, response);
			String realPath = generatePath(request.path());
			smanager.delete(realPath, res -> {
				Responder answer;
				if (res.succeeded()) {
					response.end();
				} else {
					//временно
					answer = new Responder(false, res.cause().toString());
					response.end(answer.toJson().toString());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			response.end(new Responder(false, "bad path").toJson().toString());
		}
	}
}
