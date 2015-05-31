package tests;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestProperties {
	public static boolean testPrints = false;
	
	@Test
	public void dummyTest() {
		assertTrue(testPrints == true || testPrints == false);
	}
}
