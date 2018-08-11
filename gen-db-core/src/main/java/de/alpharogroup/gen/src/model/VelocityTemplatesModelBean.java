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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VelocityTemplatesModelBean
{
	/** TEMPLATE FILES have the prefix 'tmpl'. */

	String tmplApplicationContextXml;
	/** POM TEMPLATE FILES */
	String tmplBusinessPom;
	String tmplDataApplicationContextXml;
	String tmplDataPom;
	String tmplDomainClass;
	String tmplDomainMapperClass;
	String tmplDomainPom;
	String tmplDomainServiceClass;

	String tmplDomainServiceInterface;
	String tmplEntitiesPom;
	String tmplGitignore;
	String tmplH2ApplicationContextXml;
	String tmplIndexJsp;
	String tmplInitDbInitClass;
	String tmplInitInitDbClass;
	String tmplInitPom;
	String tmplJdbcH2Properties;
	String tmplJdbcProperties;
	String tmplJettyRunnerClass;
	String tmplLog4jProperties;
	String tmplManifestMf;
	String tmplPersistenceH2Xml;
	String tmplEhcacheXml;
	String tmplPersistenceXml;
	String tmplProjectProperties;
	String tmplRepositoryClass;
	String tmplRepositoryTestClass;
	String tmplRestApiPom;
	String tmplRestClientClass;
	String tmplRestClientPom;
	String tmplRestClientTestClass;
	String tmplRestResourceClass;
	String tmplRestResourceInterface;
	String tmplRestWebPom;
	String tmplServiceClass;
	String tmplServiceH2TestClass;
	String tmplServiceInterface;
	String tmplServiceTestClass;
	String tmplTestNgXml;
	String tmplWebApplicationContextXml;
	String tmplWebXml;

}
