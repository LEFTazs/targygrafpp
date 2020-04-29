package pdfreaders;

import TargygrafPP.Subject;

public interface PDFReaderInterface {
    public void readSubjects(String filePath, Template[] templates);
    public Subject[] getExtractedSubjects();
}
