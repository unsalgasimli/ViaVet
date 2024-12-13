package main.app.heart;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.app.heart.PostgreSQLConnection;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class App extends Application {

    static int screenWidth;
    static int screenHeight;

    ///JUST OPENS DIFFERENT STAGES
    public static void openMain() {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/admin/adminStaff.fxml"));
        try {
            initStage(stage, loader, "/main/app/admin/admin.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openPatient()  {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/admin/adminPatient.fxml"));
        try {
            initStage(stage, loader, "/main/app/admin/admin.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void openStaffAppointment(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/staff/staffAppointment.fxml"));
        try {
            initStage(stage, loader, "/main/resources/app.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void openStaffHistory(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/staff/staffHistory.fxml"));
        try {
            initStage(stage, loader, "/main/resources/app.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openStaffInfo(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/staff/staffInfo.fxml"));
        try {
            initStage(stage, loader, "/main/resources/app.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logOpen() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/logreg/appLogin.fxml"));
        try {
            initStage(stage, loader, "/main/app/logreg/login.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void regOpen() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/app/logreg/appRegister.fxml"));
        try {
            initStage(stage, loader, "/main/app/logreg/register.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void openPatientAppointment() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/patient/patientAppointment.fxml"));
        initStage(stage, loader, "/main/resources/app.css");
    }



    public static void openPatienInfo()  {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/patient/patientInfo.fxml"));
        try {
            initStage(stage, loader, "/main/resources/app.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void openPatienHistory(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/patient/patientHistory.fxml"));
        try {
            initStage(stage, loader, "/main/resources/app.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void start(Stage stage) throws IOException {
        initializeScreenSize();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/app/logreg/appLogin.fxml"));
//        initStage(stage, loader, "/main/app/logreg/login.css");



        FXMLLoader loader = new FXMLLoader(App.class.getResource("/main/app/admin/adminStaff.fxml"));
        try {
            initStage(stage, loader, "/main/app/admin/admin.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void initializeScreenSize() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

    }
    private static void initStage(Stage stage, FXMLLoader loader, String cssSource) throws IOException {
        AnchorPane rootLayout = (AnchorPane) loader.load();
        Scene scene = new Scene(rootLayout, screenWidth, screenHeight);
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource(cssSource)).toExternalForm());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setMinWidth(500);
        stage.setMinHeight(540);
        stage.setScene(scene);
        stage.show();
    }
}


