

import newServer.TestVertexRouter;

import java.net.URI;
import java.net.URISyntaxException;

public class MyTestSupport {

	public static String getTestDir(){
		try {
			URI uri = new URI(TestVertexRouter.class.getResource("/").getPath()+"../../");
			uri = uri.normalize();
			return uri.toString().substring(1) + "testDir/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
