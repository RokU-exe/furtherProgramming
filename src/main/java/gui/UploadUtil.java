package gui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadUtil {
    public static String uploadDocument(Stage stage, String claimId, String cardNumber) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String targetDir = "uploads";
            String targetFileName = String.format("%s_%s_%s", claimId, cardNumber, file.getName());
            Path targetPath = Paths.get(targetDir, targetFileName);

            try {
                Files.createDirectories(Paths.get(targetDir));
                Files.copy(file.toPath(), targetPath);
                return targetPath.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}
