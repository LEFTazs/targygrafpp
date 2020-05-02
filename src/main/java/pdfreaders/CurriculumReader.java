package pdfreaders;

import TargygrafPP.Curriculum;
import TargygrafPP.Subject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CurriculumReader {
    List<Curriculum> curriculums;
    private List<File> pdfFiles;
    private List<File> yamlFiles;
    
    private TemplateReader templateReader;
    private PDFReader pdfReader;
    
    public CurriculumReader() {
        templateReader = new TemplateReader();
        pdfReader = new PDFReader();
    }
    
    public void read(String pdfAndTemplateFolder) {
        curriculums = new ArrayList<>();
        pdfFiles = new ArrayList<>();
        yamlFiles = new ArrayList<>();
        
        collectPDFAndYamlFiles(pdfAndTemplateFolder);
        processCollectedFiles();
    }
    
    private void collectPDFAndYamlFiles(String pdfAndTemplateFolder) {
        File folder = new File(pdfAndTemplateFolder);
        File[] listFiles = folder.listFiles();
        for (File file : listFiles) {
            if (file.isFile())
                addFiletoCorrectList(file);
        }
    }
    
    private void addFiletoCorrectList(File file) {
        if (isFilePDF(file)) {
            pdfFiles.add(file);
        } else if (isFileYaml(file)) {
            yamlFiles.add(file);
        }
    }
    
    private boolean isFilePDF(File file) {
        return file.toString().toLowerCase().endsWith(".pdf");
    }
    
    private boolean isFileYaml(File file) {
        return file.toString().toLowerCase().endsWith(".yaml") ||
                file.toString().toLowerCase().endsWith(".yml");
    }
    
    private void processCollectedFiles() {
        for (File yamlFile : yamlFiles) {
            File correspondingPdf = pdfFiles.stream()
                    .filter((pdfFile) -> doFilesCorrespond(yamlFile, pdfFile))
                    .findFirst()
                    .orElseThrow();
            
            templateReader.read(yamlFile.toString());
            Template[] templates = templateReader.getTemplates();
            pdfReader.readSubjects(correspondingPdf.toString(), templates);
            
            generateCurriculum(getFileId(yamlFile));
        }
    }
    
    private boolean doFilesCorrespond(File file1, File file2) {
        String path1Id = getFileId(file1);
        String path2Id = getFileId(file2);
        return path2Id.equals(path1Id);
    }
    
    private String getFileId(File file) {
        return file.getName().split("[.]")[0];
    }
    
    private void generateCurriculum(String id) {
        String curriculumName = templateReader.getCurriculumName();
        Subject[] extractedSubjects = pdfReader.getExtractedSubjects();
        
        Curriculum curriculum = new Curriculum(id, curriculumName, extractedSubjects);
        curriculums.add(curriculum);
    }
    
    public Curriculum[] getCurriculums() {
        return curriculums.toArray(new Curriculum[0]);
    }
}
