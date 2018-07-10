import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class MyVertex extends AbstractVerticle {

	//private HttpServer server = vertx.createHttpServer();
	private SystemManager smanager = new SystemManager();

	@Override
	public void start(Future<Void> fut){
		HttpServer server = vertx.createHttpServer();
		server
				.requestHandler(request -> {

					try {
						if(request.headers().contains("Origin"))
							request.response().putHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
					}catch (Exception e){
						System.out.println("error");
					}


					System.out.println(request.method());
					//System.out.println(request.absoluteURI());
					System.out.println(request.headers().names());
					//System.out.println(request.uri());
					System.out.println(request.path());
					System.out.println(request.params());
					if(request.params().isEmpty()){
						returnError(request);
						return;
					}
					JsonObject json = new JsonObject(request.params().toString());
					//System.out.println(json);

					if(!json.containsKey("type")){
						returnError(request);
						return;
					}
					switch (json.getString("type")){
						case "auto":
							auto(request,json);
							break;
						case "getPackage":
							getPackage(request,json);
							break;
						case "getFile":
							getFile(request,json);
							break;
							default:
								returnError(request);
								break;
					}



					//request.response().end("Hello world");
				})
				.listen(8080,res -> {
					if (res.succeeded()) {
						System.out.println("Server is now listening!");
						fut.complete();
					} else {
						System.out.println("Failed to bind!");
						fut.fail(res.cause());
					}
				});

	}

	private void getFile(HttpServerRequest request, JsonObject in) {
		if(!in.containsKey("path"))
			returnError(request);

		HttpServerResponse response = request.response();

		request.params();

		JsonObject out = new JsonObject();
		out.put("status",true);

		response.end("getFile type");
	}

	private void getPackage(HttpServerRequest request, JsonObject in) {
		if(!in.containsKey("path"))
			returnError(request);

		JsonArray jsonArray = smanager.getPackage(in.getString("path"));
		if(jsonArray == null)
			returnError(request);
		JsonObject out = new JsonObject();
		out.put("status", true);
		out.put("answer", jsonArray);

		request.response().end(out.toString());
	}

	private void auto(HttpServerRequest request, JsonObject in) {

		if(!in.containsKey("login") || !in.containsKey("password") )
			returnError(request);

		JsonObject out = new JsonObject();
		out.put("status",true);


		request.response().end(out.toString());
	}


	private void returnError(HttpServerRequest request) {
		JsonObject json = new JsonObject();
		json.put("status",false);
		request.response().end(json.toString());
	}

}
