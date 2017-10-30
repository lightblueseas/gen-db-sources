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

	/**
	 * The main method.
	 *
	 * @param args
	 *            the args
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String[] args) throws Exception
	{
		 generateRepositoryClasses();
//		final String pomGenerationBean = "PomGenerationModel.xml";
//		loadPomGenerationModel(pomGenerationBean);
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

		final Template businessPomTemplate = Velocity.getTemplate(generationData.getBusinessPom());

		final String businessPomClassPath = generationData.getParentName() + "/"
			+ generationData.getParentName() + "-business" + "/" + "pom.xml";
		final File f = new File(businessPomClassPath);
		CreateFileExtensions.newFileQuietly(f);
		final BufferedWriter writer = new BufferedWriter(new FileWriter(businessPomClassPath));

		businessPomTemplate.merge(context, writer);
		writer.flush();
		writer.close();


	}

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
			final Template repositoryClassTemplate = Velocity
				.getTemplate(generator.getRepositoryClassTemplateFile());

			final String repositoryClassPath = generator.getSrcFolder()
				+ generator.getSrcGenerationPackage() + model.getRepositoryClassName()
				+ FileExtension.JAVA.getExtension();
			File f = new File(repositoryClassPath);
			CreateFileExtensions.newFileQuietly(f);
			final BufferedWriter writer = new BufferedWriter(new FileWriter(repositoryClassPath));

			repositoryClassTemplate.merge(context, writer);
			writer.flush();
			writer.close();
			// Velocity Template for the repository unit test classes...
			final Template repositoryTestClassTemplate = Velocity
				.getTemplate(generator.getRepositoryTestClassTemplateFile());
			final String daoTestClassPath = generator.getSrcTestFolder()
				+ generator.getSrcTestGenerationPackage() + model.getRepositoryClassName()
				+ FileSuffix.TEST + FileExtension.JAVA.getExtension();
			f = new File(daoTestClassPath);
			CreateFileExtensions.newFileQuietly(f);
			final BufferedWriter daoTestClassWriter = new BufferedWriter(
				new FileWriter(daoTestClassPath));

			repositoryTestClassTemplate.merge(context, daoTestClassWriter);
			daoTestClassWriter.flush();
			daoTestClassWriter.close();
			// Velocity Template for the business services intefaces...
			final Template serviceInterfaceTemplate = Velocity
				.getTemplate(generator.getServiceInterfaceTemplateFile());
			final String serviceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage() + model.getServiceClassName()
				+ FileExtension.JAVA.getExtension();
			f = new File(serviceInterfaceClassPath);
			CreateFileExtensions.newFileQuietly(f);
			final BufferedWriter serviceInterfaceWriter = new BufferedWriter(
				new FileWriter(generator.getSrcFolder() + generator.getSrcServiceGenerationPackage()
					+ model.getServiceClassName() + FileExtension.JAVA.getExtension()));

			serviceInterfaceTemplate.merge(context, serviceInterfaceWriter);
			serviceInterfaceWriter.flush();
			serviceInterfaceWriter.close();
			// Velocity Template for the business services classes...
			final String serviceClassPath =
				  generator.getSrcFolder()
				+ generator.getSrcServiceGenerationPackage()
				+ model.getModelClassName()
				+ "Business" + FileSuffix.SERVICE + FileExtension.JAVA.getExtension();

			mergeToContext(context, generator.getServiceClassTemplateFile(), serviceClassPath);

//			final Template serviceClassTemplate = Velocity
//				.getTemplate(generator.getServiceClassTemplateFile());
//			f = new File(serviceClassPath);
//			CreateFileExtensions.newFileQuietly(f);
//			final BufferedWriter serviceClassWriter = new BufferedWriter(
//				new FileWriter(serviceClassPath));
//
//			serviceClassTemplate.merge(context, serviceClassWriter);
//			serviceClassWriter.flush();
//			serviceClassWriter.close();

			// Velocity Template for the domain services intefaces...
			final String domainServiceInterfaceClassPath = generator.getSrcFolder()
				+ generator.getSrcDomainServiceGenerationPackage() + model.getDomainServiceClassName()
				+ FileExtension.JAVA.getExtension();
			mergeToContext(context, generator.getDomainServiceInterfaceTemplateFile(), domainServiceInterfaceClassPath);
		}
	}


	private static void mergeToContext(final VelocityContext context, String templateFileName, String className) throws IOException
	{
		File f;
		final Template domainServiceInterfaceTemplate = Velocity
			.getTemplate(templateFileName);
		f = new File(className);
		CreateFileExtensions.newFileQuietly(f);
		final BufferedWriter domainServiceInterfaceWriter = new BufferedWriter(
			new FileWriter(className));

		domainServiceInterfaceTemplate.merge(context, domainServiceInterfaceWriter);
		domainServiceInterfaceWriter.flush();
		domainServiceInterfaceWriter.close();
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

		}
		return repositoryModels;
	}

	public static void getPomGenerationModel(final PomGenerationBean generator)
	{

	}

}
