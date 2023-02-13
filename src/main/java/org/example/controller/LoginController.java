package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.RunApp;
import org.example.domain.Utilizator;
import org.example.service.Service;


import java.io.IOException;
import java.util.stream.Collectors;

public class LoginController {

    private Service service;

    @FXML
    TextField textFieldNume;

    @FXML
    PasswordField passwordFieldPassword;

    @FXML
    Button buttonLogin;

    @FXML
    Button buttonSignUp;

    @FXML
    public void initialize()
    {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleLogin(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonLogin.setOnAction(event);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleSignUp(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonSignUp.setOnAction(event1);
    }

    public void handleSignUp(ActionEvent event) throws  IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader(RunApp.class.getResource("/view/signupView.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),800,600);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        SignUpController sController=fxmlLoader.getController();
        sController.setService(service);
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException
    {
        String username = textFieldNume.getText();
        String password = passwordFieldPassword.getText();
        textFieldNume.clear();
        passwordFieldPassword.clear();
        System.out.println(username);
        System.out.println(password);
        String[] arr = username.split(" +");
        int n = 0;
        String firstname = "";
        String lastname = "";
        for ( String ss : arr) {
            if(n == 0)
                firstname = ss;
            else {
                lastname = ss;
                System.out.println(ss);
                break;
            }
            System.out.println(ss);
            n++;
        }
        Utilizator u = service.findUserByName(firstname, lastname);
        Utilizator u1 = service.findUserByName(lastname, firstname);
        Utilizator fin = null;

        if(u != null) {
            fin = u;
        }
        if(u1 != null) {
            fin = u1;
        }
        if(fin == null)
        {
            MessageAlert.showErrorMessage(null, "Given user does not exist");
            return;
        }

        String found = service.getPasswordForUser(fin);
        System.out.println(found);
        if(!found.equals(password))
        {
            MessageAlert.showErrorMessage(null, "Incorrect password");
        }
        FXMLLoader fxmlLoader=new FXMLLoader(RunApp.class.getResource("/view/reteaView.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),800,600);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        ReteaController mainController=fxmlLoader.getController();
        mainController.setUserCurent(fin);
        mainController.setService(service);
        mainController.setLoginController(this);
        mainController.update();
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }


    public void setService(Service service) {
        this.service = service;
    }

}
