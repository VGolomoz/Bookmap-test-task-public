package app.service;

import app.util.CustomException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyParser {

    private InputDataHandler inputDataHandler;

    public MyParser(InputDataHandler inputDataHandler) {
        this.inputDataHandler = inputDataHandler;
    }

    public void startRead() {

        List<String> inputData = new ArrayList();

        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            String fileName = chooser.getSelectedFile().getName();
            String pathWithFileName = chooser.getSelectedFile().getAbsolutePath();
            String pathToSave = pathWithFileName.substring(0, pathWithFileName.lastIndexOf(fileName)) + "output";

            try (FileReader reader = new FileReader(chooser.getSelectedFile())) {
                StringBuilder result = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) {
                    result.append((char) c);
                }
                inputData = Arrays.asList(result.toString().split("\r\n"));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            try {
                saveToFile(inputDataHandler.processing(inputData), pathToSave);
            } catch (CustomException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void saveToFile(String outputData, String pathToSave) {

        try (FileWriter writer = new FileWriter(pathToSave, false)) {
            writer.write(outputData);
            writer.flush();
            System.out.println("Done! Output file was saved by this path: " + pathToSave);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}



