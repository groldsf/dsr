package systemManager;


import dirElements.File;
import dirElements.Jsonoble;
import dirElements.Package;
import dirElements.TextFile;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SystemManager implements InterfaceSystemManager {


	private String mainPath = "C:/";
	private FileSystem fileSystem;

	public SystemManager(FileSystem fs) {
		fileSystem = fs;
	}

	private static String toAbsolutePath(String maybeRelative) {
		Path path = Paths.get(maybeRelative);
		Path effectivePath = path;
		if (!path.isAbsolute()) {
			Path base = Paths.get("");
			effectivePath = base.resolve(path).toAbsolutePath();
		}
		return effectivePath.normalize().toString();
	}

	public void setMainPath(String mainPath) {
		Path path = Paths.get(mainPath);
		if (path.isAbsolute())
			this.mainPath = mainPath;
		else
			this.mainPath = toAbsolutePath(mainPath);
		if (!this.mainPath.endsWith("/"))
			this.mainPath += "/";
	}

	public void readFile(String path, Handler<AsyncResult<Jsonoble>> handler) {

		try {
			path = fullPath(path);
			if (!path.endsWith(".txt")) {
				handler.handle(Future.failedFuture("type error"));//rewrite
				return;
			}

			fileSystem.readFile(path, res -> {
				if (res.succeeded()) {
					TextFile textFile = new TextFile(res.result().toString());
					handler.handle(Future.succeededFuture(textFile));

				} else {
					handler.handle(Future.failedFuture(res.cause()));
				}
			});
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}


	public void open(String path, Handler<AsyncResult<Jsonoble>> handler) {

		try {
			String fullPath = fullPath(path);
			String finalPath = new URI(path).normalize().toString();

			fileSystem.props(fullPath, res -> {
				if (res.failed()) {
					handler.handle(Future.failedFuture(res.cause()));
					return;
				}
				FileProps props = res.result();
				if (props.isDirectory()) {
					readPackage(finalPath, handler);
				} else {
					readFile(finalPath, handler);
				}
			});
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}


	public void readPackage(String path, Handler<AsyncResult<Jsonoble>> handler) {
		try {
			String finalPath = new URI(path).normalize().toString();
			fileSystem.readDir(fullPath(path), dir -> {
				if (dir.failed()) {
					handler.handle(Future.failedFuture(dir.cause()));
					return;
				}
				createJsonDirectory(dir.result(), res -> {
					if (res.succeeded()) {
						Package answer = new Package(res.result(), finalPath);
						handler.handle(Future.succeededFuture(answer));
					} else {
						handler.handle(Future.failedFuture(res.cause()));
					}
				});
			});
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}

	private void createJsonDirectory(List<String> dir, Handler<AsyncResult<ArrayList<File>>> handler) {
		ArrayList<File> array = new ArrayList<>();
		ArrayList<Future> list = new ArrayList<>();
		for (String path : dir) {
			Future<Void> fut = Future.future();
			list.add(fut);
			fileSystem.props(path, res -> {
				if (res.succeeded()) {
					String localPath = path.substring(mainPath.length());
					array.add(new File(res.result(), localPath));
					fut.complete();
				} else {
					fut.fail(res.cause());
				}
			});
		}
		CompositeFuture.all(list).setHandler(ar -> {
			if (ar.succeeded()) {
				handler.handle(Future.succeededFuture(array));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	private String fullPath(String path) throws URISyntaxException {
		return mainPath + new URI(path).normalize().toString();
	}

	public void copy(String path, String newPath, Handler<AsyncResult<Void>> handler) {
		try {
			fileSystem.copyRecursive(fullPath(path), fullPath(newPath), true, result -> {
				if (result.succeeded()) {
					handler.handle(Future.succeededFuture());
				} else {
					handler.handle(Future.failedFuture(result.cause()));
				}
			});
		} catch (URISyntaxException e) {
			e.printStackTrace();
			handler.handle(Future.failedFuture(e));
		}
	}

	public void delete(String path, Handler<AsyncResult<Void>> handler) {
		try {
			path = fullPath(path);
			String finalPath = path;
			fileSystem.exists(path, result -> {
				if (result.succeeded() && result.result()) {
					fileSystem.deleteRecursive(finalPath, true, r -> {
						if (r.succeeded()) {
							handler.handle(Future.succeededFuture());
						} else {
							handler.handle(Future.failedFuture(r.cause()));
						}
					});
				} else {
					handler.handle(Future.failedFuture(result.cause()));
				}
			});
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}

	public void move(String from, String to, CopyOptions copyOption, Handler<AsyncResult<Void>> handler) {
		try {
			fileSystem.move(fullPath(from), fullPath(to), copyOption, handler);
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}
}
