package com.example.pizzapp.controller.admin;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.utils.DatabaseConnection;
import com.example.pizzapp.utils.Ingrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMenuManager implements Initializable {

    @FXML
    private Button bttAddPizza;

    @FXML
    private Button bttModificaPrezzo;

    @FXML
    private Button bttSegnaDisp;

    @FXML
    private Button bttSegnaNonDisp;

    @FXML
    private TableColumn<Ingrediente, String> colIng;

    @FXML
    private TableColumn<Pizza, String> colListaIng;

    @FXML
    private TableColumn<Pizza, Boolean> colDisp;

    @FXML
    private TableColumn<Pizza, String> colNome;

    @FXML
    private TableView<Ingrediente> tableIng;

    @FXML
    private TableView<Pizza> tableMenu;

    @FXML
    private TextField txtListaIng;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtNuovoPrezzo;

    @FXML
    private TextField txtPrezzo;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Pizza selectedPizza;

    private Ingrediente selectedIngrediente;

    @FXML
    public void addPizza(ActionEvent event) {
        this.connect = DatabaseConnection.connectDb();
        List<String> div = new ArrayList<>(Arrays.asList(this.txtListaIng.getText().split(", ")));
        try {
            String queryPizza = "INSERT INTO Pizze (nome, prezzoBase) VALUES (?, ?)";
            this.pst = this.connect.prepareStatement(queryPizza);
            this.pst.setString(1, this.txtNome.getText());
            this.pst.setDouble(2, Double.parseDouble(this.txtPrezzo.getText()));
            this.pst.executeUpdate();
            String queryFarc = "INSERT INTO Farciture (pizza, ingrediente) VALUES (?, ?);";
            String query = "INSERT IGNORE INTO Ingredienti (nome, prezzo) VALUES (?, ?)";
            for (String str : div) {
                this.pst = this.connect.prepareStatement(query);
                PreparedStatement tmpPst = this.connect.prepareStatement(queryFarc);
                this.pst.setString(1, str);
                this.pst.setDouble(2, 1.5);
                this.pst.executeUpdate();
                tmpPst.setString(1, txtNome.getText());
                tmpPst.setString(2, str);
                tmpPst.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        popolateTablePizza();
        popolateTableIng();
    }

    @FXML
    public void modificaPrezzo(ActionEvent event) {
        String query = "UPDATE Pizze SET prezzoBase = ? WHERE nome = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setDouble(1, Double.parseDouble(this.txtNuovoPrezzo.getText()));
            this.pst.setString(2, this.selectedPizza.getName());
            this.pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTablePizza();
    }

    @FXML
    public void selectPizza() {
        this.selectedPizza = this.tableMenu.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void selectIng() {

    }

    @FXML
    public void segnaDisp(ActionEvent event) {
        updateDisp(true);
    }

    @FXML
    public void segnaNonDisp(ActionEvent event) {
        updateDisp(false);
    }

    public void updateDisp(Boolean flag) {
        if (this.selectedPizza != null){
            String query = "UPDATE Pizze SET presente = ? WHERE nome = ?";
            this.connect = DatabaseConnection.connectDb();
            try {
                this.pst = this.connect.prepareStatement(query);
                this.pst.setBoolean(1, flag);
                this.pst.setString(2, this.selectedPizza.getName());
                this.pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        popolateTablePizza();
    }

    public void popolateTablePizza() {
        ObservableList<Pizza> list = FXCollections.observableArrayList();
        String queryGetName = "SELECT\n" +
                "    Pizze.nome AS NomePizza,\n" +
                "    Pizze.prezzoBase AS PrezzoBase, \n" +
                "    Pizze.presente AS Presente,\n" +
                "    GROUP_CONCAT(Ingredienti.nome SEPARATOR ', ') AS Ingredienti\n" +
                "FROM Pizze\n" +
                "LEFT JOIN Farciture ON Pizze.nome = Farciture.pizza\n" +
                "LEFT JOIN Ingredienti ON Farciture.ingrediente = Ingredienti.nome\n" +
                "GROUP BY Pizze.nome, Pizze.prezzoBase\n" +
                "ORDER BY Pizze.nome;\n";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryGetName);
            this.rs = this.pst.executeQuery();
            Pizza tmp;
            while (this.rs.next()) {
                String[] ingredienti = this.rs.getString("Ingredienti").split(", ");
                tmp = new Pizza(this.rs.getString("NomePizza"), this.rs.getDouble("PrezzoBase"),
                        this.rs.getBoolean("Presente"), Arrays.asList(ingredienti));
                list.add(tmp);
            }
            this.connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colListaIng.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
        this.colDisp.setCellValueFactory(new PropertyValueFactory<>("present"));
        this.tableMenu.setItems(list);
    }

    public void popolateTableIng() {
        ObservableList<Ingrediente> list = FXCollections.observableArrayList();
        String queryGetIngredients = "SELECT * FROM Ingredienti";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryGetIngredients);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                list.add(new Ingrediente(this.rs.getString("nome"), this.rs.getDouble("prezzo")));
            }
            this.connect.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colIng.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.tableIng.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTableIng();
        popolateTablePizza();
    }
}

