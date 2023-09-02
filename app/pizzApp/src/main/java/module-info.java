module com.example.pizzapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
            
                            
    opens com.example.pizzapp to javafx.fxml;
    opens com.example.pizzapp.controller to javafx.fxml;
    exports com.example.pizzapp;
}