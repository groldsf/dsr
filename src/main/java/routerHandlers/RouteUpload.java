package routerHandlers;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import responses.Responder;
import systemManager.InterfaceSystemManager;

public class RouteUpload extends RouteHandler {
	public RouteUpload(InterfaceSystemManager smanager, String apiFs) {
		super(smanager, apiFs);
	}

	public void handle(RoutingContext ctx) {
		HttpServerRequest request = ctx.request();
		HttpServerResponse response = ctx.response();
		try {
			origin(request, response);
			String realPath = generatePath(request.path());
			for (FileUpload file : ctx.fileUploads()) {
				file.uploadedFileName();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.end(new Responder(false, "bad path").toJson().toString());
		}
	}
}
