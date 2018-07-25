package newServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import systemManager.InterfaceSystemManager;
import systemManager.NewSystemManager;

public class TestVertexRouter extends AbstractVerticle {

	private InterfaceSystemManager smanager;
	private String pathMainDir;

	public TestVertexRouter setPathMainDir(String pathMainDir) {
		this.pathMainDir = pathMainDir;
		return this;
	}

	public void start(Future<Void> fut){
		smanager  = new NewSystemManager(vertx.fileSystem());
		smanager.setMainPath(pathMainDir);
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		String apiFs = "/api/fs";

		Route apiRoute = router.route(apiFs+"*");
		apiRoute.method(HttpMethod.GET);
		apiRoute.handler(new RouteHandler(smanager, apiFs));

		server.requestHandler(router::accept).listen(8080,res -> {
			if (res.succeeded()) {
				System.out.println("Server is now listening!");
				fut.complete();
			} else {
				System.out.println("Failed to bind!");
				fut.fail(res.cause());
			}
		});
	}
}
