package io.github.bdulac.persephona;

import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class PersephonaTest extends TestCase {
	
	@Test
	public void testParseRimbaud() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("La sottise, l'erreur, le péché, la lésine,");
		sb.append("Occupe nos esprits et travaille nos corps,");
		sb.append("Et nous alimentons nos aimables remords ");
		sb.append("Comme les mendiants nourissent leur vermine.");
		Persephona pers = new Persephona();
		pers.parse(sb);
	}
	
	@Test
	public void testParseByron() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Darkness, ");
		sb.append("Dear Doctor, I Have Read Your Play, ");
		sb.append("The Destruction of Sennacherib, ");
		sb.append("Don Juan: Dedication.");
		Persephona pers = new Persephona();
		pers.parse(sb);
	}
	
	@Test
	public void testParseSchiller() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Freude, schöner Götterfunken,");
		sb.append("Tochter aus Elysium,");
		sb.append("Wir betreten feuertrunken, ");
		sb.append("Himmlische, dein Heiligtum.");
		Persephona pers = new Persephona();
		pers.parse(sb);
	}
}
