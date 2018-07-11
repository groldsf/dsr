import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public abstract class ErrorResponse {

	public static void returnError(HttpServerRequest request) {
		JsonObject json = new JsonObject();
		json.put("status",false);
		request.response().end(json.toString());
	}
}
