package AdminPanel;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.config.MySQLConfig;
import engine.graphics.Display;

import java.awt.*;

    public class AdminPanelMain {

                //Attribute

                //Referenzen
            private Display display;
            private DatabaseConnector connector;

        public AdminPanelMain() {

            display = new Display();

            mySQLSetup();

            Dashboard dashboard = new Dashboard(connector, display);
            display.getActivePanel().drawObjectOnPanel(dashboard);
        }

        private void mySQLSetup() {

            MySQLConfig config = new MySQLConfig();
            connector = new DatabaseConnector(config.getHost(), Integer.parseInt(config.getPort()), config.getDatabase(), config.getUsername(), config.getPassword());
        }

        public static void main(String[] args) {

            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {

                    new AdminPanelMain();
                }
            });
        }
    }
