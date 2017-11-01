package de.alpharogroup.gen.src.generator;

import org.testng.annotations.Test;

public class GeneratorExtensionsTest
{

	@Test(enabled = true)
	public void testGenerateClasses() throws Exception
	{
		GeneratorExtensions.generateRepositoryClasses();
	}

	@Test(enabled = true)
	public void testGeneratePomFiles() throws Exception
	{
		GeneratorExtensions.generatePomFiles();
	}

}
