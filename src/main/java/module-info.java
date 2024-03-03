module isi.dev.javasocketexam {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires mysql.connector.j;
    requires java.sql;
    requires java.sql.rowset;

    opens isi.dev.javasocketexam to javafx.fxml;
    opens isi.dev.javasocketexam.entities to org.hibernate.orm.core;

    exports isi.dev.javasocketexam;

}