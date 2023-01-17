package com.github.sarhatabaot.messages;

import com.github.sarhatabaot.messages.model.FileType;
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
public class GenerateClassMojo extends AbstractMojo implements MessagesPlugin<MojoExecutionException> {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;
    
    @Parameter(property = "messages.filetype")
    private FileType fileType = FileType.JSON;
    
    @Parameter(property = "messages.overwrite")
    private boolean overwriteClasses;
    
    @Parameter(property = "messages.sourcefolder") //source folder to generate classes from
    private File sourceFolder;
    
    @Parameter(property = "messages.targetpackage") //target should be a package
    private String targetPackage;
    
    @Parameter(property = "messages.privateconstructor")
    private String privateConstructor;
    
    @Parameter(property = "messages.basepath", defaultValue = "src/main/java/", readonly = true)
    private String basePath;
    
    public void execute() throws MojoExecutionException {
        runTask();
    }
    
    @Override
    public String getBasePath() {
        return basePath;
    }
    
    @Override
    public File getSourceFolder() {
        return sourceFolder;
    }
    
    @Override
    public File getBaseDir() {
        return mavenProject.getBasedir();
    }
    
    @Override
    public String getTargetPackage() {
        return targetPackage;
    }
    
    @Override
    public String getPrivateConstructor() {
        return privateConstructor;
    }
    
    @Override
    public boolean isOverwriteClasses() {
        return overwriteClasses;
    }
    
    @Override
    public void throwException(String s) throws MojoExecutionException {
        throw new MojoExecutionException(s);
    }
    
    @Override
    public FileType getFileType() {
        return fileType;
    }
}
