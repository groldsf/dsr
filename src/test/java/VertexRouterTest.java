import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.TreeSet;

@RunWith(VertxUnitRunner.class)
public class VertexRouterTest {
	private Vertx vertx;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		VertexRouter vertex = new VertexRouter();
		vertex.setPathMainDir(TestSupport.getTestDir());
		vertx.deployVerticle(vertex, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void access(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(8080, "localhost", "/api/fs/", response -> response.bodyHandler(body -> {
			async.complete();
		}));
	}

	@Test
	public void getPackage(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(8080, "localhost", "/api/fs/", response -> response.bodyHandler(body -> {
			try {
				JsonObject json = new JsonObject(body.toString());
				context.assertTrue(json.getBoolean("status"));
				JsonObject ans = json.getJsonObject("answer");
				JsonArray array = ans.getJsonArray("array");
				context.assertEquals(array.size(), 5);
				Set<String> set = new TreeSet<>();
				for (Object myFile : array) {
					set.add(((JsonObject) myFile).getString("name"));
				}
				context.assertTrue(set.contains("1") && set.contains("2") && set.contains("3") &&
						set.contains("4") && set.contains("1.txt"));
				async.complete();
			} catch (Exception e) {
				context.fail();
				e.printStackTrace();
			}
		}));
	}

	@Test
	public void fatherDirFalse(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient()
				.getNow(8080, "localhost", "/api/fs/1/5", response -> response.bodyHandler(body -> {
					try {
						JsonObject json = new JsonObject(body.toString());
						context.assertTrue(json.getBoolean("status"));
						JsonObject ans = json.getJsonObject("answer");
						context.assertFalse(ans.getBoolean("isMainDir"));
						context.assertEquals(ans.getString("fatherDir"), "/1");
						async.complete();
					} catch (Exception e) {
						context.fail();
						e.printStackTrace();
					}
				}));
	}

	@Test
	public void fatherDirTrue(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient()
				.getNow(8080, "localhost", "/api/fs", response -> response.bodyHandler(body -> {
					try {
						JsonObject json = new JsonObject(body.toString());
						context.assertTrue(json.getBoolean("status"));
						JsonObject ans = json.getJsonObject("answer");
						context.assertTrue(ans.getBoolean("isMainDir"));
						async.complete();
					} catch (Exception e) {
						context.fail();
						e.printStackTrace();
					}
				}));
	}

	@Test
	public void getFile(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(8080, "localhost", "/api/fs/1.txt", response -> response.bodyHandler(body -> {
			try {
				JsonObject json = new JsonObject(body.toString());
				context.assertTrue(json.getBoolean("status"));
				JsonObject ans = json.getJsonObject("answer");
				context.assertEquals(ans.getString("text"), "test txt");
				async.complete();
			} catch (Exception e) {
				context.fail();
				e.printStackTrace();
			}

		}));
	}

	@Test
	public void badRequest(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(8080, "localhost", "/api/fs/2.txt", response -> response.bodyHandler(body -> {
			try {
				JsonObject json = new JsonObject(body.toString());
				context.assertFalse(json.getBoolean("status"));
				async.complete();
			} catch (Exception e) {
				context.fail();
				e.printStackTrace();
			}

		}));
	}
}