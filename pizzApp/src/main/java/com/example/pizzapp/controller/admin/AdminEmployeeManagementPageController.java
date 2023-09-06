package com.example.pizzapp.controller.admin;

import com.example.pizzapp.utils.Employee;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminEmployeeManagementPageController implements Initializable {

    @FXML
    private Button bttAddEmployee;

    @FXML
    private Button bttRemoveEmployee;

    @FXML
    private TableColumn<Employee, String> colCognome;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, String> colNome;

    @FXML
    private TableColumn<Employee, String> colRuolo;

    @FXML
    private TableColumn<Employee, String> colTelefono;

    @FXML
    private ComboBox<String> comboRuolo;

    @FXML
    private TableView<Employee> tableEmployee;

    @FXML
    private TextField txtCognome;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtRuolo;

    @FXML
    private TextField txtPassword;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Employee selectedEmployee;

    @FXML
    void addEmployee(ActionEvent event) {
        String query = "INSERT INTO Utenti (nome, cognome, email, telefono, password, tipo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setString(1, txtNome.getText());
            this.pst.setString(2, txtCognome.getText());
            this.pst.setString(3, txtEmail.getText());
            this.pst.setString(4, txtTelefono.getText());
            this.pst.setString(5, txtPassword.getText());
            this.pst.setString(6, txtRuolo.getText());
            this.pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    void removeEmployee(ActionEvent event) {
        String query = "DELETE FROM Utenti WHERE codUtente = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.selectedEmployee.getCodice());
            this.pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    void selectEmployee(MouseEvent event) {
        this.selectedEmployee = this.tableEmployee.getSelectionModel().getSelectedItem();
        this.txtNome.setText(this.selectedEmployee.getNome());
        this.txtCognome.setText(this.selectedEmployee.getCognome());
        this.txtEmail.setText(this.selectedEmployee.getEmail());
        this.txtTelefono.setText(this.selectedEmployee.getTelefono());
        this.txtRuolo.setText(this.selectedEmployee.getRuolo());
    }

    public void popolateTable() {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Utenti WHERE tipo != 'admin' AND tipo != 'cliente'";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.rs = this.pst.executeQuery();
            while(this.rs.next()) {
                list.add(new Employee(this.rs.getString("nome"),
                        this.rs.getString("cognome"),
                        this.rs.getString("email"),
                        this.rs.getString("telefono"),
                        this.rs.getString("tipo"),
                        this.rs.getInt("codUtente")));
            }
            this.colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            this.colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
            this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            this.colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            this.colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
            this.tableEmployee.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectRuolo() {
        this.txtRuolo.setText(this.comboRuolo.getSelectionModel().getSelectedItem());
    }

    public void popolateCombo() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Aiuto cuoco");
        list.add("Pizzaiolo");
        list.add("Rider");
        this.comboRuolo.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
        popolateCombo();
    }
}