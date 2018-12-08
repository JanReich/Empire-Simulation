package gamePackage;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import gamePackage.Game.*;

    public class GameController {

                //Attribute

                //Referenzen
            private Display display;

            private GUI gui;
            private Questbook questbook;
            private GameField gameField;
            private DatabaseConnector connector;

            private LoadingScreen loadingScreen;

        public GameController(DatabaseConnector connector, Display display, String mail) {

            this.display = display;
            this.connector = connector;

            loadingScreen = new LoadingScreen(connector, this, display, mail);
            display.getActivePanel().drawObjectOnPanel(loadingScreen);
        }

        public void init() {

            display.getActivePanel().removeObjectFromPanel(loadingScreen);

            display.getActivePanel().drawObjectOnPanel(gameField);
            display.getActivePanel().drawObjectOnPanel(gui, 100);
            display.getActivePanel().drawObjectOnPanel(questbook, 100);
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

        public void setGui(GUI gui) {
            this.gui = gui;
        }

        public void setQuestbook(Questbook questbook) {
            this.questbook = questbook;
        }

        public void setGameField(GameField gameField) {
            this.gameField = gameField;
        }
    }
