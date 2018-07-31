import io.vertx.core.Vertx;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import systemManager.SystemManager;

import java.util.Set;
import java.util.TreeSet;

@RunWith(VertxUnitRunner.class)
public class SystemManagerTest {
	private SystemManager sm;

	@Before
	public void setUp() {
		Vertx vertx = Vertx.vertx();
		sm = new SystemManager(vertx.fileSystem());
		sm.setMainPath(TestSupport.getTestDir());

	}

	@Test
	public void readGoodFile(TestContext context) {
		final Async async = context.async();
		sm.readFile("1.txt", res -> {
			if (res.failed()) {
				context.fail();
			} else {
				context.assertEquals(res.result().toJson().getString("text"), "test txt");
				async.complete();
			}
		});
	}

	@Test
	public void readBadFile(TestContext context) {
		final Async async = context.async();
		sm.readFile("bad.txt", res -> {
			if (res.failed()) {
				async.complete();
			} else {
				context.fail();
			}
		});
	}

	@Test
	public void readGoodPackage(TestContext context) {
		final Async async = context.async();
		sm.readPackage("", res -> {
			if (res.failed()) {
				context.fail();
			} else {
				JsonObject json = res.result().toJson();
				JsonArray array = json.getJsonArray("array");
				context.assertEquals(array.size(), 5);
				Set<String> set = new TreeSet<>();
				for (Object myFile : array) {
					set.add(((JsonObject) myFile).getString("name"));
				}
				context.assertTrue(set.contains("1") && set.contains("2") && set.contains("3") &&
						set.contains("4") && set.contains("1.txt"));
				async.complete();
			}
		});
	}

	@Test
	public void readBadPackage(TestContext context) {
		final Async async = context.async();
		sm.readPackage("123231", res -> {
			if (res.failed()) {
				async.complete();
			} else {
				context.fail();
			}
		});
	}

	@Test
	public void copy(TestContext context) {
		final Async async = context.async();
		sm.copy("1.txt", "2.txt", res -> {
			context.assertTrue(res.succeeded());
			sm.readFile("2.txt", res2 -> {
				context.assertTrue(res2.succeeded());
				sm.delete("2.txt", res3 -> {
					context.assertTrue(res3.succeeded());
					async.complete();
				});
			});

		});
	}

	@Test
	public void move(TestContext context) {
		final Async async = context.async();
		sm.move("1.txt", "2.txt", new CopyOptions(), res -> {
			context.assertTrue(res.succeeded());
			sm.move("2.txt", "1.txt", new CopyOptions(), res2 -> {
				context.assertTrue(res2.succeeded());
				async.complete();
			});
		});
	}

	@Test
	public void delete(TestContext context) {
		final Async async = context.async();
		sm.copy("1.txt", "2.txt", res -> sm.delete("2.txt", res2 -> {
			context.assertTrue(res2.succeeded());
			async.complete();
		}));
	}
}