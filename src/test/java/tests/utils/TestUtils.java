package tests.utils;

import static org.junit.Assert.assertTrue;
import genlib.utils.Utils;

import org.junit.Test;

public class TestUtils {

	@Test
	public void testEq() {
		assertTrue(Utils.eq(0.9d, 1d, 0.1d));
		assertTrue(Utils.eq(1d, 0.9d, 0.1d));
		assertTrue(!Utils.eq(20,10,1));
		assertTrue(!Utils.eq(10,20,1));
	}
}
