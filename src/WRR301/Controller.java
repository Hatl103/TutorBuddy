package WRR301;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.*;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller extends ListView<String> implements Initializable {

    //Current Student's Student Number
    int studentNumber;
    //RequestTutorUI
    public TextField rtModuleTextField;
    public TextField TRStudNrField;

    //Landing Page
    public ListView myTutorList;
    ObservableList<String> observableListMyTutorList = FXCollections.observableArrayList("Thabo","Mike");

    //Login Page
    public CheckBox showPasswordStart;
    public Label passwordLabelStart;
    public PasswordField passwordFieldStart;
    public TextField studentNrStart;

    //Tutor Application
    public TextField TAStudNr;
    public TextField TAMod;

    //update Details
    public TextField emailFieldUpdateDets;
    public TextField surnameFieldUpdateDets;
    public TextField nameFieldUpdateDets;
    public PasswordField passwordFieldAUpdateDets;
    public PasswordField passwordFieldBUpdateDets;
    public CheckBox showPasswordUpdateDets;
    public TextField contactNrFieldUpdateDets;
    public Label showPasswordLableUpdateDets;


    //Database
    Statement statement;
    PreparedStatement preparedStatement;
    Connection connection;

    //Stage and Scene
    private Stage stage;
    private Scene scene;
    private Parent root;

    //Register Account UI References
    public TextField studentNrField;
    public TextField nameField;
    public TextField surnameField;
    public TextField emailField;
    public TextField contactNrField;
    public PasswordField passwordFieldA;
    public PasswordField passwordFieldB;
    public CheckBox showPassword;
    public Label passwordFeedback;
    public Label passwordLabel;

    //Sessions
    public ComboBox Hours;
    public ComboBox Minutes;
    public ComboBox Module;
    public DatePicker Date;
    public TextField NrOfstud;


    static class Cell extends ListCell<String>
    {
        HBox hBox = new HBox();
        Button btn = new Button("View Content");

        Label lbl = new Label("");
        Pane pane = new Pane();

        public Cell()
        {
            super();
            hBox.getChildren().addAll(lbl,pane,btn);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }
        public  void updateItem(String t, boolean empty)
        {
            super.updateItem(t, empty);
            setStyle("-fx-background-color: #5578eb");
            btn.setStyle("-fx-background-color: #04366b" );
            setText(null);
            setGraphic(null);
            if(t != null && empty ==false)
            {
                lbl.setText(t);
                lbl.setTextFill(Color.WHITE);
                btn.setTextFill(Color.WHITE);
                setGraphic(hBox);
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(connection == null)
        {
            try {
                connectToDB();
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Could not connect to DB");
            }
        }

        if(myTutorList!=null)
        {

            myTutorList.setItems(observableListMyTutorList);
            myTutorList.setCellFactory(param -> new Cell());

        }

    }

    public void registerAccount(ActionEvent event){

        int studNr = Integer.valueOf(studentNrField.getText());
        String fName = nameField.getText();
        String lName = surnameField.getText();
        String email = emailField.getText();
        int contactNr = Integer.valueOf(contactNrField.getText());
        String passwordA = passwordFieldA.getText();
        String passwordB = passwordFieldB.getText();
        if(passwordA.equals(passwordB)==false)
        {
            passwordFeedback.setText("Password does not match!");
            return;
        }
        else
        {
            System.out.println("Entering Record to Db");
            try{
                String sql = "Insert into Student(ST_Nr,ST_FName,ST_LName,ST_email,ST_ContactNr,studPassword) Values("+studNr+",'"+fName+"','"+lName+"','"+email+"',"+contactNr+",'"+passwordA+"')";
                statement.execute(sql);
                System.out.println("Record entered");
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("could not enter record");
            }


        }
    }
    public void connectToDB() throws Exception
    {
        System.out.println("loading driver");
        String databaseUrl = "jdbc:ucanaccess://ProjectDatabase.accdb";
        connection = DriverManager.getConnection(databaseUrl);
        System.out.println("Established connection");
        statement = connection.createStatement();
    }

    public void disconnectFromDB() throws Exception
    {
        System.out.println("Disconnecting from DB...");
        connection.close();
    }

    public void switchToRegistrationScene(ActionEvent event) throws Exception {
      switchScenes(event,"RegisterAccount.fxml");

    }
    public void switchtoCreatesession(ActionEvent event)throws Exception
    {
        switchScenes(event,"CreateSessions.fxml");
    }

    public void showPassword(ActionEvent event) {
        showPassword.selectedProperty().addListener((observable,oldVal, newVal)->{
            if(oldVal!=newVal)
            {
                passwordLabel.setText(passwordFieldA.getText());
                if(showPassword.isSelected()==true)
                {
                    passwordLabel.setVisible(true);
                }
                else
                if(showPassword.isSelected()==false)
                {
                    passwordLabel.setVisible(false);
                    passwordLabel.setText("");
                }
            }

        });

    }

    public void switchToLoginScene(ActionEvent event) throws Exception{
        switchScenes(event,"LoginUI.fxml");

    }
    private void switchScenes(ActionEvent event, String fxml) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLandingUI(ActionEvent event) throws Exception {

        String sql = "SELECT * FROM Student ";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next())
        {
            int studentNr = resultSet.getInt("ST_Nr");
            studentNumber = studentNr;
            String password = resultSet.getString("studPassword");
            if(Integer.valueOf(studentNrStart.getText())==studentNr && passwordFieldStart.getText().equals(password))
            {
                //sql = "SELECT * FROM Student INNER JOIN TutorModule ON Student.ST_Nr = TutorModule.ST_Nr_Tutor WHERE Student.ST_Nr ="+"";
                resultSet = statement.executeQuery(sql);
                switchScenes(event,"LandingPage.fxml");
            }
        }

        System.out.println("done traversing student");

    }

    public void switchToLandingUI2(ActionEvent event)throws Exception
    {
        switchScenes(event,"LandingPage.fxml");

    }


    public void showPasswordStart(ActionEvent event) {
        showPasswordStart.selectedProperty().addListener((observable,oldVal,newVal)->{
            if(oldVal!=newVal)
            {
                passwordLabelStart.setText(passwordFieldStart.getText());
                if(showPasswordStart.isSelected()==true)
                {
                    passwordLabelStart.setVisible(true);
                }
                else
                if(showPasswordStart.isSelected()==false)
                {
                    passwordLabelStart.setVisible(false);
                    passwordLabelStart.setText("");
                }
            }
        });
    }

    public void switchToTRFeedbackUI(ActionEvent event) throws Exception {
        System.out.println("Adding tutor request to database");
        String mod = rtModuleTextField.getText();
        int studNr = Integer.valueOf(TRStudNrField.getText());
        String requestId = studNr+mod;
        String sql = "INSERT INTO Request (requestID,ST_NR,ModuleCode) VALUES ('"+requestId+"',"+studNr+",'"+mod+"')";
        statement.execute(sql);
        System.out.println("request sent");
        switchScenes(event,"TutorRequestFeedback.fxml");
    }

    public void switchToRequestTutorUI(ActionEvent event) throws Exception {
        switchScenes(event,"RequestTutorUI.fxml");
    }

    public void switchToTAFeedback(ActionEvent event) throws Exception {

        int studNr = Integer.valueOf(TAStudNr.getText());
        String module = TAMod.getText().trim();
        if(module!="")
        {
            System.out.println("Entering application to database");
            String applicationID = studNr+module;
            String sql = "INSERT INTO Application(ST_NR,Module) VALUES('"+applicationID+"',"+studNr+",'"+module+"')";
            statement.execute(sql);
            switchScenes(event,"TutorApplicationFeedback.fxml");
        }


    }

    public void UpdateDetails(ActionEvent event) throws Exception{

        String name = nameFieldUpdateDets.getText().trim();
        String surname = surnameFieldUpdateDets.getText().trim();
        String email = emailFieldUpdateDets.getText().trim();
        int contact = Integer.valueOf(contactNrFieldUpdateDets.getText().trim());
        String passwordA = passwordFieldAUpdateDets.getText();
        String passwordB = passwordFieldBUpdateDets.getText();
        if(passwordA.equals(passwordB)==false)
        {
            showPasswordLableUpdateDets.setText("Password does not match");
            showPasswordLableUpdateDets.setVisible(true);
            return;
        }
        //String sql = "UPDATE Student SET ST_FName='"+name+" ', ST_LName='"+surname+"', studPassword ='"+passwordA+"',  ST_email='"+email+"', ST_ContactNr="+contact+ "WHERE ST_Nr= 219320489)";

        String sql = "UPDATE Student SET ST_FName = ?, ST_LName= ?, ST_email=?, ST_ContactNr=?, studPassword=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,surname);
        preparedStatement.setString(3,email);
        preparedStatement.setInt(4,contact);
        preparedStatement.setString(5,passwordA);
        preparedStatement.executeUpdate();


        System.out.println("Details updated");
        switchScenes(event,"UpdateDetailsFeedbackUI.fxml");


    }

    public void showPasswordUpdateDets(ActionEvent event) {
        showPasswordUpdateDets.selectedProperty().addListener((observable,oldVal,newVal)->{
            if(oldVal!=newVal)
            {
                showPasswordLableUpdateDets.setText(passwordFieldAUpdateDets.getText());
                if(showPasswordUpdateDets.isSelected()==true)
                {
                    showPasswordLableUpdateDets.setVisible(true);
                }
                else
                if(showPasswordUpdateDets.isSelected()==false)
                {
                    showPasswordLableUpdateDets.setVisible(false);
                    showPasswordLableUpdateDets.setText("");
                }
            }
        });
    }

    public void switchToUpdateDetsUI(ActionEvent event) throws Exception {
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("UpdateDetailsUI.fxml"));
        primaryStage.setTitle("Tutor Buddy");
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.show();

    }

    public void closeUpdateFeedback(ActionEvent event) throws Exception{
        switchScenes(event,"LandingPage.fxml");

    }
}
