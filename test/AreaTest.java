package test;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class AreaTest{

	@Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
	public void get_area_test() {
	    Result result = callAction(
	      controllers.routes.ref.AreaController.get_area()
	    );

	    System.out.println(result);
	    assertThat(status(result)).isEqualTo(OK);
	    assertThat(contentType(result)).isEqualTo("application/json");
	    assertThat(charset(result)).isEqualTo("utf-8");
	    assertThat(contentAsString(result)).contains("area");
	}

}
