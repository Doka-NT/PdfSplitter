/*
 * Copyright (c) 2016 Soshnikov Artem <213036@skobka.com>
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.PdfManipulationService;
import service.exceptions.NoBookmarksException;
import service.interfaces.PdfSplitterInterface;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private Button btnSelect;
    private Button btnStart;
    private File selectedFile;
    private PdfSplitterInterface pdfSplitter = new PdfManipulationService();

    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(this.getPrimaryRoot()));
        stage.show();
        stage.setResizable(false);
        stage.setTitle("PDF splitter by Bookmarks");
        this.setupListeners();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Parent getPrimaryRoot() {
        HBox box = new HBox();
        box.setPadding(new Insets(10));
        box.setSpacing(5);

        btnSelect = new Button("Select PDF file");
        btnStart = new Button("Start");

        HBox.setHgrow(box, Priority.ALWAYS);
        HBox.setHgrow(btnSelect, Priority.ALWAYS);
        HBox.setHgrow(btnStart, Priority.ALWAYS);

        box.getChildren().addAll(
                btnSelect,
                btnStart
        );

        return box;
    }

    private void setupListeners() {
        btnSelect.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select PDF file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));

            selectedFile = fileChooser.showOpenDialog(btnSelect.getScene().getWindow());
        });

        btnStart.setOnAction((event) -> {
            if (selectedFile == null) {
                return;
            }
            try {
                pdfSplitter.splitByBookmarks(selectedFile);
            } catch (IOException | NoBookmarksException e) {
                e.printStackTrace();
            }
        });
    }
}
