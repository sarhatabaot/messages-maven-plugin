package com.github.sarhatabaot.messages;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;


/**
 * This goal aims to generate a static accessor class
 * with string to every internal message
 */
@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
public class GenerateClassMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;

    @Parameter(property = "messages.overwrite")
    private boolean overwriteClasses;

    @Parameter(required = true, property = "messages.sourcefolder") //source folder to generate classes from
    private File sourceFolder;

    @Parameter(required = true, property = "messages.targetpackage") //target should be a package
    private String targetPackage;

    @Parameter(property = "messages.privateconstructor")
    private String privateConstructor;

    private static final String BASE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator;

    public void execute() throws MojoExecutionException {
        String splitPackage = Util.getPathFromPackage(targetPackage);

        final File targetFolder = new File(mavenProject.getBasedir(), BASE_PATH + splitPackage);
        if (!sourceFolder.exists())
            throw new MojoExecutionException("Could not find source folder." + sourceFolder.getName());

        if (!targetFolder.exists())
            throw new MojoExecutionException("Could not find specified package. " + targetPackage + " " + targetFolder.getPath());

        WriteClass writeClass = new WriteClass(targetPackage,BASE_PATH,privateConstructor,overwriteClasses);
        for (File sourceFile : sourceFolder.listFiles()) {
            writeClass.createJavaClassFromJsonFile(sourceFile);
        }
    }




}
