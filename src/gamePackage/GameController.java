package gamePackage;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import gamePackage.Game.*;

    public class GameController {

                //Attribute

                //Referenzen
            private Display display;

            private GUI gui;
            private Player player;
            private Refresh refresh;
            private Questbook questbook;
            private GameField gameField;
            private DatabaseConnector connector;


        public GameController(DatabaseConnector connector, Display display, String mail) {

            this.display = display;
            this.connector = connector;

            init(mail);
        }

        private void init(String mail) {

            player = new Player(connector, mail);
            refresh = new Refresh(player);

            questbook = new Questbook(display, connector, player,this);
            gameField = new GameField(connector, refresh, display, player, this);
            gui = new GUI(display, connector, gameField, player, this);

            display.getActivePanel().drawObjectOnPanel(gameField);
            display.getActivePanel().drawObjectOnPanel(gui, 100);
            display.getActivePanel().drawObjectOnPanel(questbook, 100);

            //Worker worker = new Worker();
            //display.getActivePanel().drawObjectOnPanel(worker, 110);
        }

        public void setBuildingmode(boolean active) {

            gameField.setBuildingMode(active);
        }

        public void refreshQuests() {

            questbook.refresh();
        }

        public GUI getGui() {

            return gui;
        }

        public Questbook getQuestbook() {

            return questbook;
        }
    }
