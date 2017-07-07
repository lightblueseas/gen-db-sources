package de.alpharogroup.gen.src.model;

import java.io.Serializable;

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
public class RepositoryClassModel implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String modelPackageName;

	private String repositoryPackageName;

	private String servicePackageName;

	private String modelQuilifiedClassName;

	private String repSpringRefClassName;

	private String repServiceClassName;

	private String serviceClassName;

	private String repositoryClassName;

	private String modelClassName;

	private String modelInstanceName;

	private String primaryKeyClassName;

}
