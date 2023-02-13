package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.domain.Prietenie;
import org.example.domain.Utilizator;
import org.example.domain.validators.PrietenieValidator;
import org.example.domain.validators.UtilizatorValidator;
import org.example.domain.validators.Validator;
import org.example.repository.db.MessageDbRepository;
import org.example.repository.db.PrietenieDbRepository;
import org.example.repository.db.UtilizatorDbRepository;
import org.example.service.Service;

public class RunApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root=loader.load();

        LoginController ctrl=loader.getController();

        String username = "postgres";
        String password = "Andreas14321";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        Validator<Utilizator> val = new UtilizatorValidator();
        Validator<Prietenie> valP = new PrietenieValidator();
        UtilizatorDbRepository repo = new UtilizatorDbRepository(url, username, password, val);
        PrietenieDbRepository repoP = new PrietenieDbRepository(url, username, password, valP, repo);
        MessageDbRepository repoMsg = new MessageDbRepository(url, username, password, repo);
        Service service = new Service(repo, repoP, repoMsg);

        ctrl.setService(service);

        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setTitle("Login Retea");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
