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
public class PomGenerationBean
{
	String absoluteProjectPath;
	String basePackageName;

	ClassGenerationModelBean classGenerationModel;

	String dataProjectName;

	String dataProjectParentVersion;
	String dataProjectVersion;
	String parentName;
	String password;
    String persistenceunitName;

}
