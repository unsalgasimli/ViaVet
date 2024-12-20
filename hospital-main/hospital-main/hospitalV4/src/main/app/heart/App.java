package main.app.heart;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static int screenWidth;
    private static int screenHeight;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        // Initialize primary stage and screen dimensions
        primaryStage = stage;
        initializeScreenSize();

        // Load the initial scene (login screen)

        changeScene("/main/app/logreg/appLogin.fxml", "/main/app/logreg/login.css");


    }

    /**
     * Initializes screen width and height.
     */
    private static void initializeScreenSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }

    /**
     * Changes the scene of the primary stage with smooth fade transitions.
     *
     * @param fxmlPath  The FXML file path for the new scene.
     * @param cssSource The CSS file path for the new scene.
     */
    public static void changeScene(String fxmlPath, String cssSource) {
        try {
            // Validate FXML path
            if (App.class.getResource(fxmlPath) == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            // Load FXML
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            AnchorPane rootLayout = loader.load();

            // Create a new scene
            Scene newScene = new Scene(rootLayout, screenWidth, screenHeight);
            if (cssSource != null && !cssSource.isEmpty()) {
                if (App.class.getResource(cssSource) == null) {
                    throw new IOException("CSS file not found: " + cssSource);
                }
                newScene.getStylesheets().add(App.class.getResource(cssSource).toExternalForm());
            }

            // Save fullscreen state
            boolean wasFullScreen = primaryStage.isFullScreen();

            // Set the new scene
            primaryStage.setScene(newScene);

            // Restore fullscreen state
            if (wasFullScreen) {
                primaryStage.setFullScreen(true);
            }

            // Show the stage if not already visible
            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (IOException e) {
            System.err.println("Error changing scene: " + e.getMessage());
            e.printStackTrace();
        }
    }





//    private static void applyFadeTransition(Scene oldScene, AnchorPane rootLayout, Scene newScene) {
//        // Save the current fullscreen state before starting the transition
//        boolean wasFullScreen = primaryStage.isFullScreen();
//
//        // Fade out the old scene's root (if it exists)
//        if (oldScene != null && oldScene.getRoot() != null) {
//            // Fade out animation for the current scene
//            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), oldScene.getRoot());
//            fadeOut.setFromValue(1);
//            fadeOut.setToValue(0); // Fade to transparent
//
//            fadeOut.setOnFinished(event -> {
//                // Once fade-out is complete, set the new scene
//                primaryStage.setScene(newScene);
//
//                // Reapply the fullscreen state to ensure it stays
//                primaryStage.setFullScreen(wasFullScreen);
//
//                // Fade in animation for the new scene
//                FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newScene.getRoot());
//                fadeIn.setFromValue(0);  // Start from transparent
//                fadeIn.setToValue(1);    // Fade to fully opaque
//                fadeIn.play(); // Start fade-in
//            });
//
//            fadeOut.play(); // Start fade-out
//        } else {
//            // If there is no previous scene, just set the new scene and fade it in
//            primaryStage.setScene(newScene);
//
//            // Reapply the fullscreen state to ensure it stays
//            primaryStage.setFullScreen(wasFullScreen);
//
//            // Fade in animation for the new scene
//            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newScene.getRoot());
//            fadeIn.setFromValue(0);  // Start from transparent
//            fadeIn.setToValue(1);    // Fade to fully opaque
//            fadeIn.play(); // Start fade-in
//        }
//    }





    /**
     * Opens the admin staff screen.
     */
    public static void openAdminStaff() {
        changeScene("/main/app/admin/adminStaff.fxml", "/main/app/admin/admin.css");
    }

    /**
     * Opens the admin patient screen.
     */
    public static void openAdminPatient() {
        changeScene("/main/app/admin/adminPatient.fxml", "/main/app/admin/admin.css");
    }

    /**
     * Opens the staff appointment screen.
     */
    public static void openStaffAppointment() {
        changeScene("/main/app/staff/staffAppointment.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the staff history screen.
     */
    public static void openStaffHistory() {
        changeScene("/main/app/staff/staffHistory.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the staff info screen.
     */
    public static void openStaffInfo() {
        changeScene("/main/app/staff/staffInfo.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the patient appointment screen.
     */
    public static void openPatientAppointment() {
        changeScene("/main/app/patient/patientAppointment.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the patient info screen.
     */
    public static void openPatientInfo() {
        changeScene("/main/app/patient/patientInfo.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the patient history screen.
     */
    public static void openPatientHistory() {
        changeScene("/main/app/patient/patientHistory.fxml", "/main/resources/app.css");
    }

    /**
     * Opens the registration screen.
     */
    public static void openRegister() {
        changeScene("/main/app/logreg/appRegister.fxml", "/main/app/logreg/register.css");
    }

    /**
     * Opens the login screen.
     */
    public static void openLogin() {
        changeScene("/main/app/logreg/appLogin.fxml", "/main/app/logreg/login.css");
    }

    public static void main(String[] args) {
        launch();
    }
}
