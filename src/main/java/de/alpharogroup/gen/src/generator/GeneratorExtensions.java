package de.alpharogroup.gen.src.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import de.alpharogroup.file.FileExtension;
import de.alpharogroup.file.FileSuffix;
import de.alpharogroup.file.create.CreateFileExtensions;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.gen.src.model.ClassGenerationModel;
import de.alpharogroup.gen.src.model.PomGenerationBean;
import de.alpharogroup.gen.src.model.RepositoryClassModel;
import de.alpharogroup.lang.ClassExtensions;
import de.alpharogroup.lang.PackageExtensions;
import de.alpharogroup.lang.TypeArgumentsExtensions;
import de.alpharogroup.string.StringExtensions;
import de.alpharogroup.xml.XmlExtensions;

/**
 * The class {@link GeneratorExtensions} generates repositories, service and unit test classes from
 * the model classes that are in the gen.res.model package.
 */
public class GeneratorExtensions
{

	private static final String POM_XML_FILENAME = "pom.xml";

	/**
	 * Generate classes.
	 *
	 * @param generator
	 *            the generator
	 * @param repositoryModels
	 *            the repository models
	 * @throws IOException
	 *             the IO exception
	 */
	public static void generateClasses(final ClassGenerationModel generator,
		final List<RepositoryClassModel> repositoryModels) throws IOException
	{
		for (final RepositoryClassModel model : repositoryModels)
		{

			final VelocityContext context = new VelocityContext();
			context.put("model", model);
			// Velocity Template for the repository classes...
			final String repositoryClassPath = generator.getSrcFolder()
				+ generator.getSrcGenerationPackage() + model.getRepositoryClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getRepositoryClassTemplateFile(), repositoryClassPath);

			// Velocity Template for the repository unit test classes...
			final String repositoryTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcTestGenerationPackage() + model.getRepositoryClassName()
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getRepositoryTestClassTemplateFile(), repositoryTestClassPath);

			// Velocity Template for the business services intefaces...
			final String serviceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + model.getServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getServiceInterfaceTemplateFile(), serviceInterfaceClassPath);

			// Velocity Template for the business services classes...
			final String serviceClassPath =
				  generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage()
				+ model.getModelClassName()
				+ "Business" + FileSuffix.SERVICE + FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getServiceClassTemplateFile(), serviceClassPath);

			// Velocity Template for the domain object...
			final String domainClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainGenerationPackage() + model.getDomainClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainClassTemplateFile(), domainClassPath);

			// Velocity Template for the domain mapper object...
			final String domainMapperClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainMapperGenerationPackage() + model.getModelClassName()
				+ "Mapper"
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainMapperClassTemplateFile(), domainMapperClassPath);

			// Velocity Template for the domain services intefaces...
			final String domainServiceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage() + model.getDomainServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainServiceInterfaceTemplateFile(), domainServiceInterfaceClassPath);
		}
	}


	/**
	 * Generate pom files.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void generatePomFiles() throws Exception
	{
		final String pomGenerationModel = "PomGenerationModel.xml";

		final PomGenerationBean generationData = loadPomGenerationModel(pomGenerationModel);
		final VelocityContext context = new VelocityContext();
		context.put("model", generationData);

		// Generate parent data pom.xml
		final String parentDataPomClassPath = generationData.getParentName() + "/"
			 + POM_XML_FILENAME;
		mergeToContext(context, generationData.getDataPom(), parentDataPomClassPath);

		// Generate business pom.xml
		final String businessPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-business" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getBusinessPom(), businessPomClassPath);

		// Generate domain pom.xml
		final String domainPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-domain" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getDomainPom(), domainPomClassPath);

		// Generate entities pom.xml
		final String entitiesPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-entities" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getEntitiesPom(), entitiesPomClassPath);

		// Generate init pom.xml
		final String initPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-init" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getInitPom(), initPomClassPath);

		// Generate rest-api pom.xml
		final String restApiPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-api" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestApiPom(), restApiPomClassPath);

		// Generate rest-client pom.xml
		final String restClientPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-client" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestClientPom(), restClientPomClassPath);

		// Generate rest-web pom.xml
		final String restWebPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-web" + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestWebPom(), restWebPomClassPath);

	}

	/**
	 * Generate dao classes.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void generateRepositoryClasses() throws Exception
	{
		final String classGenerationModel = "ClassGenerationModel.xml";

		final ClassGenerationModel generationData = loadClassGenerationModel(classGenerationModel);

		initializeQualifiedModelClassNames(generationData);

		final List<RepositoryClassModel> repositoryModels = getRepositoryClassModels(
			generationData);

		generateClasses(generationData, repositoryModels);

	}

	public static void getPomGenerationModel(final PomGenerationBean generator)
	{

	}

	/**
	 * Gets the repository class models.
	 *
	 * @param generator
	 *            the generator
	 * @return the repository class models
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<RepositoryClassModel> getRepositoryClassModels(
		final ClassGenerationModel generator)
	{
		final List<RepositoryClassModel> repositoryModels = new ArrayList<>();
		for (final String clazz : generator.getQualifiedModelClassNames())
		{
			final RepositoryClassModel model = new RepositoryClassModel();

			final String modelClassName = clazz.substring(clazz.lastIndexOf(".") + 1,
				clazz.length());
			final String domainClassName = modelClassName.substring(0, modelClassName.length()-1);
			if (modelClassName.equals("package-info"))
			{
				continue;
			}

			try
			{
				final Class modelClass = Class.forName(clazz);
				final Class primaryKeyClassType = TypeArgumentsExtensions
					.getFirstTypeArgument(ClassExtensions.getBaseClass(modelClass), modelClass);
				final String className = primaryKeyClassType.getName();
				model.setPrimaryKeyClassName(className);
			}
			catch (final ClassNotFoundException e)
			{
				throw new RuntimeException(e.getMessage()
					+ "\n by get the first type argument from the entity class " + modelClassName);
			}


			repositoryModels.add(model);
			final String repositoryClassName = modelClassName + FileSuffix.REPOSITORY;

			final String serviceInterfaceName = modelClassName + FileSuffix.SERVICE;
			final String repSpringRefClassName = StringExtensions
				.firstCharacterToLowerCase(repositoryClassName);
			final String repServiceClassName = StringExtensions
				.firstCharacterToLowerCase(serviceInterfaceName);
			model.setServiceClassName(serviceInterfaceName);
			model.setRepServiceClassName(repServiceClassName);
			model.setServicePackageName(generator.getServicePackageName());
			model.setDomainServicePackageName(generator.getDomainServicePackageName());
			model.setModelPackageName(generator.getModelPackageName());
			model.setRepositoryPackageName(generator.getRepositoryPackageName());
			model.setModelQuilifiedClassName(clazz);
			model.setRepSpringRefClassName(repSpringRefClassName);
			model.setRepositoryClassName(repositoryClassName);
			model.setModelClassName(modelClassName);
			model.setModelInstanceName(
				StringExtensions.firstCharacterToLowerCase(model.getModelClassName()));
			model.setDomainClassName(domainClassName);

			final String domainServiceInterfaceName = domainClassName + FileSuffix.SERVICE;
			final String repDomainServiceClassName = StringExtensions
				.firstCharacterToLowerCase(domainServiceInterfaceName);
			model.setRepDomainServiceClassName(repDomainServiceClassName);
			model.setDomainServiceClassName(domainServiceInterfaceName );
			model.setDomainPackageName(generator.getDomainPackageName());

		}
		return repositoryModels;
	}


	/**
	 * Reads the qualified entity class names from the entities in the specified package.
	 *
	 * @param generationData
	 *            the generation data
	 * @throws Exception
	 *             the exception
	 */
	public static void initializeQualifiedModelClassNames(final ClassGenerationModel generationData)
		throws Exception
	{
		final Set<String> qualifiedModelClassNames = PackageExtensions
			.scanClassNames(generationData.getModelPackageName());
		generationData.setQualifiedModelClassNames(qualifiedModelClassNames);
	}

	/**
	 * Load class generation model.
	 *
	 * @param classGenerationModel
	 *            the class generation model
	 * @return the class generation model
	 */
	public static ClassGenerationModel loadClassGenerationModel(final String classGenerationModel)
	{
		final InputStream is = ClassExtensions.getResourceAsStream(classGenerationModel);
		final String xml = ReadFileExtensions.inputStream2String(is);
		final ClassGenerationModel generationData = XmlExtensions.toObjectWithXStream(xml);
		return generationData;
	}

	/**
	 * Load class generation model.
	 *
	 * @param pomGenerationBean
	 *            the pom generation bean
	 * @return the class generation model
	 */
	public static PomGenerationBean loadPomGenerationModel(final String pomGenerationBean)
	{
		final InputStream is = ClassExtensions.getResourceAsStream(pomGenerationBean);
		final String xml = ReadFileExtensions.inputStream2String(is);
		final PomGenerationBean generationData = XmlExtensions.toObjectWithXStream(xml);
		return generationData;
	}

	/**
	 * Merge to context.
	 *
	 * @param context
	 *            the context
	 * @param templateFileName
	 *            the template file name
	 * @param className
	 *            the class name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @deprecated use instead the same name method from velocity extensions.
	 */
	@Deprecated
	private static void mergeToContext(final VelocityContext context, final String templateFileName, final String className) throws IOException
	{
		File generatedClassFile;
		generatedClassFile = new File(className);
		CreateFileExtensions.newFileQuietly(generatedClassFile);
		final BufferedWriter domainServiceInterfaceWriter = new BufferedWriter(
			new FileWriter(className));

		final Template domainServiceInterfaceTemplate = Velocity
			.getTemplate(templateFileName);
		domainServiceInterfaceTemplate.merge(context, domainServiceInterfaceWriter);
		domainServiceInterfaceWriter.flush();
		domainServiceInterfaceWriter.close();
	}

}
