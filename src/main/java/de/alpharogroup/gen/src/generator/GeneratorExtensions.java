package de.alpharogroup.gen.src.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import de.alpharogroup.file.FileExtension;
import de.alpharogroup.file.FileSuffix;
import de.alpharogroup.file.read.ReadFileExtensions;
import de.alpharogroup.file.search.PathFinder;
import de.alpharogroup.gen.src.model.ClassGenerationModelBean;
import de.alpharogroup.gen.src.model.PomGenerationBean;
import de.alpharogroup.gen.src.model.RepositoryClassModel;
import de.alpharogroup.lang.ClassExtensions;
import de.alpharogroup.lang.PackageExtensions;
import de.alpharogroup.lang.TypeArgumentsExtensions;
import de.alpharogroup.string.StringExtensions;
import de.alpharogroup.velocity.VelocityExtensions;
import de.alpharogroup.xml.XmlExtensions;

/**
 * The class {@link GeneratorExtensions} generates repositories, service and unit test classes from
 * the model classes that are in the gen.res.model package.
 */
//@Slf4j
public class GeneratorExtensions
{

	private static final String DATABASE_INITIALIZATION_CLASSNAME = "DatabaseInitialization";
	private static final String INITIALIZE_DATABASE_CLASSNAME = "InitializeDatabase";
	private static final String DB_INIT_PATH = "de/alpharogroup/db/init/";
	private static final String API_PACKAGE_PATH = "/api/";
	private static final String POM_XML_FILENAME = "pom.xml";
	private static final String GITIGNORE_FILENAME = ".gitignore";
	private static final String LOG4J_PROPERTIES_FILENAME = "log4j.properties";

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
		final List<RepositoryClassModel> repositoryModels, final PomGenerationBean pomGenerationData, final boolean withProjectPath) throws IOException
	{
		for (final RepositoryClassModel model : repositoryModels)
		{
			final VelocityContext context = new VelocityContext();
			context.put("model", model);
			// Velocity Template for the repository classes...
			final String repositoryClassPath = generator.getSrcFolder()
				+ generator.getSrcGenerationPackage() + model.getRepositoryClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplRepositoryClass(), getEntitiesProjectPath(pomGenerationData), repositoryClassPath, withProjectPath);

			// Velocity Template for the repository unit test classes...
			final String repositoryTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcTestGenerationPackage() + model.getRepositoryClassName()
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplRepositoryTestClass(), getInitProjectPath(pomGenerationData), repositoryTestClassPath, withProjectPath);

			// Velocity Template for the business services intefaces...
			final String serviceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + API_PACKAGE_PATH + model.getServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplServiceInterface(), getBusinessProjectPath(pomGenerationData), serviceInterfacePath, withProjectPath);

			// Velocity Template for the business services classes...
			final String serviceClassPath =
				  generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage()
				+ model.getModelClassName()
				+ FileSuffix.BUSINESS_SERVICE + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplServiceClass(), getBusinessProjectPath(pomGenerationData), serviceClassPath, withProjectPath);

			// Velocity Template for the business services unit test classes...
			final String serviceTestClassPath =
				  generator.getSrcTestFolder()
				+ generator.getSrcServiceGenerationPackage()
				+ model.getModelClassName()
				+ FileSuffix.BUSINESS_SERVICE
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplServiceTestClass(), getInitProjectPath(pomGenerationData), serviceTestClassPath, withProjectPath);

			// Velocity Template for the domain object...
			final String domainClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainGenerationPackage() + model.getDomainClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplDomainClass(), getDomainProjectPath(pomGenerationData), domainClassPath, withProjectPath);

			// Velocity Template for the domain mapper object...
			final String domainMapperClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainMapperGenerationPackage() + model.getModelClassName()
				+ FileSuffix.MAPPER
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplDomainMapperClass(), getDomainProjectPath(pomGenerationData), domainMapperClassPath, withProjectPath);

			// Velocity Template for the domain services intefaces...
			final String domainServiceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage()
				+ API_PACKAGE_PATH
				+ model.getDomainServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplDomainServiceInterface(), getDomainProjectPath(pomGenerationData), domainServiceInterfacePath, withProjectPath);

			// Velocity Template for the domain services classes...
			final String domainServiceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage()
				+ model.getDomainClassName()
				+ FileSuffix.DOMAIN_SERVICE
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplDomainServiceClass(), getDomainProjectPath(pomGenerationData), domainServiceClassPath, withProjectPath);

			// Velocity Template for the rest-api resource inteface...
			final String restapiResourceInterfacePath = generator.getSrcFolder()
				+ generator.getSrcRestGenerationPackage()
				+ API_PACKAGE_PATH
				+ model.getModelClassName()
				+ FileSuffix.RESOURCE
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplRestResourceInterface(), getRestApiProjectPath(pomGenerationData), restapiResourceInterfacePath, withProjectPath);

			// Velocity Template for the rest-api resource class...
			final String restapiResourceClassPath = generator.getSrcFolder()
				+ generator.getSrcRestGenerationPackage()
				+ model.getModelClassName()
				+ FileSuffix.REST_RESOURCE
				+ FileExtension.JAVA.getExtension();
			mergeProjectFile(context, generator.getTmplRestResourceClass(), getRestApiProjectPath(pomGenerationData), restapiResourceClassPath, withProjectPath);
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
		VelocityExtensions.mergeToContext(context, generationData.getDataPom(), parentDataPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), parentProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate business pom.xml
		final String businessProjectPath = getBusinessProjectPath(generationData);
		final String businessPomClassPath = businessProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getBusinessPom(), businessPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), businessProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate domain pom.xml
		final String domainProjectPath = getDomainProjectPath(generationData);
		final String domainPomClassPath = domainProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getDomainPom(), domainPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), domainProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate entities pom.xml
		final String entitiesProjectPath = getEntitiesProjectPath(generationData);
		final String entitiesPomClassPath = entitiesProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getEntitiesPom(), entitiesPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), entitiesProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate init pom.xml
		final String initProjectPath = getInitProjectPath(generationData);
		final String initSrcMainResourcesPath = initProjectPath + "/" + PathFinder.SOURCE_FOLDER_SRC_MAIN_RESOURCES;
		final String initSrcTestResourcesPath = initProjectPath + "/" + PathFinder.SOURCE_FOLDER_SRC_TEST_RESOURCES;
		final String initPomClassPath = initProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getInitPom(), initPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), initProjectPath + "/" + GITIGNORE_FILENAME);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplLog4jProperties(), initSrcMainResourcesPath+ "/" + LOG4J_PROPERTIES_FILENAME);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplH2ApplicationContextXml(),initSrcTestResourcesPath + "/" + "test-h2-applicationContext.xml");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplApplicationContextXml(), initSrcTestResourcesPath + "/" + "test-applicationContext.xml");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplTestNgXml(), initSrcTestResourcesPath + "/" + "testng.xml");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplPersistenceH2Xml(), initSrcMainResourcesPath+ "/META-INF/" + "persistence-h2.xml");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplPersistenceXml(), initSrcMainResourcesPath+ "/META-INF/" + "persistence.xml");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplJdbcH2Properties(), initSrcMainResourcesPath+ "/" + "jdbc-h2.properties");
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplJdbcProperties(), initSrcMainResourcesPath+ "/" + "jdbc.properties");

		// Generate rest-api pom.xml
		final String restApiProjectPath = getRestApiProjectPath(generationData);
		final String restApiPomClassPath = restApiProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getRestApiPom(), restApiPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), restApiProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate rest-client pom.xml
		final String restClientProjectPath = getRestClientProjectPath(generationData);
		final String restClientPomClassPath = restClientProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getRestClientPom(), restClientPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), restClientProjectPath + "/" + GITIGNORE_FILENAME);

		// Generate rest-web pom.xml
		final String restWebProjectPath = getRestWebProjectPath(generationData);
		final String restWebPomClassPath = restWebProjectPath + "/" + POM_XML_FILENAME;

		VelocityExtensions.mergeToContext(context, generationData.getRestWebPom(), restWebPomClassPath);
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplGitignore(), restWebProjectPath + "/" + GITIGNORE_FILENAME);

		// Velocity Template for the InitializeDatabase class...
		final String initDbClassPath = getClassGenerationModelBean().getSrcFolder()
			+ DB_INIT_PATH
			+ INITIALIZE_DATABASE_CLASSNAME
			+ FileExtension.JAVA.getExtension();
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplInitInitDbClass(), initProjectPath + "/" + initDbClassPath);
		final String basePackageName = getClassGenerationModelBean().getBasePackageName().replace(".", "/")+"/";

		// Velocity Template for the DatabaseInitialization class...
		final String dbInitClassPath = getClassGenerationModelBean().getSrcFolder()
			+ basePackageName
			+ DATABASE_INITIALIZATION_CLASSNAME
			+ FileExtension.JAVA.getExtension();
		VelocityExtensions.mergeToContext(context, getClassGenerationModelBean().getTmplInitDbInitClass(), initProjectPath + "/" + dbInitClassPath);

	}

	/**
	 * Generate dao classes.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void generateRepositoryClasses(final boolean withProjectPath) throws Exception
	{
		initializeQualifiedModelClassNames(getClassGenerationModelBean());

		final List<RepositoryClassModel> repositoryModels = getRepositoryClassModels(
			getClassGenerationModelBean());

		generateClasses(getClassGenerationModelBean(), repositoryModels, getPomGenerationBean(), withProjectPath);

	}

	private static String getBusinessProjectPath(final PomGenerationBean generationData)
	{
		final String businessPomClassPath = getParentProjectPath(generationData) + "/"
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
		final String domainPomClassPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-domain" ;
		return domainPomClassPath;
	}

	private static String getEntitiesProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-entities" ;
		return projectPath;
	}

	private static String getInitProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-init" ;
		return projectPath;
	}

	private static String getParentProjectPath(final PomGenerationBean generationData)
	{
		final StringBuilder sb = new StringBuilder();
//		sb.append(System.getProperty("user.home"));
//		sb.append("/");
//		sb.append("git");
		return getParentProjectPath(generationData, sb.toString().trim());
	}

	private static String getParentProjectPath(final PomGenerationBean generationData, final String basePath)
	{
		final String projectPath = generationData.getParentName();
		if(StringUtils.isNotEmpty(basePath)) {
			return basePath + "/"+ projectPath;
		}
		return projectPath;
	}

	private static PomGenerationBean getPomGenerationBean() {
		if(pomGenerationData == null) {
			final String pomGenerationModel = "PomGenerationModel.xml";
			pomGenerationData = loadPomGenerationModel(pomGenerationModel);
		}
		return pomGenerationData;
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
			model.setDomainMapperPackageName(generator.getDomainMapperPackageName());
			model.setRestPackageName(generator.getRestPackageName());

		}
		return repositoryModels;
	}

	private static String getRestApiProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-rest-api" ;
		return projectPath;
	}

	private static String getRestClientProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
			+ generationData.getParentName() + "-rest-client" ;
		return projectPath;
	}

	private static String getRestWebProjectPath(final PomGenerationBean generationData)
	{
		final String projectPath = getParentProjectPath(generationData) + "/"
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
		generationData.setClassGenerationModel(getClassGenerationModelBean());
		return generationData;
	}

	private static void mergeProjectFile(
		final VelocityContext context, final String templateFileName, final String projectPath,
		final String generatedFilePath, final boolean withProjectPath) throws IOException
	{
		String fileName;
		if(withProjectPath) {
			fileName = projectPath + "/" + generatedFilePath;
		} else {
			fileName = generatedFilePath;
		}
		VelocityExtensions.mergeToContext(context, templateFileName, fileName);
	}

}
