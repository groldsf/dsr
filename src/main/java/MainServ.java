import io.vertx.core.Vertx;
import java.util.Scanner;

public class MainServ {


	public static void main(String str[]) {


		Vertx vertx = Vertx.vertx();
		String testDir = MyTestSupport.getTestDir();
		System.out.println(testDir);
		vertx.deployVerticle(new TestVertexRouter().setPathMainDir(testDir));

		Scanner in = new Scanner(System.in);
		in.next();

		vertx.close();
	}
}
