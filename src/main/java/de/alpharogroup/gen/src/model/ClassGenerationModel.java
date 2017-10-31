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
public class ClassGenerationModel {

    private Set<String> qualifiedModelClassNames;
    private String basePackageName;
    private String modelPackageName;
    private String domainPackageName;
    private String repositoryPackageName;
    private String servicePackageName;
    private String domainServicePackageName;
    private String domainMapperPackageName;
    private String srcFolder;
    private String srcTestFolder;
    private String srcGenerationPackage;
    private String srcTestGenerationPackage;
    private String templateDir;
    private String repositoryClassTemplateFile;
    private String serviceClassTemplateFile;
    private String serviceInterfaceTemplateFile;
    private String domainServiceClassTemplateFile;
    private String domainServiceInterfaceTemplateFile;
    private String domainMapperClassTemplateFile;
    private String srcServiceGenerationPackage;
    private String srcDomainServiceGenerationPackage;
    private String srcDomainGenerationPackage;
    private String srcDomainMapperGenerationPackage;
    private String repositoryTestClassTemplateFile;
    private String businessPom;
    private String dataPom;
    private String domainClassTemplateFile;

}
