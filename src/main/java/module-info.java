module com.leftings.project2opt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires org.json;
    requires jbcrypt;

    opens com.groepb.project2 to javafx.graphics;
    exports com.groepb.project2;
    exports com.groepb.project2.Windows;
    opens com.groepb.project2.Windows to javafx.graphics;
    exports com.groepb.project2.AI;
    opens com.groepb.project2.AI to javafx.graphics;
    exports com.groepb.project2.AI.BobGPT;
    opens com.groepb.project2.AI.BobGPT to javafx.graphics;
}