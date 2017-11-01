package de.alpharogroup.gen.src.model;

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
public class PomGenerationBean
{
	private String parentName;

	private String dataProjectName;
	private String dataProjectVersion;
	private String dataProjectParentVersion;
	private String persistenceunitName;
    // pom files
    private String businessPom;
    private String dataPom;
    private String domainPom;
    private String entitiesPom;
    private String initPom;
    private String restApiPom;
    private String restClientPom;
    private String restWebPom;

}
