package org.example.controller;

import events.UtilizatorEntityChangeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.RunApp;
import org.example.domain.NumeUI;
import org.example.domain.Prietenie;
import org.example.domain.PrietenieUI;
import org.example.domain.Utilizator;
import org.example.service.Service;
import org.example.utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReteaController implements Observer {

    private LoginController loginController;

    private Utilizator userCurent;

    private Service service;

    ObservableList<PrietenieUI> modelFriends = FXCollections.observableArrayList();

    ObservableList<NumeUI> modelUsers = FXCollections.observableArrayList();

    @FXML
    private TableView<PrietenieUI> tableViewFriends;

    @FXML
    private TableView<NumeUI> tableViewUsers;

    @FXML
    private TableColumn<PrietenieUI, String> columnNameFriends;

    @FXML
    private TableColumn<PrietenieUI, String> columnStatusFriends;

    @FXML
    private TableColumn<PrietenieUI, String> columnDateFriends;

    @FXML
    private TableColumn<NumeUI, String> columnNameUsers;


    @FXML
    Label labelUser;

    @FXML
    Button buttonUnfriend;

    @FXML
    Button buttonSendRequest;

    @FXML
    Button buttonAcceptRequest;

    @FXML
    Button buttonLogOut;

    @FXML
    Button buttonMessage;

    @FXML
    public void initialize()
    {

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleDeleteFriendship(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonUnfriend.setOnAction(event);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event1) {
                try {
                    handleAddPendingFriendship(event1);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonSendRequest.setOnAction(event1);

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event2) {
                try {
                    handleAcceptFriendship(event2);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonAcceptRequest.setOnAction(event2);

        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event3) {
                try {
                    handleLogOut(event3);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonLogOut.setOnAction(event3);

        EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event4) {
                try {
                    handleMessage(event4);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        buttonMessage.setOnAction(event4);

        columnNameFriends.setCellValueFactory(new PropertyValueFactory<PrietenieUI, String>("user"));
        columnDateFriends.setCellValueFactory(new PropertyValueFactory<PrietenieUI, String>("friendsFrom"));
        columnStatusFriends.setCellValueFactory(new PropertyValueFactory<PrietenieUI, String>("status"));
        tableViewFriends.setItems(modelFriends);


        columnNameUsers.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tableViewUsers.setItems(modelUsers);

    }

    private void initModel(){

        Iterable<Utilizator> users = service.getNotFriends(userCurent);
        List<NumeUI> userNames = new ArrayList<>();
        users.forEach(x -> userNames.add(new NumeUI(x.getFirstName() + " " + x.getLastName())));
        modelUsers.setAll(userNames);

        Iterable<Prietenie> friends = service.getAllFriendshipsForUser(userCurent);
        List<PrietenieUI> prietenii = new ArrayList<>();
        for(Prietenie p : friends)
        {
            Utilizator u1 = p.getUser1();
            Utilizator u2 = p.getUser2();
            String name = null;
            if(u1.getId() == userCurent.getId())
            {
                name = u2.getFirstName() + " " + u2.getLastName();

            }
            else
                name = u1.getFirstName() + " " + u1.getLastName();
            PrietenieUI pp = new PrietenieUI(name, p.getDate().toString());
            if(p.getStatus() == 0L)
                if(u1.getId() == userCurent.getId())
                    pp.setStatus("Pending ( from you )");
                else
                    pp.setStatus("Pending ( from them )");
            else
                pp.setStatus("Friends :)");
            prietenii.add(pp);
        }
        modelFriends.setAll(prietenii);

    }

    @Override
    public void update() {

        initModel();
    }


    public void handleDeleteFriendship(ActionEvent actionEvent) throws IOException
    {
        PrietenieUI selected = (PrietenieUI) tableViewFriends.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String uname = selected.getUser();
            String[] names = uname.split(" ");
            Utilizator u = service.findUserByName(names[0], names[1]);
            service.removeFriend(userCurent.getId(), u.getId());
            if(selected.getStatus() == "Friends :)")
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "You are no longer friends </3");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "You turned down the friend request </3");
        } else MessageAlert.showErrorMessage(null, "You haven't selected any friend relations!");

    }

    public void handleAddPendingFriendship(ActionEvent actionEvent) throws IOException
    {
        NumeUI selected = (NumeUI) tableViewUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String uname = selected.getNume();
            String[] names = uname.split(" ");
            Utilizator u = service.findUserByName(names[0], names[1]);
            service.addFriend(userCurent.getId(), u.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add", "Friend request sent :)");
        } else MessageAlert.showErrorMessage(null, "You haven't selected a user");
    }

    public void handleAcceptFriendship(ActionEvent actionEvent) throws IOException
    {
        PrietenieUI selected = (PrietenieUI) tableViewFriends.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String uname = selected.getUser();
            String[] names = uname.split(" ");
            Utilizator u = service.findUserByName(names[0], names[1]);
            if(selected.getStatus() == "Friends :)")
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "You are already friends -_- ");
            else
                if(selected.getStatus() == "Pending ( from you )")
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "You can't accept your own friend request -_-");
                else
                {
                    service.acceptFriend(u.getId(), userCurent.getId());
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "You are now friends :)");
                }
        } else MessageAlert.showErrorMessage(null, "You haven't selected any friend relations!");

    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException
    {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader fxmlLoader=new FXMLLoader(RunApp.class.getResource("/view/loginView.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),800,600);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        LoginController lController=fxmlLoader.getController();
        lController.setService(service);

    }

    public void handleMessage(ActionEvent actionEvent) throws IOException
    {
        PrietenieUI selected = (PrietenieUI) tableViewFriends.getSelectionModel().getSelectedItem();
        if(selected == null)
            MessageAlert.showErrorMessage(null, "You haven't selected any friend to send messages to!");

        FXMLLoader fxmlLoader=new FXMLLoader(RunApp.class.getResource("/view/messengerView.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        MessengerController mController = fxmlLoader.getController();
        mController.setService(service);
        mController.setUserCurent(userCurent);


        String uname = selected.getUser();
        String[] names = uname.split(" ");
        Utilizator u = service.findUserByName(names[0], names[1]);
        mController.setReceiver(u);

        service.addObserver(mController);
        mController.update();


    }


    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
        labelUser.setId(userCurent.getFirstName() + " " + userCurent.getLastName());
        labelUser.setText(userCurent.getFirstName() + " " + userCurent.getLastName());
    }

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
