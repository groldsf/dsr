package dirElements;

import io.vertx.core.json.JsonObject;

public class TextFile implements Jsonoble {

	private String text;

	public TextFile(String text) {
		this.text = text;
	}

	public JsonObject toJson() {
		return new JsonObject().put("text", text);
	}
}
