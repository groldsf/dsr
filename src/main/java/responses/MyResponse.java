package responses;

import dirElements.Jsonoble;
import io.vertx.core.json.JsonObject;

public class MyResponse implements Jsonoble {

	private boolean status;
	private Object answer;

	public MyResponse(boolean status, Object answer) {
		this.status = status;
		this.answer = answer;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public JsonObject toJson() {
		JsonObject res = new JsonObject();
		res.put("status", status);
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
