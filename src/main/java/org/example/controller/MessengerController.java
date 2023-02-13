package org.example.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.domain.Mesaj;
import org.example.domain.Utilizator;
import org.example.service.Service;
import org.example.utils.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MessengerController implements Observer
{

    private Service service;

    private Utilizator userCurent;

    private Utilizator receiver;

    @FXML
    Label labelRecv;

    @FXML
    Button buttonExit;

    @FXML
    Button buttonSend;

    @FXML
    TextField msgField;

    @FXML
    TextFlow msgDisplay;

    @FXML
    private void initialize() {

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleExit(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonExit.setOnAction(event);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                try {
                    handleSend(event1);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonSend.setOnAction(event1);
    }

    @Override
    public void update() {
        msgDisplay.getChildren().clear();
        List<Mesaj> l = service.getMessages(userCurent, receiver);
        for(Mesaj m : l)
        {
            msgDisplay.getChildren().add(new Text("[ " +
                    m.getSender().getFirstName() + " " + m.getSender().getLastName() + " ] : " + m.getContent() + "\n"));
        }
    }

    public void handleExit(ActionEvent actionEvent) throws IOException{
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void handleSend(ActionEvent actionEvent) throws IOException{
        String content = msgField.getText();
        msgField.clear();
        service.addMessage(userCurent, receiver, content, LocalDateTime.now());
        msgDisplay.getChildren().add(new Text("[ " +
                userCurent.getFirstName() + " " + userCurent.getLastName() + " ] : " + content + "\n"));
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setUserCurent(Utilizator userCurent)
    {
        this.userCurent = userCurent;
    }

    public void setReceiver(Utilizator receiver)
    {
        this.receiver = receiver;
        labelRecv.setText(receiver.getFirstName() + " " + receiver.getLastName());
    }

}
