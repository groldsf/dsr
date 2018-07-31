package routerHandlers;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import responses.Responder;
import systemManager.InterfaceSystemManager;

public class RouteHandlerOpen extends RouteHandler {


	public RouteHandlerOpen(InterfaceSystemManager smanager, String apiFs) {
		super(smanager, apiFs);
	}


	public void handle(RoutingContext routingContext) {
		HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();
		try {
			origin(request, response);
			String realPath = generatePath(request.path());
			smanager.open(realPath, res -> {
				Responder answer;
				if (res.succeeded()) {
					answer = new Responder(true, res.result().toJson());
				} else {
					//временно
					answer = new Responder(false, res.cause().toString());
				}
				response.end(answer.toJson().toString());
			});
		} catch (Exception e) {
			e.printStackTrace();
			response.end(new Responder(false, "bad path").toJson().toString());
		}


	}


}
