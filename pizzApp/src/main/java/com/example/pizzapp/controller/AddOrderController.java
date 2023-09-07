package com.example.pizzapp.controller;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddOrderController implements Initializable {


//    @FXML
//    private Button bttAddIndirizzo;
//
//    @FXML
//    private Button bttAddIngredienti;
//
//    @FXML
//    private Button bttAddPizza;
//
//    @FXML
//    private Button bttConfirmOrder;
//
//    @FXML
//    private Button bttRemovePizza;

    @FXML
    private Button bttUseCoupon;

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
    private Label lbTotal;

    @FXML
    private RadioButton radioConsegna;

    @FXML
    private RadioButton radioRitiro;

    @FXML
    private TableView<Pizza> tableMenu;

    @FXML
    private TableView<Orario> tableOrario;

//    @FXML
//    private ToggleGroup toggleOrderType;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private LocalTime selectedTime;

    private String selectedType;

    private List<PizzaAndIngred> pizzaList = new ArrayList<>();

    private Pizza currentSelectedPizza;

    private ObservableList<Pizza> menu;

//    private ObservableList<Ingrediente> ingredientesList;

    private Ingrediente currentSelectedIngrediente;

    private List<String> ingredToAdd = new ArrayList<>();

    @FXML
    private ComboBox<Address> comboAddress;

    @FXML
    private TextField txtComune;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtVia;

    private static double sconto;

    @FXML
    void addIndirizzo() {
        String queryUpdate = "INSERT IGNORE INTO Indirizzi (via, numero, comune)" +
                "VALUES (?, ?, ?)";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryUpdate);
            this.pst.setString(1, this.txtVia.getText());
            this.pst.setInt(2, Integer.parseInt(this.txtNumero.getText()));
            this.pst.setString(3, this.txtComune.getText());
            this.pst.executeUpdate();
            if (!User.getTipo().equals("admin")) {
                System.out.println("User è: " + User.getTipo());
                System.out.println("Dato che non è un amministratore inserisco anche in domiciliazioni");
                String queryDomic = "INSERT INTO Domiciliazioni (utente, via, numero, comune)" +
                        "VALUES (?, ?, ?, ?)";
                this.pst = this.connect.prepareStatement(queryDomic);
                this.pst.setInt(1, User.getCodUtente());
                this.pst.setString(2, this.txtVia.getText());
                this.pst.setInt(3, Integer.parseInt(this.txtNumero.getText()));
                this.pst.setString(4, this.txtComune.getText());
                this.pst.executeUpdate();
            }
            popolateComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            Double price = 0.0;
            String query = "SELECT prezzo FROM Ingredienti WHERE nome = ?";
            this.connect = DatabaseConnection.connectDb();
            try {
                for(String ingName : this.ingredToAdd) {
                    this.pst = this.connect.prepareStatement(query);
                    this.pst.setString(1, ingName);
                    this.rs = this.pst.executeQuery();
                    if (this.rs.next()) {
                        price = price + this.rs.getDouble("prezzo");
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            price = price + this.currentSelectedPizza.getPrice();
            this.pizzaList.add(new PizzaAndIngred(this.currentSelectedPizza.getName(), List.copyOf(this.ingredToAdd), price));
            this.ingredToAdd.clear();
            Double allTotal = 0.0;
            for (PizzaAndIngred e : this.pizzaList) {
                allTotal = allTotal + e.getPrice();
            }
            this.lbTotal.setText(allTotal.toString());
        }
    }

    @FXML
    void confirmOrder(ActionEvent event) {
        Boolean flagSconto = sconto > 0.0;
        Double allTotal = 0.0;
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
                "    ?,\n" +
                "    'In attesa',\n" +
                "    FALSE,\n" +
                "    ?,\n" +
                "    ?, -- Orario di ritiro\n" +
                "    ?,  -- Orario di effettuazione\n" +
                "    ?\n" +
                ");";
        try {
            this.pst = this.connect.prepareStatement(queryInsertOrder);
            for (PizzaAndIngred e : this.pizzaList) {
                allTotal = allTotal + e.getPrice();
            }
            allTotal = allTotal * (1-sconto);
            sconto = 0.0; //Per evitare usi multipli di coupon
            this.pst.setDouble(1, allTotal);
            this.pst.setInt(2, User.getCodUtente());
            this.pst.setObject(3, rit);
            this.pst.setObject(4, eff);
            this.pst.setString(5, this.selectedType);
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
            if (this.radioConsegna.isSelected()) {
                String queryDest = "INSERT INTO Destinazioni (ordine, via, numero, comune)" +
                        "VALUES (?, ?, ?, ?)";
                this.pst = this.connect.prepareStatement(queryDest);
                this.pst.setInt(1, orderId);
                Address addr = this.comboAddress.getSelectionModel().getSelectedItem();
                this.pst.setString(2, addr.getVia());
                this.pst.setInt(3, addr.getNumero());
                this.pst.setString(4, addr.getComune());
                this.pst.executeUpdate();
            }
            if (!flagSconto) {
                updatePoint(allTotal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePoint(double tot) {
        String queryUpdatePoint = "UPDATE Carte_fedelta AS cf " +
                "INNER JOIN Appartenenze AS a ON cf.codCarta = a.cartaFedelta " +
                "SET cf.punti = ? " +
                "WHERE a.utente = ?";
        ;
        this.connect = DatabaseConnection.connectDb();
        try {
            PreparedStatement tmpPst = this.connect.prepareStatement(queryUpdatePoint);
            tmpPst.setInt(1, (int) (getPunti() + tot));
            tmpPst.setInt(2, User.getCodUtente());
            tmpPst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int getPunti() {
        String query = "SELECT C.punti\n" +
                "FROM Utenti U\n" +
                "INNER JOIN Appartenenze A ON U.codUtente = A.utente\n" +
                "INNER JOIN Carte_fedelta C ON A.cartaFedelta = C.codCarta\n" +
                "WHERE U.codUtente = ?;";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.rs = this.pst.executeQuery();
            if (this.rs.next()) {
                return this.rs.getInt("punti");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
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

    @FXML
    public void openCouponPopup() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pizzapp/fxmlFile/cliente/popupCoupon.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage popupStage = new Stage();
            popupStage.setScene(scene);
            popupStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void popolateComboBox() {
        ObservableList<Address> addresses = FXCollections.observableArrayList();
        String queryAddress = (User.getTipo().equals("admin") ? "SELECT * FROM Indirizzi" :
                "SELECT * FROM Domiciliazioni WHERE utente = ?");
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryAddress);
            if (!User.getTipo().equals("admin")) {
                this.pst.setInt(1, User.getCodUtente());
            }
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                addresses.add(new Address(this.rs.getString("via"), this.rs.getInt("numero"),
                        this.rs.getString("comune")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.comboAddress.setItems(addresses);
    }

    public static void setSconto(double s) {
        sconto = s;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTableMenu();
        popolateTableIngredienti();
        popolateComboBox();
        if(User.getTipo().equals("cliente")){
            this.bttUseCoupon.setVisible(true);
        }
    }
}