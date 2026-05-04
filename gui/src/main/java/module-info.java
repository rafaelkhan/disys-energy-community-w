module at.uastw.energycommunity.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens at.uastw.energycommunity.gui to javafx.fxml, com.fasterxml.jackson.databind;
    exports at.uastw.energycommunity.gui;
}
