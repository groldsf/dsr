import io.vertx.core.Handler;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.rxjava.core.buffer.Buffer;
import responses.MyResponse;
import systemManager.InterfaceSystemManager;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class RouteHandler implements Handler<RoutingContext> {

	private InterfaceSystemManager smanager;
	private String apiFs;

	public RouteHandler(InterfaceSystemManager smanager, String apiFs){
		this.smanager = smanager;
		this.apiFs = apiFs;
	}

	public void handle(RoutingContext routingContext) {
		HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();
		try {
			if(request.headers().contains("Origin")) {
				response.putHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		System.out.println(request.path());

		String path = "";
		try {
			path = new URI(URLDecoder.decode(request.path(), "UTF-8")).normalize().toString();
		} catch (UnsupportedEncodingException e) {
			response.end(new MyResponse(false, e).toJson().toString());
			return;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		smanager.open(path.substring(apiFs.length()), res -> {
			MyResponse answer;
			if(res.succeeded()){
				answer = new MyResponse(true,res.result().toJson());
			}else{
				//временно
				answer = new MyResponse(false, res.cause().toString());
			}
			System.out.println(answer.toJson().toString());
			response.end(answer.toJson().toString());
		});
	}


}
