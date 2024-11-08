package MiMiA98.markdownPlugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

        String tocContent = createTOC(originalContent);

        WriteCommandAction.runWriteCommandAction(project, () -> document.setText(tocContent + "\n\n" + originalContent));
    }

    private String createTOC(String content) {
        String[] lines = content.split("\n");
        List<String> tocLines = new ArrayList<>();

        tocLines.add("<!-- Table of Contents -->");

        for (String line : lines) {
            if(line.startsWith("#")) {
                int level = getHeadingLevel(line);
                String headingText = line.replaceAll("^#+\\s*", "");
                String tocEntry = "  ".repeat(level - 1) + "- [" + headingText + "](#" + formatAnchor(headingText) + ")";
                tocLines.add(tocEntry);
            }
        }

        tocLines.add("<!-- Table of Contents -->");

        return String.join("\n", tocLines);
    }

    private int getHeadingLevel(String line) {
        int level = 0;
        while(level < line.length() && line.charAt(level) == '#') {
            level++;
        }
        return level;
    }

    private String formatAnchor(String headingText) {
        return headingText.toLowerCase().replace(" ", "-").replaceAll("[^a-z0-9\\-]", "");
    }
}
