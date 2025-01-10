package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private TextField arabicNumberField;
    private TextField geezNumberField;
    private Label resultLabel;

    private static final Map<Integer, String> geezNumerals = new HashMap<>();

    static {
        geezNumerals.put(1, "፩");
        geezNumerals.put(2, "፪");
        geezNumerals.put(3, "፫");
        geezNumerals.put(4, "፬");
        geezNumerals.put(5, "፭");
        geezNumerals.put(6, "፮");
        geezNumerals.put(7, "፯");
        geezNumerals.put(8, "፰");
        geezNumerals.put(9, "፱");
        geezNumerals.put(10, "፲");
        geezNumerals.put(20, "፳");
        geezNumerals.put(30, "፴");
        geezNumerals.put(40, "፵");
        geezNumerals.put(50, "፶");
        geezNumerals.put(60, "፷");
        geezNumerals.put(70, "፸");
        geezNumerals.put(80, "፹");
        geezNumerals.put(90, "፺");
        geezNumerals.put(100, "፻");
        geezNumerals.put(10000, "፼");
    }

    public void start(Stage primaryStage) {
        arabicNumberField = new TextField();
        geezNumberField = new TextField();
        resultLabel = new Label("Result: ");

        Button convertToGeezButton = new Button("Convert to Ge'ez");
        Button convertToArabicButton = new Button("Convert to Arabic");

        // Set button actions
        convertToGeezButton.setOnAction(e -> convertToGeez());
        convertToArabicButton.setOnAction(e -> convertToArabic());

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        // Add components to layout
        gridPane.add(new Label("Enter Arabic Number:"), 0, 0);
        gridPane.add(arabicNumberField, 1, 0);
        
        gridPane.add(convertToGeezButton, 0, 1);
        
        gridPane.add(new Label("Enter Ge'ez Number:"), 0, 2);
        gridPane.add(geezNumberField, 1, 2);
        
        gridPane.add(convertToArabicButton, 0, 3);
        
        gridPane.add(resultLabel, 0, 4, 2, 1);

        // Set up the scene and stage
        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setTitle("Geez Numerals Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void convertToGeez() {
        try {
            int num = Integer.parseInt(arabicNumberField.getText());
            String result = intToGeez(num);
            resultLabel.setText("Result: " + result);
            geezNumberField.clear(); // Clear the Ge'ez field
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid Arabic number!");
            geezNumberField.clear();
        }
    }

    private void convertToArabic() {
        String geezNum = geezNumberField.getText();
        
        int result = geezToInt(geezNum);
        
        if (result >= 0) {
            resultLabel.setText("Result: " + result);
            arabicNumberField.clear(); // Clear the Arabic field
        } else {
            resultLabel.setText("Invalid Ge'ez number!");
            arabicNumberField.clear();
        }
    }

    private String intToGeez(int num) {
        if (num == 0) return "፩"; // Special case for zero

        StringBuilder result = new StringBuilder();
        
        int[] powers = {10000, 1000, 100, 10};
        
        for (int value : powers) {
            if (num >= value) {
                int quotient = num / value;
                if (quotient > 1) {
                    result.append(intToGeez(quotient));
                }
                result.append(getGeezSymbol(value));
                num %= value;
            }
        }

        if (num > 0) {
            result.append(geezNumerals.get(num));
        }

        return result.toString();
    }

    private String getGeezSymbol(int value) {
       switch (value) {
           case 10000: return "፼";
           case 100: return "፻";
           case 10: return "፲";
           default: return "";
       }
   }

   private int geezToInt(String geezNum) {
       int result = 0;
       int temp = 0;

       for (char charDigit : geezNum.toCharArray()) {
           Integer value = getArabicValue(charDigit);

           if (value != null) {
               if (value == 100 || value == 10000) {
                   temp = (temp == 0 ? 1 : temp) * value;
               } else {
                   temp += value;
               }
           }

           if (temp >= 100 && charDigit != '፻' && charDigit != '፼') {
               result += temp;
               temp = 0;
           }
       }
       result += temp;

       return result;
   }

   private Integer getArabicValue(char charDigit) {
       for (Map.Entry<Integer, String> entry : geezNumerals.entrySet()) {
           if (entry.getValue().equals(String.valueOf(charDigit))) {
               return entry.getKey();
           }
       }
       return null; 
   }

   public static void main(String[] args) {
       launch(args);
   }
}
