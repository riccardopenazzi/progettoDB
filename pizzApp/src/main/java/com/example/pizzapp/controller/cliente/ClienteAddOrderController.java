package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteAddOrderController implements Initializable {

    @FXML
    private Button bttAddIngredienti;

    @FXML
    private Button bttAddPizza;

    @FXML
    private Button bttConfirmOrder;

    @FXML
    private Button bttRemovePizza;

    @FXML
    private TableColumn<Pizza, String> colIngredienti;

    @FXML
    private TableColumn<Pizza, String>  colNome;

    @FXML
    private TableColumn<Orario, LocalTime> colOrario;

    @FXML
    private TableColumn<Pizza, Double> colPrezzo;

    @FXML
    private TableColumn<Ingrediente, String> colAggiunte;

    @FXML
    private TableView<Ingrediente> tableAggiunge;

    @FXML
    private RadioButton radioConsegna;

    @FXML
    private RadioButton radioRitiro;

    @FXML
    private TableView<Pizza> tableMenu;

    @FXML
    private TableView<Orario> tableOrario;

    @FXML
    private ToggleGroup toggleOrderType;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private LocalTime selectedTime;

    private String selectedType;

    private List<PizzaAndIngred> pizzaList = new ArrayList<>();

    private Pizza currentSelectedPizza;

    private ObservableList<Pizza> menu;

    private ObservableList<Ingrediente> ingredientesList;

    private Ingrediente currentSelectedIngrediente;

    private List<String> ingredToAdd = new ArrayList<>();

    @FXML
    void showOrari(ActionEvent event) {
        this.selectedTime = null;
        this.tableOrario.setItems(null);
        if(this.radioConsegna.isSelected()) {
            this.selectedType = "consegna";
            List<LocalTime> available = FXCollections.observableArrayList(
                    OrariDisponibili.getList(18, 00, checkNotAvailable()));
            List<Orario> tmp = new ArrayList<>();
            for (LocalTime e : available) {
                tmp.add(new Orario(e));
            }
            ObservableList<Orario> toShow = FXCollections.observableArrayList(tmp);
            this.colOrario.setCellValueFactory(new PropertyValueFactory<>("ora"));
            this.tableOrario.setItems(toShow);
        } else if (this.radioRitiro.isSelected()) {
            this.selectedType = "ritiro";
            List<LocalTime> available = FXCollections.observableArrayList(
                    OrariDisponibili.getList(18, 00, null));
            List<Orario> tmp = new ArrayList<>();
            for (LocalTime e : available) {
                tmp.add(new Orario(e));
            }
            ObservableList<Orario> toShow = FXCollections.observableArrayList(tmp);
            this.colOrario.setCellValueFactory(new PropertyValueFactory<>("ora"));
            this.tableOrario.setItems(toShow);
        }
    }

    @FXML
    void addIngredienti(ActionEvent event) {
        if (this.currentSelectedIngrediente != null) {
            this.ingredToAdd.add(this.currentSelectedIngrediente.getName());
        }
    }

    @FXML
    void addPizza(ActionEvent event) {
        if(this.currentSelectedPizza != null) {
            this.pizzaList.add(new PizzaAndIngred(this.currentSelectedPizza.getName(), List.copyOf(this.ingredToAdd)));
            System.out.println("La pizza è così modellata: " + this.currentSelectedPizza.getName() +
                    " " +  this.ingredToAdd.toString());
            this.ingredToAdd.clear();
        }
    }

    @FXML
    void confirmOrder(ActionEvent event) {
        //inserimento orari
        this.connect = DatabaseConnection.connectDb();
        String queryTempi = "INSERT IGNORE into Tempi (orario) VALUES (?), (?)";
        LocalDateTime eff = LocalDateTime.now();
        LocalDateTime rit = this.selectedTime.atDate(LocalDate.now());
        try {
            this.pst = this.connect.prepareStatement(queryTempi);
            this.pst.setObject(1, eff);
            this.pst.setObject(2, rit);
            this.pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //creo ordine
        String queryInsertOrder = "INSERT INTO Ordini (totale, stato, assegnata, utente, orarioRitiro, orarioEffettuazione, tipo)\n" +
                "VALUES (\n" +
                "    0,\n" +
                "    'In attesa',\n" +
                "    FALSE,\n" +
                "    ?,\n" +
                "    ?, -- Orario di ritiro\n" +
                "    ?,  -- Orario di effettuazione\n" +
                "    ?\n" +
                ");";
        try {
            this.pst = this.connect.prepareStatement(queryInsertOrder);
            this.pst.setInt(1, User.getCodUtente());
            this.pst.setObject(2, rit);
            this.pst.setObject(3, eff);
            this.pst.setString(4, this.selectedType);
            this.pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //inserisco le pizze
        int orderId = 0;
        try {
            ResultSet gen = this.connect.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
            if (gen.next()) {
                orderId = gen.getInt(1);
            }
            String queryAddPizza = "INSERT INTO Composizioni (ordine, pizza, ingredienti_array)\n" +
                    "VALUES (?, ?, ?);\n";
            for (PizzaAndIngred e : pizzaList) {
                System.out.println("Aggiungo pizza: " + e);
                this.pst = this.connect.prepareStatement(queryAddPizza);
                this.pst.setInt(1, orderId);
                this.pst.setString(2, e.getPizzaName());
                String strIng = "";
                for (String str : e.getIngredients()) {
                    System.out.println("Ingrediente: " + str);
                    strIng = strIng + str + ", ";
                }
                System.out.println("La stringa degli ingredienti è: " + strIng);
                this.pst.setString(3, strIng);
                this.pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void removePizza(ActionEvent event) {

    }

    @FXML
    void selectPizza(MouseEvent event) {
        currentSelectedPizza = this.tableMenu.getSelectionModel().getSelectedItem();
        System.out.println("Stai selezionando " + this.currentSelectedPizza.getName());
    }

    @FXML
    void selectTime(MouseEvent event) {
        Orario orario = this.tableOrario.getSelectionModel().getSelectedItem();
        this.selectedTime = orario.getOra();
    }

    private List<LocalTime> checkNotAvailable() {
        List<LocalTime> toReturn = new ArrayList<>();
        String query = "SELECT orario, SUM(contatore) AS somma_contatori\n" +
                "FROM Assegnazioni\n" +
                "WHERE DATE(orario) = ? -- Sostituisci con la data specifica\n" +
                "GROUP BY orario\n" +
                "HAVING SUM(contatore) >= 6;";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setDate(1, Date.valueOf(LocalDate.now()));
            this.rs = this.pst.executeQuery();
            while(this.rs.next()) {
                LocalTime dataCompleta = this.rs.getTime("orario").toLocalTime();
                toReturn.add(LocalTime.of(dataCompleta.getHour(), dataCompleta.getMinute()));
            }
            this.connect.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toReturn;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @FXML
    void selectAggiunta(MouseEvent event) {
        this.currentSelectedIngrediente = this.tableAggiunge.getSelectionModel().getSelectedItem();
        System.out.println("Stai selezionando " + this.currentSelectedIngrediente.getName());
    }

    public void popolateTableMenu() {
        this.menu = readMenu();
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colIngredienti.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
        this.colPrezzo.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.tableMenu.setItems(this.menu);
    }

    public void popolateTableIngredienti() {
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
        this.colAggiunte.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.tableAggiunge.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTableMenu();
        popolateTableIngredienti();
    }
}
