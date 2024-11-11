package MiMiA98.markdownPlugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
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

        DocumentUtils.insertTextToDocument(project, editor.getDocument(), tocContent);
    }

    private String createTOC(String documentContent) {
        String[] documentLines = documentContent.split("\n");
        List<String> tocLines = new ArrayList<>();

        String tocDelimiter = "<!-- Table of Contents -->";
        tocLines.add(tocDelimiter);

        for (String line : documentLines) {
            if (line.startsWith("#")) {
                int headingLevel = getHeadingLevel(line);
                String headingText = line.replaceAll("^#+\\s*", ""); // Replace all heading syntax and empty spaces after it
                String tocEntry = generateTocEntry(headingLevel, headingText);
                tocLines.add(tocEntry);
            }
        }

        tocLines.add(tocDelimiter);

        return String.join("\n", tocLines);
    }

    private @NotNull String generateTocEntry(int headingLevel, String headingText) {
        return "  ".repeat(headingLevel - 1) // Format indentation
                + "- [" + headingText + "](#" + formatAnchor(headingText) + ")"; // Add heading text with anchor
    }

    private int getHeadingLevel(String line) {
        int headingLevel = 0;
        while (headingLevel < line.length() && line.charAt(headingLevel) == '#') {
            headingLevel++;
        }
        return headingLevel;
    }

    private String formatAnchor(String headingText) {
        return headingText.toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9\\-]", ""); // Replace all that are not letters, numbers or dash
    }
}
