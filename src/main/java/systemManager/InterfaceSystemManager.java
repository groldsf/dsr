package systemManager;

import dirElements.Jsonoble;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface InterfaceSystemManager {
	void open(String path, Handler<AsyncResult<Jsonoble>> handler);
	void setMainPath(String pathMainDir);
}
