package pdfreaders;

import pojos.Subject;

/**
 * An enclosing interface for all PDF readers.
 * This was used initially, to allow us to have a fake pdf reader
 * while the actual reader was worked on. This interface allowed
 * easier integration of the actual pdf reader after it was done.
 */
public interface PDFReaderInterface {
    public void readSubjects(String filePath, Template[] templates);
    public Subject[] getExtractedSubjects();
}
