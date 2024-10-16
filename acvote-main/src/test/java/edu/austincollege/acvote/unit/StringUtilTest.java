package edu.austincollege.acvote.unit;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

	@Test
	public void test_dquote_whenSingleQuotesPresent() {
		String str = "tr/td[@id='xyz']";
		assertEquals("tr/td[@id=\"xyz\"]",StringUtil.dquote(str));
	}

	@Test
	public void test_dquote_whenSingleQuotesAbsent() {
		String str = "tr/td";
		assertEquals("tr/td",StringUtil.dquote(str));
	}

	@Test
	public void test_dquote_whenEmptyString() {
		String str = "";
		assertEquals("",StringUtil.dquote(str));
	}

	
	@Test
	public void test_dquote_whenNull() {
		String str = null;
		assertEquals(null,StringUtil.dquote(str));
	}

}
