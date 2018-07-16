import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public abstract class ErrorResponse {

	public static void returnError(HttpServerRequest request) {
		JsonObject json = new JsonObject();
		json.put("status",false);
		request.response().end(json.toString());
	}

	public static void returnBadFile(HttpServerRequest request) {
		JsonObject json = new JsonObject();
		json.put("status", true);
		json.put("text", "нельзя открыть файл этого типа");
		request.response().end(json.toString());
	}
}
