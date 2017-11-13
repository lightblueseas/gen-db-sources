package de.alpharogroup.gen.src.generator;

import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratorExtensionsTest
{

	@Test(enabled = true)
	public void testdummy() throws Exception
	{
		final String userHome = System.getProperty("user.home");
		log.info("System.getProperty(\"user.home\"):"+userHome);
		final String envVarHome = System.getenv("HOME");
		log.info("System.getenv(\"HOME\"):"+envVarHome);
	}

	@Test(enabled = true)
	public void testGenerateClasses() throws Exception
	{
		GeneratorExtensions.generateRepositoryClasses(true);
	}

	@Test(enabled = true)
	public void testGeneratePomFiles() throws Exception
	{
		GeneratorExtensions.generatePomFiles();
	}



}
