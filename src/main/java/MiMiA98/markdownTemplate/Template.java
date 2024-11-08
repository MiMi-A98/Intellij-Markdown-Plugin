package MiMiA98.markdownTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Template extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        String textToInsert;
        try {
            textToInsert = readTemplateText();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Messages.showInfoMessage(textToInsert, "Template");
    }

    private String readTemplateText() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(("/template.md"))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }

    }
}
