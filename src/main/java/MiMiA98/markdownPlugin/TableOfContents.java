package MiMiA98.markdownPlugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class TableOfContents extends AnAction {
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

        Document document = editor.getDocument();
        String originalContent = document.getText();
    }
}