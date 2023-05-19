package com.example.escritorioauditorio;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class datePiker_Threads extends Thread {

    public void run() {

        DatePicker fecha = new DatePicker();



        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {

            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                this.setDisable(false);
                this.setBackground(null);
                this.setTextFill(Color.BLACK);


                int day;
                int month;
                int year;


                try {

                    Connection c = ConexionBD.getConexion();
                    Statement stm = c.createStatement();
                    String sql = "SELECT fechaAgenda FROM datosusuario where fechaAgenda is not null";
                    ResultSet r = stm.executeQuery(sql);
                    //stm.execute(sql);

                    while (r.next()) {
                        day = item.getDayOfMonth();
                        month = item.getMonthValue();
                        year = item.getYear();
                        //ESTE METODO ES PARA MARCAR LOS DIAS EN DATEPICKER DE LA BASE DE DATOS

                        if (year == Integer.parseInt(r.getString("fechaAgenda").split("-")[0]) && month == Integer.parseInt(r.getString("fechaAgenda").split("-")[1]) && day == Integer.parseInt(r.getString("fechaAgenda").split("-")[2])) {
                            Paint color = Color.RED;
                            BackgroundFill fill = new BackgroundFill(color, null, null);
                            this.setBackground(new Background(fill));
                            this.setTextFill(Color.WHITESMOKE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // fines de semana en color verde
           /* DayOfWeek dayweek = item.getDayOfWeek();
            if (dayweek == DayOfWeek.SATURDAY || dayweek == DayOfWeek.SUNDAY) {
                this.setTextFill(Color.GREEN);
            }

            */
            }
        };

    }
}
