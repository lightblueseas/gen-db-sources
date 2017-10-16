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
    private String modelPackageName;
    private String repositoryPackageName;
    private String servicePackageName;
    private String srcFolder;
    private String srcTestFolder;
    private String srcGenerationPackage;
    private String srcTestGenerationPackage;
    private String templateDir;
    private String repositoryClassTemplateFile;
    private String serviceClassTemplateFile;
    private String serviceInterfaceTemplateFile;
    private String srcServiceGenerationPackage;
    private String repositoryTestClassTemplateFile;
    private String businessPom;
    private String dataPom;

}
