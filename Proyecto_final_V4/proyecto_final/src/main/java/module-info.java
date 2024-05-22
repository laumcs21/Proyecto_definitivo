module co.edu.uniquindio.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;

    // Open the package to both javafx.fxml and junit
    opens co.edu.uniquindio.poo to javafx.fxml, org.junit.jupiter.api;

    exports co.edu.uniquindio.poo;
}
