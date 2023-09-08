package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteShowMenuController implements Initializable {

    @FXML
    private Button bttFilter;

    @FXML
    private TableColumn<Pizza, String> colName;

    @FXML
    private TableColumn<Pizza, Double> colPrice;

    @FXML
    private TableColumn<Pizza, String> colIngredienti;

    @FXML
    private TableView<Pizza> tableMenu;

    @FXML
    private TextField txtIngredient;

    private ObservableList<Pizza> menu;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
    }

    public ObservableList<Pizza> readMenu() {
        ObservableList<Pizza> list = FXCollections.observableArrayList();
        String queryGetName = "SELECT\n" +
                "    Pizze.nome AS NomePizza,\n" +
                "    Pizze.prezzoBase AS PrezzoBase,\n" +
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
                tmp = new Pizza(this.rs.getString("NomePizza"), this.rs.getDouble("PrezzoBase"), Arrays.asList(ingredienti));
                list.add(tmp);
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void popolateTable() {
        this.menu = readMenu();
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colIngredienti.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
        this.colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.tableMenu.setItems(this.menu);
    }
}
