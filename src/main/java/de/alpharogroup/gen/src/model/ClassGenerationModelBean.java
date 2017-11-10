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

    private Set<String> qualifiedModelClassNames;
    /** the package names have the suffix:'PackageName' */
    private String basePackageName;
    private String restPackageName;
    private String modelPackageName;
    private String domainPackageName;
    private String repositoryPackageName;
    private String servicePackageName;
    private String domainServicePackageName;
    private String domainMapperPackageName;
    /** SOURCE FOLDERS have the prefix 'src'. */
    private String srcFolder;
    private String srcTestFolder;
    /** SOURCE GENERATION PACKAGE NAMES have the prefix 'scr' and the suffix 'GenerationPackage'. */
    private String srcGenerationPackage;
    private String srcTestGenerationPackage;
    private String srcRestGenerationPackage;
    private String srcServiceGenerationPackage;
    private String srcDomainServiceGenerationPackage;
    private String srcDomainGenerationPackage;
    private String srcDomainMapperGenerationPackage;
    private String templateDir; // ???

}
