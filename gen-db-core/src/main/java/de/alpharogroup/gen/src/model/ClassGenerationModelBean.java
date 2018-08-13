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
package de.alpharogroup.gen.src.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClassGenerationModelBean {

	/** the package names have the suffix:'PackageName' */
	private String basePackageName;
	private String domainMapperPackageName;
	private String domainPackageName;
	private String domainServicePackageName;
	private String modelPackageName;
	private Set<String> qualifiedModelClassNames;
	private String repositoryPackageName;
	private String restPackageName;
	private String servicePackageName;
	private String srcDomainGenerationPackage;
	private String srcDomainMapperGenerationPackage;
	private String srcDomainServiceGenerationPackage;
	/** SOURCE FOLDERS have the prefix 'src'. */
	private String srcFolder;
	/**
	 * SOURCE GENERATION PACKAGE NAMES have the prefix 'scr' and the suffix
	 * 'GenerationPackage'.
	 */
	private String srcGenerationPackage;
	private String srcRestGenerationPackage;
	private String srcServiceGenerationPackage;
	private String srcTestFolder;
	private String srcTestGenerationPackage;
	private String templateDir; // ???

}
