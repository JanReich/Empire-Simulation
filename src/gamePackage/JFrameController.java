package gamePackage;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.config.MySQLConfig;
import engine.graphics.Display;
import engine.toolBox.Math;
import gamePackage.Game.GUI;
import gamePackage.Game.GameField;
import gamePackage.Game.Player;
import gamePackage.LoginSystem.LoginManager;

    public class JFrameController {

                //Attribute
            /**
             * CurrentState drückt aus an welchem Punkt sich das Programm gerade befindet:
             * -1 = Kein aktives Overlay
             *  0 = Login / Registierung / Passwort vergessen
             *  1 = Spielstart
             */
            private int currentState;

                //Referenzen
            private Display display;
            private GameField gameField;
            private LoginManager manager;
            private DatabaseConnector connector;

        public JFrameController(Display display) {

            mySQLSetup();
            this.display = display;

                //Wenn das Programm gestartet wird, soll zunächst die Login-Maske aufgerufen werden.
            showLoginManager();
        }

        private void mySQLSetup() {

            MySQLConfig config = new MySQLConfig();
            connector = new DatabaseConnector(config.getHost(), Integer.parseInt(config.getPort()), config.getDatabase(), config.getUsername(), config.getPassword());
        }

        public void showLoginManager() {

            currentState = 0;
            this.manager = new LoginManager(display, connector, this);
        }

        public void removeLoginManager() {

            currentState = -1;
            display.getActivePanel().removeAllObjectsFromPanel();
        }

        public void startGame(String mail) {

            removeLoginManager();
            currentState = 1;

            GameController gameController = new GameController(connector, display, mail);
            display.getActivePanel().drawObjectOnPanel(gameController);
        }
    }
