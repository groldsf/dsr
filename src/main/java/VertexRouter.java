import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import routerHandlers.*;
import systemManager.InterfaceSystemManager;
import systemManager.SystemManager;

public class VertexRouter extends AbstractVerticle {

	private PathApi pathApi = new PathApi();
	private String pathMainDir;

	public VertexRouter setPathMainDir(String pathMainDir) {
		this.pathMainDir = pathMainDir;
		return this;
	}

	public void start(Future<Void> fut) {

		InterfaceSystemManager smanager = new SystemManager(vertx.fileSystem());
		smanager.setMainPath(pathMainDir);
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		Route getRoute = router.get(pathApi.apiFs + "*");
		getRoute.handler(new RouteHandlerOpen(smanager, pathApi.apiFs));

		Route copyRoute = router.post(pathApi.apiFsCopy + "*");
		copyRoute.handler(new RouteHandlerCopy(smanager, pathApi.apiFsCopy));

		Route moveRoute = router.post(pathApi.apiFsMove + "*");
		moveRoute.handler(new RouteHandlerMove(smanager, pathApi.apiFsMove));

		Route deleteRoute = router.delete(pathApi.apiFs + "*");
		deleteRoute.handler(new RouteHandlerDelete(smanager, pathApi.apiFs));

		Route uploadRoute = router.post(pathApi.upload + "*");
		uploadRoute.handler(new RouteUpload(smanager, pathApi.upload));

		router.route().handler(res -> {
			String path = res.request().path();
			if (path.equals("/"))
				path += "index.html";
			res.response().sendFile("client" + path);
		});


		server.requestHandler(router::accept).listen(8080, res -> {
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
