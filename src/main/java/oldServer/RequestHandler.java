package oldServer;

import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServerRequest;
import systemManager.InterfaceSystemManager;
import systemManager.NewSystemManager;


public class RequestHandler implements Handler<HttpServerRequest> {


	private InterfaceSystemManager smanager;

	public RequestHandler(FileSystem fileSystem) {
		smanager = new NewSystemManager(fileSystem);
	}

	public void handle(HttpServerRequest request) {
		/*
		try {
			if(request.headers().contains("Origin"))
				request.response().putHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		}catch (Exception e){
			System.out.println("error");
			System.out.println(e);
		}


		System.out.println(request.method());
		//System.out.println(request.absoluteURI());
		//System.out.println(request.headers().names());
		//System.out.println(request.uri());
		System.out.println(request.path());
		System.out.println(request.params());


		if(request.params().isEmpty()){
			request.response().end(new BadResponse("params is empty").toString());
			return;
		}
		try {

			JsonObject json = new JsonObject(request.params().toString());
			System.out.println(json);

			if (!json.containsKey("type")) {
				request.response().end(new BadResponse("type is empty").toString());
				return;
			}
			switch (json.getString("type")) {
				case "auto":
					auto(request, json);
					break;
				case "returnPackage":
					returnPackage(request.response(), json);
					break;
				case "readFile":
					returnFile(request.response(), json);
					break;
				default:
					request.response().end(new BadResponse("und type").toString());
					break;
			}
		}catch (Exception e){
			System.out.println(e);
			request.response().end(new BadResponse("error read json").toString());
		}
	}

	private void returnFile(HttpServerResponse response, JsonObject json) {
		smanager.readFile(json, res ->{
			if(res.succeeded()){
				response.end(new ResponseTextFile(res.result()).toString());
			}else{
				response.end(new BadResponse(res.cause().toString()).toString());
			}
		});
	}

	private void returnPackage(HttpServerResponse response, JsonObject json) {
		smanager.readPackage(json, res->{
			if(res.succeeded()){
				response.end(new ResponsePackage(res.result()).toString());
			}else{
				response.end(new BadResponse(res.cause().toString()).toString());
			}
		});
	}

	private void auto(HttpServerRequest request, JsonObject in) {

		if(!in.containsKey("login") || !in.containsKey("password") )
			request.response().end(new BadResponse("error login or password").toString());

		JsonObject out = new JsonObject();
		out.put("status",true);


		request.response().end(out.toString());
		*/
	}
}
