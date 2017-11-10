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
@FieldDefaults(level=AccessLevel.PRIVATE)
public class VelocityTemplatesModelBean
{
    /** TEMPLATE FILES have the prefix 'tmpl'. */

    /** POM TEMPLATE FILES */
    String tmplBusinessPom;
    String tmplDataPom;
    String tmplDomainPom;
    String tmplEntitiesPom;
    String tmplInitPom;
    String tmplRestApiPom;
    String tmplRestClientPom;
    String tmplRestWebPom;

    String tmplGitignore;
    String tmplLog4jProperties;
    String tmplRepositoryClass;
    String tmplServiceClass;
    String tmplServiceInterface;
    String tmplDomainClass;
    String tmplDomainMapperClass;
    String tmplDomainServiceClass;
    String tmplDomainServiceInterface;
    String tmplInitDbInitClass;
    String tmplInitInitDbClass;
    String tmplJdbcH2Properties;
    String tmplJdbcProperties;
    String tmplPersistenceH2Xml;
    String tmplPersistenceXml;
    String tmplRepositoryTestClass;
    String tmplServiceH2TestClass;
    String tmplServiceTestClass;
    String tmplApplicationContextXml;
    String tmplH2ApplicationContextXml;
    String tmplTestNgXml;
    String tmplRestResourceClass;
    String tmplRestResourceInterface;
    String tmplRestClientClass;
    String tmplRestClientTestClass;
    String tmplJettyRunnerClass;
    String tmplDataApplicationContextXml;
    String tmplIndexJsp;
    String tmplManifestMf;
    String tmplProjectProperties;
    String tmplWebApplicationContextXml;
    String tmplWebXml;

}
