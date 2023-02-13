package org.example.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.RunApp;
import org.example.domain.Utilizator;
import org.example.service.Service;

import java.io.IOException;


public class SignUpController {
    private Service service;

    @FXML
    TextField textFieldF;

    @FXML
    TextField textFieldL;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField confirmPassword;

    @FXML
    Button buttonSign;

    @FXML
    public void initialize()
    {
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                try {
                    handleSignUp(event1);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonSign.setOnAction(event1);
    }

    public void handleSignUp(ActionEvent event) throws  IOException
    {
        String firstname = textFieldF.getText();
        String lastname = textFieldL.getText();
        String p1 = passwordField.getText();
        String p2 = confirmPassword.getText();
        textFieldF.clear();
        textFieldL.clear();
        passwordField.clear();
        confirmPassword.clear();
        if(!p1.equals(p2))
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Passwords", "Passwords do not match");
            return;
        }
        if(service.findUserByName(firstname, lastname) != null)
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Names", "There already exists a viewer with given name");
            return;
        }
        try {
            service.addUser(firstname, lastname, 10L);
            service.addPassword(service.findUserByName(firstname, lastname), p1);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "SignUp", "Sing up was a succes");
        }catch(RuntimeException e)
        {
            e.printStackTrace();
            return;
        }

        FXMLLoader fxmlLoader=new FXMLLoader(RunApp.class.getResource("/view/reteaView.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),800,600);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        ReteaController mainController=fxmlLoader.getController();
        mainController.setUserCurent(service.findUserByName(firstname, lastname));
        mainController.setService(service);
        mainController.update();
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
