package isi.dev.javasocketexam;


import isi.dev.javasocketexam.dao.CommentaireImpl;
import isi.dev.javasocketexam.dao.ICommentaire;
import isi.dev.javasocketexam.entities.Commentaire;
import isi.dev.javasocketexam.entities.Membre;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;

import java.util.ResourceBundle;

import static isi.dev.javasocketexam.Controller.*;


public class Room extends Thread implements Initializable {

    @FXML
    private Label clientName;

    @FXML
    private Pane chat;

    @FXML
    private TextField msgField;

    @FXML
    private TextArea msgRoom;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;

    @FXML
    private Pane profile;

    @FXML
    private javafx.scene.control.Button profileBtn;

    @FXML
    private TextField fileChoosePath;

    @FXML
    private ImageView proImage;

    @FXML
    private Circle showProPic;


    private FileChooser fileChooser;
    private File filePath;
    private boolean toggleChat = false, toggleProfile = false;

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    private ICommentaire commentaire = new CommentaireImpl();

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 1234);
            System.out.println(username+ " est connecter au server");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(username);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("quit")) {
                    msgRoom.appendText(username + " a quitter le chat" + "\n");
                    break;
                }
                msgRoom.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), profile);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();

            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), chat);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();

            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    public void setProfile() {
        for (Membre user : loggedInMembre) {
            if (username.equalsIgnoreCase(user.getName())) {
                fullName.setText(user.getFullName());
                fullName.setOpacity(1);
                email.setText(user.getEmail());
                email.setOpacity(1);
                phoneNo.setText(user.getPhoneNo());
                gender.setText(user.getGender());
            }
        }
    }

    public void handleSendEvent(MouseEvent event) {
        send();
        for (Membre user : loggedInMembre) {
            System.out.println(user.getName());
        }
    }

    public void send() {
        Commentaire cmt = new Commentaire();
        String msg = msgField.getText();
        if (!msg.equalsIgnoreCase("quit")){
            cmt.setMessage(msg);
            cmt.setMembre(mbre);
            commentaire.create(cmt);
        }

        writer.println(username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Moi: " + msg + "\n");
        msgField.setText("");
        if (msg.equalsIgnoreCase("quit") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    // Changing profile pic

    private boolean saveControl = false;

    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void saveImage() {
        if (saveControl) {
            try {
                Image image = new Image(filePath.toURI().toString());
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if (Controller.gender.equalsIgnoreCase("Masculin")) {
           // image = new Image(getClass().getResourceAsStream("/icons/user.png"));
        } else {
           // image = new Image(getClass().getResourceAsStream("/icons/female.png"));
          //  proImage.setImage(image);
        }
        //showProPic.setFill(new ImagePattern(image));
        clientName.setText(username);
        connectSocket();
    }
}
