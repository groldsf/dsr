package systemManager;

import dirElements.Jsonoble;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.CopyOptions;

public interface InterfaceSystemManager {

	void setMainPath(String pathMainDir);

	void open(String path, Handler<AsyncResult<Jsonoble>> handler);

	void copy(String path, String newPath, Handler<AsyncResult<Void>> handler);

	void delete(String path, Handler<AsyncResult<Void>> handler);

	void move(String from, String to, CopyOptions copyOption, Handler<AsyncResult<Void>> handler);
}
