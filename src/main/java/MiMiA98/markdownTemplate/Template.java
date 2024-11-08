package MiMiA98.markdownTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Template extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Project project = event.getProject();
        if (project == null) {
            System.out.println("No project found");
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            System.out.println("No editor is currently open");
            return;
        }

        VirtualFile file = editor.getVirtualFile();
        if (file == null || !(file.getName().endsWith(".md") || file.getName().endsWith(".markdown"))) {
            System.out.println("The active file is not a markdown file");
            return;
        }

        String textToInsert;
        try {
            textToInsert = readTemplateText();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        insertTextToFile(project, editor.getDocument(), textToInsert);

    }

    private void insertTextToFile(Project project, Document markdownFile, String textToInsert) {
        WriteCommandAction.runWriteCommandAction(project, () -> markdownFile.insertString(0, textToInsert));
    }

    private String readTemplateText() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(("/template.md"))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }

    }
}
