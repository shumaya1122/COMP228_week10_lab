
package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Main Layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // Form Title
        Label title = new Label("EMPLOYMENT APPLICATION");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        // Personal Information
        Label personalTitle = new Label("Personal Information");
        personalTitle.setStyle(
            "-fx-background-color: black; -fx-text-fill: white; " +
            "-fx-padding: 10; -fx-font-weight: bold;"
        );

        TextField fullName = new TextField();
        TextField address = new TextField();
        TextField contact = new TextField();
        TextField email = new TextField();
        TextField education = new TextField();

        fullName.setPromptText("Full Name");
        address.setPromptText("Current Address");
        contact.setPromptText("Contact Number");
        email.setPromptText("Email Address");
        education.setPromptText("Highest Educational Attainment");

        // Gender
        RadioButton male = new RadioButton("Male");
        RadioButton female = new RadioButton("Female");
        RadioButton other = new RadioButton("Other");

        ToggleGroup genderGroup = new ToggleGroup();

        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        other.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(15, male, female, other);

        // Employment Eligibility
        Label employmentTitle = new Label("Employment Eligibility");
        employmentTitle.setStyle(
            "-fx-background-color: black; -fx-text-fill: white; " +
            "-fx-padding: 10; -fx-font-weight: bold;"
        );

        DatePicker dateAvailable = new DatePicker();

        TextField position = new TextField();
        TextField salary = new TextField();

        position.setPromptText("Desired Position");
        salary.setPromptText("Desired Salary");

        // Work Authorization
        RadioButton workYes = new RadioButton("Yes");
        RadioButton workNo = new RadioButton("No");

        ToggleGroup workGroup = new ToggleGroup();
        workYes.setToggleGroup(workGroup);
        workNo.setToggleGroup(workGroup);

        HBox workBox = new HBox(15, workYes, workNo);

        // Relatives
        RadioButton relativesYes = new RadioButton("Yes");
        RadioButton relativesNo = new RadioButton("No");

        ToggleGroup relativesGroup = new ToggleGroup();
        relativesYes.setToggleGroup(relativesGroup);
        relativesNo.setToggleGroup(relativesGroup);

        HBox relativesBox = new HBox(15, relativesYes, relativesNo);

        TextField explanation = new TextField();
        explanation.setPromptText("If yes, please explain further");

        // Signature
        TextArea signature = new TextArea();
        signature.setPromptText("Applicant's Signature");
        signature.setPrefRowCount(2);

        // Submit Button
        Button submitButton = new Button("Submit");
        

     // Submit Button Action
     submitButton.setOnAction(event -> {

         try {
             // Get selected Gender
             RadioButton selectedGender =
                     (RadioButton) genderGroup.getSelectedToggle();

             // Get selected Work Authorization
             RadioButton selectedWork =
                     (RadioButton) workGroup.getSelectedToggle();

             // Get selected Relatives option
             RadioButton selectedRelatives =
                     (RadioButton) relativesGroup.getSelectedToggle();

             // Validate required selections
             if (fullName.getText().trim().isEmpty()
                     || selectedGender == null
                     || selectedWork == null
                     || selectedRelatives == null) {

                 Alert alert = new Alert(Alert.AlertType.WARNING);
                 alert.setTitle("Missing Information");
                 alert.setHeaderText("Please complete the required fields.");
                 alert.showAndWait();
                 return;
             }

             // Create EmploymentForm object
             EmploymentForm employment = new EmploymentForm();

             employment.setFullName(fullName.getText());
             employment.setCurrentAddress(address.getText());
             employment.setContactNumber(contact.getText());
             employment.setEmailAddress(email.getText());
             employment.setEducation(education.getText());

             employment.setGender(selectedGender.getText());

             employment.setDateAvailable(dateAvailable.getValue());

             employment.setDesiredPosition(position.getText());
             employment.setDesiredSalary(salary.getText());

             employment.setAuthorizedToWork(
                     selectedWork.getText().equals("Yes"));

             employment.setRelativesWorking(
                     selectedRelatives.getText().equals("Yes"));

             employment.setExplanation(explanation.getText());
             employment.setSignature(signature.getText());

             // Save information to Database
             EmploymentFormDao dao = new EmploymentFormDao();

             int generatedId = dao.saveEmploymentForm(employment);

             if (generatedId > 0) {

                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Success");
                 alert.setHeaderText("Application Submitted Successfully!");
                 alert.setContentText(
                         "Your application ID is: " + generatedId);

                 alert.showAndWait();

             } else {

                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error");
                 alert.setHeaderText("Application was not saved.");
                 alert.showAndWait();
             }

         } catch (Exception e) {

             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Database Error");
             alert.setHeaderText("Could not save your application.");
             alert.setContentText(e.getMessage());
             alert.showAndWait();

             e.printStackTrace();
         }
     });


        // Add components
        root.getChildren().addAll(
            title,
            personalTitle,
            new Label("Full Name"), fullName,
            new Label("Current Address"), address,
            new Label("Contact Number"), contact,
            new Label("Email Address"), email,
            new Label("Highest Educational Attainment"), education,
            new Label("Gender"), genderBox,
            employmentTitle,
            new Label("Date Available"), dateAvailable,
            new Label("Desired Position"), position,
            new Label("Desired Salary"), salary,
            new Label("Are you legally authorized to work in the country?"),
            workBox,
            new Label("Do you have relatives working for our company?"),
            relativesBox,
            explanation,
            signature,
            submitButton
        );

        // Scene
     // Scroll Pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        // Scene
        Scene scene = new Scene(scrollPane, 600, 850);

        primaryStage.setTitle("Employment Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
