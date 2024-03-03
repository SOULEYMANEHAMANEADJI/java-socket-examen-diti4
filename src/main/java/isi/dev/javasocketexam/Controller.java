package isi.dev.javasocketexam;


import isi.dev.javasocketexam.dao.IMembre;
import isi.dev.javasocketexam.dao.MembreImpl;
import isi.dev.javasocketexam.entities.Membre;
import isi.dev.javasocketexam.server.Server;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    @FXML
    public Pane pnSignIn;
    @FXML
    public Pane pnSignUp;
    @FXML
    public Button btnSignUp;
    @FXML
    public Button getStarted;
    @FXML
    public ImageView btnBack;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    public Label success;
    @FXML
    public Label goBack;
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;
    @FXML
    public Label checkPhone;
    @FXML
    private Label connectionMessage;


    public static String username, password, gender;
    public static Membre mbre = new Membre();
    public static ArrayList<Membre> loggedInMembre = new ArrayList<>();
    private IMembre membreDao = new MembreImpl();



    public void registration() {
        if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if (checkClientNameExist(regName.getText())) {
                if (checkClientEmailExist(regEmail.getText())) {
                    if (checkClientPhoneExist(regPhoneNo.getText())) {
                        Membre newUser = new Membre();
                        newUser.setName(regName.getText());
                        newUser.setPassword(regPass.getText());
                        newUser.setEmail(regEmail.getText());
                        newUser.setFullName(regFirstName.getText());
                        newUser.setPhoneNo(regPhoneNo.getText());
                        if (male.isSelected()) {
                            newUser.setGender("Masculin");
                        } else {
                            newUser.setGender("Féminin");
                        }
                        membreDao.create(newUser);
                        goBack.setOpacity(1);
                        success.setOpacity(1);
                        makeDefault();
                        if (controlRegLabel.getOpacity() == 1) {
                            controlRegLabel.setOpacity(0);
                        }
                        if (nameExists.getOpacity() == 1) {
                            nameExists.setOpacity(0);
                        }
                    } else {
                        checkPhone.setOpacity(1);
                        setOpacity(success, goBack, controlRegLabel, checkEmail);
                        nameExists.setOpacity(0);
                    }
                } else {
                    checkEmail.setOpacity(1);
                    setOpacity(success, goBack, controlRegLabel, nameExists);
                    checkPhone.setOpacity(0);
                }
            } else {
                nameExists.setOpacity(1);
                setOpacity(success, goBack, controlRegLabel, checkEmail);
                checkPhone.setOpacity(0);
            }

        } else {
            controlRegLabel.setOpacity(1);
            setOpacity(success, goBack, nameExists, checkEmail);
        }
    }

    private boolean checkClientNameExist(String name) {
        // Vérification si le nom existe déjà dans la base de données
        Membre existingNameMember = membreDao.findByName(name);
        if (existingNameMember != null) {
            return false;
        }
        return true;
    }

    private boolean checkClientEmailExist(String email) {
        // Vérification si l'email existe déjà dans la base de données
        Membre existingEmailMember = membreDao.findByEmail(email);
        if (existingEmailMember != null) {
            return false;
        }
        return true;
    }

    private boolean checkClientPhoneExist(String phoneNo) {
        // Vérification si le numéro de téléphone existe déjà dans la base de données
        Membre existingPhoneNoMember = membreDao.findByPhoneNo(phoneNo);
        if (existingPhoneNoMember != null) {
            return false;
        }
        return true;
    }

    public void login() {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        Membre membre = new Membre();
        membre = membreDao.findNameAndPassword(username, password);
        mbre=membre;
            if (membre != null) {
                login = true;
                loggedInMembre.add(membre);
                System.out.println(membre.getName());
                gender = membre.getGender();
            }
            if (login) {
                changeWindow();
            } else {
                loginNotifier.setOpacity(1);
            }

    }

    public void changeWindow() {

        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }

    private void setOpacity(Label a, Label b, Label c, Label d) {
        if (a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }

    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnSignUp)) {
            fadeTransition(pnSignUp);
            pnSignUp.toFront();
        }
        if (event.getSource().equals(getStarted)) {
            fadeTransition(pnSignIn);
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnBack) {
            fadeTransition(pnSignIn);
            pnSignIn.toFront();
        }
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
    }

    private void fadeTransition(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(1000), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }


}