package gamePackage;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.graphics.interfaces.BasicInteractableObject;
import engine.toolBox.DrawHelper;
import gamePackage.Game.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

    public class GameController implements BasicInteractableObject {

                //Attribute

                //Referenzen
            private Display display;

            private GUI gui;
            private Player player;
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

        @Override
        public void keyPressed(KeyEvent e) {

                //Refresh
            if(e.getKeyCode() == KeyEvent.VK_R) {

                player.updateClientData();
                gameField.refresh();
                questbook.refresh();
                player.updateMySQLData();
            }

                //Cheat
            if(e.getKeyCode() == KeyEvent.VK_C) {

                player.deposit(10000, 10000, 10000, 100000, 0);
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {

        }

        @Override
        public void mouseReleased(MouseEvent event) {

        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

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

        public void setPlayer(Player player) {
            this.player = player;
        }

        public void setQuestbook(Questbook questbook) {
            this.questbook = questbook;
        }

        public void setGameField(GameField gameField) {
            this.gameField = gameField;
        }
    }
