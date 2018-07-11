import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class GetPackage {

	HttpServerRequest request;
	JsonArray jsonArray;
	public GetPackage(HttpServerRequest request, JsonArray jsonArray){
		this.jsonArray = jsonArray;
		this.request = request;
	}



	private void startAnswer(){

	}



	private void endAnswer(){
		if(jsonArray == null)
			ErrorResponse.returnError(request);
		JsonObject out = new JsonObject();
		out.put("status", true);
		out.put("answer", jsonArray);

		request.response().end(out.toString());
	}

}
