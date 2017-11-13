package de.alpharogroup.gen.src.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import de.alpharogroup.file.FileExtension;
import de.alpharogroup.file.FileSuffix;
import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.gen.src.model.ClassGenerationModelBean;
import de.alpharogroup.gen.src.model.PomGenerationBean;
import de.alpharogroup.gen.src.model.RepositoryClassModel;
import de.alpharogroup.gen.src.model.VelocityTemplatesModelBean;
import de.alpharogroup.lang.ClassExtensions;
import de.alpharogroup.lang.PackageExtensions;
import de.alpharogroup.lang.TypeArgumentsExtensions;
import de.alpharogroup.string.StringExtensions;
import de.alpharogroup.velocity.VelocityExtensions;
import de.alpharogroup.xml.XmlExtensions;
import lombok.experimental.UtilityClass;

/**
 * The class {@link GeneratorExtensions} generates repositories, service and unit test classes from
 * the model classes that are in the gen.res.model package.
 */
@UtilityClass
// @Slf4j
public class GeneratorExtensions
{
	private static final String ENTITIES = "-entities";

	private static final String INIT = "-init";

	private static final String REST_API = "-rest-api";

	private static final String REST_CLIENT = "-rest-client";

	private static final String REST_WEB = "-rest-web";

	/**
	 * The Constant SOURCE_FOLDER_SRC_MAIN_RESOURCES keeps the relative path for the source folder
	 * 'src/main/resources' in maven projects.
	 */
	public static final String SOURCE_FOLDER_SRC_MAIN_WEBAPP = "src/main/webapp";

	private static final String DATABASE_INITIALIZATION_CLASSNAME = "DatabaseInitialization";
	private static final String INITIALIZE_DATABASE_CLASSNAME = "InitializeDatabase";
	private static final String DB_INIT_PATH = "de/alpharogroup/db/init/";
	private static final String API_PACKAGE_PATH = "/api/";
	private static final String POM_XML_FILENAME = "pom.xml";
	private static final String GITIGNORE_FILENAME = ".gitignore";
	private static final String LOG4J_PROPERTIES_FILENAME = "log4j.properties";

	private static ClassGenerationModelBean classGenerationData;

	private static PomGenerationBean pomGenerationData;

	private static VelocityTemplatesModelBean templates;

	/**
	 * Generate classes.
	 *
	 * @param generator
	 *            the generator
	 * @param repositoryModels
	 *            the repository models
	 * @param pomGenerationData
	 *            the pom generation data
	 * @param withProjectPath
	 *            the with project path
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void generateClasses(final ClassGenerationModelBean generator,
		final List<RepositoryClassModel> repositoryModels,
		final PomGenerationBean pomGenerationData, final boolean withProjectPath) throws IOException
	{
		for (final RepositoryClassModel model : repositoryModels)
		{
			final VelocityContext context = new VelocityContext();
			context.put("model", model);
			// Velocity Template for the repository classes...
			final String repositoryClassPath = generator.getSrcFolder()
				+ generator.getSrcGenerationPackage() + model.getRepositoryClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRepositoryClass(),
				getEntitiesProjectPath(pomGenerationData), repositoryClassPath, withProjectPath);

			// Velocity Template for the repository unit test classes...
			final String repositoryTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcTestGenerationPackage() + model.getRepositoryClassName()
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRepositoryTestClass(),
				getInitProjectPath(pomGenerationData), repositoryTestClassPath, withProjectPath);

			// Velocity Template for the business services intefaces...
			final String serviceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + API_PACKAGE_PATH
				+ model.getServiceClassName() + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplServiceInterface(),
				getBusinessProjectPath(pomGenerationData), serviceInterfacePath, withProjectPath);

			// Velocity Template for the business services classes...
			final String serviceClassPath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + model.getModelClassName()
				+ FileSuffix.BUSINESS_SERVICE + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplServiceClass(),
				getBusinessProjectPath(pomGenerationData), serviceClassPath, withProjectPath);

			// Velocity Template for the business services unit test classes...
			final String serviceTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcServiceGenerationPackage() + model.getModelClassName()
				+ FileSuffix.BUSINESS_SERVICE + FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplServiceTestClass(),
				getInitProjectPath(pomGenerationData), serviceTestClassPath, withProjectPath);

			// Velocity Template for the domain object...
			final String domainClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainGenerationPackage() + model.getDomainClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplDomainClass(),
				getDomainProjectPath(pomGenerationData), domainClassPath, withProjectPath);

			// Velocity Template for the domain mapper object...
			final String domainMapperClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainMapperGenerationPackage() + model.getModelClassName()
				+ FileSuffix.MAPPER + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplDomainMapperClass(),
				getDomainProjectPath(pomGenerationData), domainMapperClassPath, withProjectPath);

			// Velocity Template for the domain services intefaces...
			final String domainServiceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage() + API_PACKAGE_PATH
				+ model.getDomainServiceClassName() + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplDomainServiceInterface(),
				getDomainProjectPath(pomGenerationData), domainServiceInterfacePath,
				withProjectPath);

			// Velocity Template for the domain services classes...
			final String domainServiceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage() + model.getDomainClassName()
				+ FileSuffix.DOMAIN_SERVICE + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplDomainServiceClass(),
				getDomainProjectPath(pomGenerationData), domainServiceClassPath, withProjectPath);

			// Velocity Template for the rest-api resource inteface...
			final String restapiResourceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcRestGenerationPackage() + API_PACKAGE_PATH
				+ model.getModelClassName() + FileSuffix.RESOURCE
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRestResourceInterface(),
				getRestApiProjectPath(pomGenerationData), restapiResourceInterfacePath,
				withProjectPath);

			// Velocity Template for the rest-api resource class...
			final String restapiResourceClassPath = generator.getSrcFolder()
				+ generator.getSrcRestGenerationPackage() + model.getModelClassName()
				+ FileSuffix.REST_RESOURCE + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRestResourceClass(),
				getRestApiProjectPath(pomGenerationData), restapiResourceClassPath,
				withProjectPath);

			// Velocity Template for the rest-client classes...
			final String restClientClassPath = generator.getSrcFolder()
				+ generator.getSrcRestGenerationPackage() + "/client/" + model.getModelClassName()
				+ FileSuffix.REST_CLIENT + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRestClientClass(),
				getRestClientProjectPath(pomGenerationData), restClientClassPath, withProjectPath);

			// Velocity Template for the rest-client unit test classes...
			final String restClientTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcRestGenerationPackage() + "/client/" + model.getModelClassName()
				+ FileSuffix.REST_CLIENT + FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, getVelocityTemplates().getTmplRestClientTestClass(),
				getRestClientProjectPath(pomGenerationData), restClientTestClassPath,
				withProjectPath);
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
		final String parentDataPomClassPath = parentProjectPath + "/" + POM_XML_FILENAME;
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplDataPom(),
			parentDataPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			parentProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate business pom.xml
		final String businessProjectPath = getBusinessProjectPath(generationData);
		final String businessPomClassPath = businessProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplBusinessPom(),
			businessPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			businessProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate domain pom.xml
		final String domainProjectPath = getDomainProjectPath(generationData);
		final String domainPomClassPath = domainProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplDomainPom(),
			domainPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			domainProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate entities pom.xml
		final String entitiesProjectPath = getEntitiesProjectPath(generationData);
		final String entitiesPomClassPath = entitiesProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplEntitiesPom(),
			entitiesPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			entitiesProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate init pom.xml
		final String initProjectPath = getInitProjectPath(generationData);
		final String initSrcMainResourcesPath = initProjectPath + "/"
			+ PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES;
		final String initSrcTestResourcesPath = initProjectPath + "/"
			+ PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES;
		final String initPomClassPath = initProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplInitPom(),
			initPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			initProjectPath + "/" + GITIGNORE_FILENAME);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplLog4jProperties(),
			initSrcMainResourcesPath + "/" + LOG4J_PROPERTIES_FILENAME);
		VelocityExtensions.mergeToContext(context,
			getVelocityTemplates().getTmplH2ApplicationContextXml(),
			initSrcTestResourcesPath + "/" + "test-h2-applicationContext.xml");
		VelocityExtensions.mergeToContext(context,
			getVelocityTemplates().getTmplApplicationContextXml(),
			initSrcTestResourcesPath + "/" + "test-applicationContext.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplTestNgXml(),
			initSrcTestResourcesPath + "/" + "testng.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplPersistenceH2Xml(),
			initSrcMainResourcesPath + "/META-INF/" + "persistence-h2.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplPersistenceXml(),
			initSrcMainResourcesPath + "/META-INF/" + "persistence.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplJdbcH2Properties(),
			initSrcMainResourcesPath + "/" + "jdbc-h2.properties");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplJdbcProperties(),
			initSrcMainResourcesPath + "/" + "jdbc.properties");

		// Generate rest-api pom.xml
		final String restApiProjectPath = getRestApiProjectPath(generationData);
		final String restApiPomClassPath = restApiProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplRestApiPom(),
			restApiPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			restApiProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate rest-client pom.xml
		final String restClientProjectPath = getRestClientProjectPath(generationData);
		final String restClientPomClassPath = restClientProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplRestClientPom(),
			restClientPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			restClientProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate rest-web pom.xml
		final String restWebProjectPath = getRestWebProjectPath(generationData);
		final String restWebSrcMainResourcesPath = restWebProjectPath + "/"
			+ PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES;
		final String restWebSrcMainWebappPath = restWebProjectPath + "/"
			+ SOURCE_FOLDER_SRC_MAIN_WEBAPP;
		final String restWebPomClassPath = restWebProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplRestWebPom(),
			restWebPomClassPath);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplGitignore(),
			restWebProjectPath + "/" + GITIGNORE_FILENAME);
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplLog4jProperties(),
			restWebSrcMainResourcesPath + "/" + LOG4J_PROPERTIES_FILENAME);

		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplPersistenceH2Xml(),
			restWebSrcMainResourcesPath + "/META-INF/" + "persistence-h2.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplPersistenceXml(),
			restWebSrcMainResourcesPath + "/META-INF/" + "persistence.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplJdbcH2Properties(),
			restWebSrcMainResourcesPath + "/" + "jdbc-h2.properties");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplJdbcProperties(),
			restWebSrcMainResourcesPath + "/" + "jdbc.properties");

		VelocityExtensions.mergeToContext(context,
			getVelocityTemplates().getTmplDataApplicationContextXml(),
			restWebSrcMainResourcesPath + "/" + "data-application-context.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplIndexJsp(),
			restWebSrcMainWebappPath + "/" + "index.jsp");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplManifestMf(),
			restWebSrcMainWebappPath + "/META-INF/" + "MANIFEST.MF");
		VelocityExtensions.mergeToContext(context,
			getVelocityTemplates().getTmplProjectProperties(),
			restWebSrcMainResourcesPath + "/" + "project.properties");
		VelocityExtensions.mergeToContext(context,
			getVelocityTemplates().getTmplWebApplicationContextXml(),
			restWebSrcMainResourcesPath + "/" + "web-application-context.xml");
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplWebXml(),
			restWebSrcMainWebappPath + "/WEB-INF/" + "web.xml");

		// Velocity Template for the InitializeDatabase class...
		final String initializeDatabaseClassPath = getClassGenerationModelBean().getSrcFolder()
			+ DB_INIT_PATH + INITIALIZE_DATABASE_CLASSNAME + FileExtension.JAVA.getExtension();
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplInitInitDbClass(),
			initProjectPath + "/" + initializeDatabaseClassPath);
		final String basePackageName = getClassGenerationModelBean().getBasePackageName()
			.replace(".", "/") + "/";

		// Velocity Template for the DatabaseInitialization class...
		final String databaseInitializationClassPath = getClassGenerationModelBean().getSrcFolder()
			+ basePackageName + DATABASE_INITIALIZATION_CLASSNAME
			+ FileExtension.JAVA.getExtension();
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplInitDbInitClass(),
			initProjectPath + "/" + databaseInitializationClassPath);


		// Velocity Template for the InitializeDatabase class...
		final String applicationJettyRunnerClassPath = getClassGenerationModelBean()
			.getSrcTestFolder() + basePackageName + "ApplicationJettyRunner"
			+ FileExtension.JAVA.getExtension();
		VelocityExtensions.mergeToContext(context, getVelocityTemplates().getTmplJettyRunnerClass(),
			restWebProjectPath + "/" + applicationJettyRunnerClassPath);


	}

	/**
	 * Generate repository classes.
	 *
	 * @param withProjectPath
	 *            the with project path
	 * @throws Exception
	 *             the exception
	 */
	public static void generateRepositoryClasses(final boolean withProjectPath) throws Exception
	{
		initializeQualifiedModelClassNames(getClassGenerationModelBean());

		final List<RepositoryClassModel> repositoryModels = getRepositoryClassModels(
			getClassGenerationModelBean());

		generateClasses(getClassGenerationModelBean(), repositoryModels, getPomGenerationBean(),
			withProjectPath);

	}

	private static String getBusinessProjectPath(final PomGenerationBean generationData)
	{
		final String businessPomClassPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-business";
		return businessPomClassPath;
	}

	private static ClassGenerationModelBean getClassGenerationModelBean()
	{
		if (classGenerationData == null)
		{
			final String classGenerationModel = "ClassGenerationModel.xml";
			classGenerationData = loadClassGenerationModel(classGenerationModel);
		}
		return classGenerationData;
	}

	private static String getDomainProjectPath(final PomGenerationBean generationData)
	{
		final String domainPomClassPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-domain";
		return domainPomClassPath;
	}

	private static String getEntitiesProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + ENTITIES;
		return projectPath;
	}

	private static String getInitProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + INIT;
		return projectPath;
	}

	private static String getParentProjectPath(final PomGenerationBean generationData)
	{
		final StringBuilder sb = new StringBuilder();
		// sb.append(System.getProperty("user.home"));
		// sb.append("/");
		// sb.append("git");
		return getParentProjectPath(generationData, sb.toString().trim());
	}

	private static String getParentProjectPath(final PomGenerationBean generationData,
		final String basePath)
	{
		final String projectPath = generationData.getParentName();
		if (StringUtils.isNotEmpty(basePath))
		{
			return basePath + "/" + projectPath;
		}
		return projectPath;
	}

	private static PomGenerationBean getPomGenerationBean()
	{
		if (pomGenerationData == null)
		{
			final String pomGenerationModel = "PomGenerationModel.xml";
			pomGenerationData = loadPomGenerationModel(pomGenerationModel);
		}
		return pomGenerationData;
	}

	private static VelocityTemplatesModelBean getVelocityTemplates()
	{
		if (templates == null)
		{
			final String velocityTemplatesModelBean = "VelocityTemplatesModelBean.xml";
			templates = XmlExtensions.loadObject(velocityTemplatesModelBean);
		}
		return templates;
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
			final String domainClassName = modelClassName.substring(0, modelClassName.length() - 1);
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
			// NICE_TO_HAVE move package set out of the loop...
			if(StringUtils.isNotEmpty(generator.getServicePackageName())) {
				model.setServicePackageName(generator.getServicePackageName());
			} else {
				model.setServicePackageName(generator.getBasePackageName() + ".service");
			}
			if(StringUtils.isNotEmpty(generator.getDomainServicePackageName())) {
				model.setDomainServicePackageName(generator.getDomainServicePackageName());
			} else {
				model.setDomainServicePackageName(generator.getBasePackageName() + ".domain.service");
			}
			if(StringUtils.isNotEmpty(generator.getModelPackageName())) {
				final String modelPackageName = generator.getModelPackageName();
				model.setModelPackageName(modelPackageName);
			} else {
				model.setModelPackageName(generator.getBasePackageName() + ".entities");
			}
			if(StringUtils.isNotEmpty(generator.getRepositoryPackageName())) {
				model.setRepositoryPackageName(generator.getRepositoryPackageName());
			} else {
				model.setRepositoryPackageName(generator.getBasePackageName() + ".repositories");
			}
			if(StringUtils.isNotEmpty(generator.getDomainPackageName())) {
				model.setDomainPackageName(generator.getDomainPackageName());
			} else {
				model.setDomainPackageName(generator.getBasePackageName() + ".domain");
			}
			if(StringUtils.isNotEmpty(generator.getDomainMapperPackageName())) {
				model.setDomainMapperPackageName(generator.getDomainMapperPackageName());
			} else {
				model.setDomainMapperPackageName(generator.getBasePackageName() + ".domain.mapper");
			}
			if(StringUtils.isNotEmpty(generator.getRestPackageName())) {
				model.setRestPackageName(generator.getRestPackageName());
			} else {
				model.setRestPackageName(generator.getBasePackageName() + ".rest");
			}

			final String repositoryClassName = modelClassName + FileSuffix.REPOSITORY;

			final String serviceInterfaceName = modelClassName + FileSuffix.SERVICE;
			final String repSpringRefClassName = StringExtensions
				.firstCharacterToLowerCase(repositoryClassName);
			final String repServiceClassName = StringExtensions
				.firstCharacterToLowerCase(serviceInterfaceName);
			model.setServiceClassName(serviceInterfaceName);
			model.setRepServiceClassName(repServiceClassName);

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
			model.setDomainServiceClassName(domainServiceInterfaceName);

		}
		return repositoryModels;
	}

	private static String getRestApiProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + REST_API;
		return projectPath;
	}

	private static String getRestClientProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + REST_CLIENT;
		return projectPath;
	}

	private static String getRestWebProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + REST_WEB;
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
	public static void initializeQualifiedModelClassNames(
		final ClassGenerationModelBean generationData) throws Exception
	{
		if(StringUtils.isEmpty(generationData.getModelPackageName())) {
			generationData.setModelPackageName(generationData.getBasePackageName() + ".entities");
		}
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
	public static ClassGenerationModelBean loadClassGenerationModel(
		final String classGenerationModel)
	{
		final ClassGenerationModelBean generator = XmlExtensions.loadObject(classGenerationModel);
		final String basePackageName = generator.getBasePackageName();
		final String basePackagePath = PackageExtensions.getPackagePath(basePackageName);
		if(StringUtils.isEmpty(generator.getServicePackageName())) {
			generator.setServicePackageName(basePackageName + ".service");
		}
		if(StringUtils.isEmpty(generator.getDomainServicePackageName())) {
			generator.setDomainServicePackageName(basePackageName + ".domain.service");
		}
		if(StringUtils.isEmpty(generator.getModelPackageName())) {
			generator.setModelPackageName(basePackageName + ".entities");
		}
		if(StringUtils.isEmpty(generator.getRepositoryPackageName())) {
			generator.setRepositoryPackageName(basePackageName + ".repositories");
		}
		if(StringUtils.isEmpty(generator.getDomainPackageName())) {
			generator.setDomainPackageName(basePackageName + ".domain");
		}
		if(StringUtils.isEmpty(generator.getDomainMapperPackageName())) {
			generator.setDomainMapperPackageName(basePackageName + ".domain.mapper");
		}
		if(StringUtils.isEmpty(generator.getRestPackageName())) {
			generator.setRestPackageName(basePackageName + ".rest");
		}
		if(StringUtils.isEmpty(generator.getSrcFolder())) {
			generator.setSrcFolder(PathFinder.SOURCE_FOLDER_SRC_MAIN_JAVA + "/");
		}
		if(StringUtils.isEmpty(generator.getSrcTestFolder())) {
			generator.setSrcTestFolder(PathFinder.SOURCE_FOLDER_SRC_TEST_JAVA+ "/");
		}
		if(StringUtils.isEmpty(generator.getSrcGenerationPackage())) {
			generator.setSrcGenerationPackage(basePackagePath + "/repositories/");
		}
		if(StringUtils.isEmpty(generator.getSrcRestGenerationPackage())) {
			generator.setSrcRestGenerationPackage(basePackagePath + "/rest/");
		}
		if(StringUtils.isEmpty(generator.getSrcTestGenerationPackage())) {
			generator.setSrcTestGenerationPackage(basePackagePath + "/repositories/");
		}
		if(StringUtils.isEmpty(generator.getSrcServiceGenerationPackage())) {
			generator.setSrcServiceGenerationPackage(basePackagePath + "/service/");
		}
		if(StringUtils.isEmpty(generator.getSrcDomainGenerationPackage())) {
			generator.setSrcDomainGenerationPackage(basePackagePath + "/domain/");
		}
		if(StringUtils.isEmpty(generator.getSrcDomainMapperGenerationPackage())) {
			generator.setSrcDomainMapperGenerationPackage(basePackagePath + "/mapper/");
		}
		if(StringUtils.isEmpty(generator.getSrcDomainServiceGenerationPackage())) {
			generator.setSrcDomainServiceGenerationPackage(basePackagePath + "/domain/service/");
		}




		return generator;
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
		final PomGenerationBean pomGeneration = XmlExtensions.loadObject(pomGenerationBean);
		return pomGeneration;
	}

	private static void mergeProjectFile(final VelocityContext context,
		final String templateFileName, final String projectPath, final String generatedFilePath,
		final boolean withProjectPath) throws IOException
	{
		String fileName;
		if (withProjectPath)
		{
			fileName = projectPath + "/" + generatedFilePath;
		}
		else
		{
			fileName = generatedFilePath;
		}
		VelocityExtensions.mergeToContext(context, templateFileName, fileName);
	}

}
