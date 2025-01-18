module com.sda.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sda.tictactoe to javafx.fxml;
    exports com.sda.tictactoe;
}