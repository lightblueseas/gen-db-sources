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
import de.alpharogroup.gen.src.model.ClassGenerationModelBean;
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

	private static ClassGenerationModelBean classGenerationData;

	private static PomGenerationBean pomGenerationData;

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
	public static void generateClasses(final ClassGenerationModelBean generator,
		final List<RepositoryClassModel> repositoryModels, final PomGenerationBean pomGenerationData) throws IOException
	{
		for (final RepositoryClassModel model : repositoryModels)
		{

			final VelocityContext context = new VelocityContext();
			context.put("model", model);
			// Velocity Template for the repository classes...
			final String entitiesProjectPath = getEntitiesProjectPath(pomGenerationData);
			final String repositoryClassPath = generator.getSrcFolder()
				+ generator.getSrcGenerationPackage() + model.getRepositoryClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getRepositoryClassTemplateFile(), entitiesProjectPath + "/" + repositoryClassPath);

			// Velocity Template for the repository unit test classes...
			final String initProjectPath = getInitProjectPath(pomGenerationData);
			final String repositoryTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcTestGenerationPackage() + model.getRepositoryClassName()
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getRepositoryTestClassTemplateFile(), initProjectPath + "/" + repositoryTestClassPath);
//
//			// Velocity Template for the InitializeDatabase class...
//			final String initDbClassPath = generator.getSrcFolder()
//				+ "de/alpharogroup/db/init/"
//				+ "InitializeDatabase"
//				+ FileExtension.JAVA.getExtension();
//			mergeToContext(context, generator.getInitInitDbClassTemplateFile(), initProjectPath + "/" + initDbClassPath);

			// Velocity Template for the business services intefaces...
			final String businessProjectPath = getBusinessProjectPath(pomGenerationData);
			final String serviceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + "/api/" + model.getServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getServiceInterfaceTemplateFile(), businessProjectPath + "/" + serviceInterfaceClassPath);

			// Velocity Template for the business services classes...
			final String serviceClassPath =
				  generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage()
				+ model.getModelClassName()
				+ "Business"
				+ FileSuffix.SERVICE + FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getServiceClassTemplateFile(), businessProjectPath + "/" + serviceClassPath);

			// Velocity Template for the domain object...
			final String domainProjectPath = getDomainProjectPath(pomGenerationData);
			final String domainClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainGenerationPackage() + model.getDomainClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainClassTemplateFile(), domainProjectPath + "/" + domainClassPath);

			// Velocity Template for the domain mapper object...
			final String domainMapperClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainMapperGenerationPackage() + model.getModelClassName()
				+ "Mapper"
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainMapperClassTemplateFile(), domainProjectPath + "/" + domainMapperClassPath);

			// Velocity Template for the domain services intefaces...
			final String domainServiceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage()
				+ "/api/"
				+ model.getDomainServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainServiceInterfaceTemplateFile(), domainProjectPath + "/" + domainServiceInterfaceClassPath);

			// Velocity Template for the domain services classes...
			final String domainServiceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage()
				+ model.getDomainClassName()
				+ "Domain"
				+ FileSuffix.SERVICE
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainServiceClassTemplateFile(), domainProjectPath + "/" + domainServiceClassPath);
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
		final String parentProjectPath = getParentProjectPath(generationData);
		final String parentDataPomClassPath = parentProjectPath + "/"
			 + POM_XML_FILENAME;
		mergeToContext(context, generationData.getDataPom(), parentDataPomClassPath);

		// Generate business pom.xml
		final String businessProjectPath = getBusinessProjectPath(generationData);
		final String businessPomClassPath = businessProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getBusinessPom(), businessPomClassPath);

		// Generate domain pom.xml
		final String domainProjectPath = getDomainProjectPath(generationData);
		final String domainPomClassPath = domainProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getDomainPom(), domainPomClassPath);

		// Generate entities pom.xml
		final String entitiesProjectPath = getEntitiesProjectPath(generationData);
		final String entitiesPomClassPath = entitiesProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getEntitiesPom(), entitiesPomClassPath);

		// Generate init pom.xml
		final String initProjectPath = getInitProjectPath(generationData);
		final String initPomClassPath = initProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getInitPom(), initPomClassPath);

		// Generate rest-api pom.xml
		final String restApiProjectPath = getRestApiProjectPath(generationData);
		final String restApiPomClassPath = restApiProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestApiPom(), restApiPomClassPath);

		// Generate rest-client pom.xml
		final String restClientProjectPath = getRestClientProjectPath(generationData);
		final String restClientPomClassPath = restClientProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestClientPom(), restClientPomClassPath);

		// Generate rest-web pom.xml
		final String restWebProjectPath = getRestWebProjectPath(generationData);
		final String restWebPomClassPath = restWebProjectPath + "/" + POM_XML_FILENAME;

		mergeToContext(context, generationData.getRestWebPom(), restWebPomClassPath);

		// Velocity Template for the InitializeDatabase class...
		final String initDbClassPath = getClassGenerationModelBean().getSrcFolder()
			+ "de/alpharogroup/db/init/"
			+ "InitializeDatabase"
			+ FileExtension.JAVA.getExtension();
		mergeToContext(context, getClassGenerationModelBean().getInitInitDbClassTemplateFile(), initProjectPath + "/" + initDbClassPath);
		final String basePackageName = getClassGenerationModelBean().getBasePackageName().replace(".", "/")+"/";

		// Velocity Template for the DatabaseInitialization class...
		final String dbInitClassPath = getClassGenerationModelBean().getSrcFolder()
			+ basePackageName
			+ "DatabaseInitialization"
			+ FileExtension.JAVA.getExtension();
		mergeToContext(context, getClassGenerationModelBean().getInitDbInitClassTemplateFile(), initProjectPath + "/" + dbInitClassPath);

	}

	/**
	 * Generate dao classes.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void generateRepositoryClasses() throws Exception
	{
		initializeQualifiedModelClassNames(getClassGenerationModelBean());

		final List<RepositoryClassModel> repositoryModels = getRepositoryClassModels(
			getClassGenerationModelBean());

		generateClasses(getClassGenerationModelBean(), repositoryModels, getPomGenerationBean());

	}

	private static String getBusinessProjectPath(final PomGenerationBean generationData)
	{
		final String businessPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-business";
		return businessPomClassPath;
	}

	private static ClassGenerationModelBean getClassGenerationModelBean() {
		if(classGenerationData == null) {
			final String classGenerationModel = "ClassGenerationModel.xml";
			classGenerationData = loadClassGenerationModel(classGenerationModel);
		}
		return classGenerationData;
	}



	private static String getDomainProjectPath(final PomGenerationBean generationData)
	{
		final String domainPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-domain" ;
		return domainPomClassPath;
	}


	private static String getEntitiesProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-entities" ;
		return projectPath;
	}

	private static String getInitProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-init" ;
		return projectPath;
	}

	private static String getParentProjectPath(final PomGenerationBean generationData)
	{
		final String domainPomClassPath = generationData.getParentName();
		return domainPomClassPath;
	}

	private static PomGenerationBean getPomGenerationBean() {
		if(pomGenerationData == null) {
			final String pomGenerationModel = "PomGenerationModel.xml";
			pomGenerationData = loadPomGenerationModel(pomGenerationModel);
		}
		return pomGenerationData;
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
		final ClassGenerationModelBean generator)
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

	private static String getRestApiProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-api" ;
		return projectPath;
	}

	private static String getRestClientProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-client" ;
		return projectPath;
	}

	private static String getRestWebProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-rest-web" ;
		return projectPath;
	}


	/**
	 * Reads the qualified entity class names from the entities in the specified package.
	 *
	 * @param generationData
	 *            the generation data
	 * @throws Exception
	 *             the exception
	 */
	public static void initializeQualifiedModelClassNames(final ClassGenerationModelBean generationData)
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
	public static ClassGenerationModelBean loadClassGenerationModel(final String classGenerationModel)
	{
		final InputStream is = ClassExtensions.getResourceAsStream(classGenerationModel);
		final String xml = ReadFileExtensions.inputStream2String(is);
		final ClassGenerationModelBean generationData = XmlExtensions.toObjectWithXStream(xml);
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
