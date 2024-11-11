package MiMiA98.markdownPlugin;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

public class DocumentUtils {
    /**
     * Inserts text at the beginning of the document
     */
    public static void insertTextToDocument(Project project, Document document, String textToInsert) {
        WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(0, textToInsert));
    }

}
