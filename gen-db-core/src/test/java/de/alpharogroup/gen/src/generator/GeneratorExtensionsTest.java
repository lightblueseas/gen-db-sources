/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		log.info("System.getProperty(\"user.home\"):" + userHome);
		final String envVarHome = System.getenv("HOME");
		log.info("System.getenv(\"HOME\"):" + envVarHome);
	}

	@Test(enabled = false)
	public void testGenerateClasses() throws Exception
	{
		GeneratorExtensions.generateRepositoryClasses(true);
	}

	@Test(enabled = false)
	public void testGeneratePomFiles() throws Exception
	{
		GeneratorExtensions.generatePomFiles();
	}


}
