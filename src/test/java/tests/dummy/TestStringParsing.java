package tests.dummy;

import static org.junit.Assert.*;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestStringParsing {

	@Test
	public void testStringParsing() {
		String leafString = "^N([0-9]+) \\[label=\"\'(.*) \\(([0-9.]+)[/]*([0-9.]*)\\)\'\".*$";
		Pattern leafPattern = Pattern.compile(leafString);
		String nodeString = "^N([0-9]+) \\[label=\"(.*)\" \\]$";
		Pattern nodePattern = Pattern.compile(nodeString);
		String edgeString = "^N([0-9]+)->N([0-9]*) \\[label=\"\'([<!=>]+) (.*)\'\"\\]$";
		Pattern edgePattern = Pattern.compile(edgeString);
		
		String sNode = "digraph J48Tree {\n"
				+	"N0 [label=\"a5\" ]\n";		
		String[] lines = sNode.split("\n");		
		Matcher m = leafPattern.matcher(lines[1]);
		assertFalse(m.matches());
		m = edgePattern.matcher(lines[1]);
		assertFalse(m.matches());
		m = nodePattern.matcher(lines[1]);
		assertTrue(m.matches());		
		assertTrue(Integer.parseInt(m.group(1))==0);
		assertTrue(m.group(2).equals("a5"));
		
		String sLeaf = "digraph J48Tree {\n"
				+	"N15 [label=\"\'c1 (4.0/1.0)\'\" shape=box style=filled ]\n";		
		lines = sLeaf.split("\n");					
		m = edgePattern.matcher(lines[1]);
		assertFalse(m.matches());
		m = nodePattern.matcher(lines[1]);
		assertFalse(m.matches());	
		m = leafPattern.matcher(lines[1]);
		assertTrue(m.matches());
		assertTrue(Integer.parseInt(m.group(1))==15);
		assertTrue(m.group(2).equals("c1"));
		assertTrue(Double.parseDouble(m.group(3))==4.0);		 
		assertTrue(Double.parseDouble(m.group(4))==1.0);
		
		String sEdge = "digraph J48Tree {\n"
				+	"N12->N20 [label=\"\'= true\'\"]\n";		
		lines = sEdge.split("\n");					
		m = leafPattern.matcher(lines[1]);
		assertFalse(m.matches());
		m = nodePattern.matcher(lines[1]);
		assertFalse(m.matches());			
		m = edgePattern.matcher(lines[1]);
		assertTrue(m.matches());
		assertTrue(Integer.parseInt(m.group(1))==12);
		assertTrue(Integer.parseInt(m.group(2))==20);
		assertTrue(m.group(3).equals("="));
		assertTrue(WekaUtils.makeSign(m.group(3)).equals(Sign.EQUALS));
		assertTrue(m.group(4).equals("true"));	
	}
	
}
