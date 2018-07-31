package responses;

import dirElements.Jsonoble;
import io.vertx.core.json.JsonObject;

public class Responder implements Jsonoble {

	private boolean status;
	private Object answer;

	public Responder(boolean status, Object answer) {
		this.status = status;
		this.answer = answer;
	}

	public Responder(boolean status) {
		this.status = status;
	}

	public JsonObject toJson() {
		JsonObject res = new JsonObject();
		res.put("status", status);
		if (answer != null)
			res.put("answer", answer);
		return res;
	}

	public Object getAnswer() {
		return answer;
	}

	public void Answer(JsonObject answer) {
		this.answer = answer;
	}
}
