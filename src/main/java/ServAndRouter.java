import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class ServAndRouter extends AbstractVerticle {


	@Override
	public void start(Future<Void> fut) {
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);

		router.route("/123")
				.handler(routingContext -> {
					// This handler will be called for every request
					HttpServerResponse response = routingContext.response();
					HttpServerRequest request = routingContext.request();
					try {
						if(request.headers().contains("Origin"))
							response.putHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
					}catch (Exception e){
						System.out.println("error");
					}

					response.end("Hello world");
					System.out.println(request.absoluteURI());
					System.out.println(request.headers().names());
				});

		//router

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
