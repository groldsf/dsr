package oldServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import oldServer.RequestHandler;

public class MyVertex extends AbstractVerticle {


	public void start(Future<Void> fut){
		HttpServer server = vertx.createHttpServer();
		RequestHandler rh = new RequestHandler(vertx.fileSystem());

		server.requestHandler(rh);
		server.listen(8080,res -> {
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
