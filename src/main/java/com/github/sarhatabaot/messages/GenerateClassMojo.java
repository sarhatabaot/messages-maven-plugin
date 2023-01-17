package com.github.sarhatabaot.messages;

import com.github.sarhatabaot.messages.generate.WriteClass;
import com.github.sarhatabaot.messages.generate.WriteJsonClass;
import com.github.sarhatabaot.messages.generate.WriteYamlClass;
import com.github.sarhatabaot.messages.model.FileType;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


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
        String splitPackage = getPathFromPackage(targetPackage);
        
        final File targetFolder = new File(mavenProject.getBasedir(), basePath + splitPackage);
        if (!sourceFolder.exists())
            throw new MojoExecutionException("Could not find source folder." + sourceFolder.getName());
        
        if (!targetFolder.exists())
            throw new MojoExecutionException("Could not find specified package. " + targetPackage + " " + targetFolder.getPath());
        
        WriteClass<?> writeClass = null;
        
        if (fileType == FileType.JSON)
            writeClass = new WriteJsonClass(targetPackage, basePath, privateConstructor, overwriteClasses);
        
        if (fileType == FileType.YAML)
            writeClass = new WriteYamlClass(targetPackage, basePath, privateConstructor, overwriteClasses);
        
        if (writeClass == null)
            throw new MojoExecutionException("There was a problem getting the file type");
        
        try {
            if (sourceFolder.isDirectory()) {
                for (File sourceFile : Objects.requireNonNull(sourceFolder.listFiles())) {
                    writeClass.createJavaClass(sourceFile);
                }
            } else {
                writeClass.createJavaClass(sourceFolder);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
        
    }
    
    public @NotNull String getPathFromPackage(final @NotNull String targetPackage) {
        return String.join(File.separator, targetPackage.split("\\."));
    }
}
