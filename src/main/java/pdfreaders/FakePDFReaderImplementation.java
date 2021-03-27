package pdfreaders;

import pojos.Subject;

/**
 * Fake pdf reader, which returns hard coded subject information.
 */
public class FakePDFReaderImplementation implements PDFReaderInterface {

    /**
     * Does nothing, since this is a fake implementation.
     */
    @Override
    public void readSubjects(String filePath, Template[] templates) {
    }

    /**
     * Return a few predefined, hard coded subjects.
     * @return Array of fake subjects
     */
    @Override
    public Subject[] getExtractedSubjects() {
        Subject[] fakeSubjects = new Subject[10];
        fakeSubjects[0] = new Subject("Programozás I.", "VEMIVIB142", (short)1, (short)2);
        fakeSubjects[1] = new Subject("Programozás II.", "VEMIVIB143",(short)1, (short)3);
        fakeSubjects[1].addPrerequisite(fakeSubjects[0].getCode());
        fakeSubjects[2] = new Subject("Betonkeverés", "VEMISAB100", (short)1, (short)1);
        fakeSubjects[3] = new Subject("Tulipánültetés I.", "VEMIFAP11", (short)1, (short)2);
        fakeSubjects[4] = new Subject("Tulipánültetés II.", "VEMIFAP11", (short)2, (short)2);
        fakeSubjects[4].addPrerequisite(fakeSubjects[3].getCode());
        fakeSubjects[5] = new Subject("Aratás elmélete", "VEMIMIMI33", (short)2, (short)5);
        fakeSubjects[6] = new Subject("Modern tulipánültetési technikák", "VEMILOLO23", (short)2, (short)3);
        fakeSubjects[6].addPrerequisite(fakeSubjects[4].getCode());
        fakeSubjects[7] = new Subject("Korrupció elmélete", "VEMIFID69", (short)2, (short)4);
        fakeSubjects[8] = new Subject("Stadionépítés", "VEMIFID71", (short)3, (short)2);
        fakeSubjects[9] = new Subject("Pacalkészítés művészete", "VEMIFID72", (short)3, (short)3);
        fakeSubjects[9].addPrerequisite(fakeSubjects[7].getCode());
        fakeSubjects[9].addPrerequisite(fakeSubjects[8].getCode());
        return fakeSubjects;

    }
    
}
