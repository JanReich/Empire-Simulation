package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.graphics.interfaces.GraphicalObject;
import engine.toolBox.DrawHelper;
import gamePackage.GameController;

import java.awt.*;

    public class LoadingScreen implements GraphicalObject {

                //Attribute
            private boolean loading;
            private boolean notLoaded;

                //Referenzen
            private String mail;
            private Display display;
            private GameController controller;
            private DatabaseConnector connector;

            GUI gui;
            Player player;
            Refresh refresh;
            Questbook questbook;
            GameField gameField;

        public LoadingScreen(DatabaseConnector connector, GameController controller, Display display, String mail) {

            this.mail = mail;
            this.display = display;
            this.connector = connector;
            this.controller = controller;

            this.notLoaded = true;
        }

        private void load() {

            notLoaded = false;
            player = new Player(connector, mail);
            refresh = new Refresh(player);

            questbook = new Questbook(display, connector, player, controller);
            gameField = new GameField(connector, refresh, display, player, controller);
            gui = new GUI(display, connector, gameField, player, controller);

            controller.setGui(gui);
            controller.setQuestbook(questbook);
            controller.setGameField(gameField);

            controller.init();
        }

        @Override
        public void update(double delta) {

            if(loading && notLoaded) {

                load();
            } else {

                loading = true;
            }
        }

        @Override
        public void draw(DrawHelper draw) {

            draw.setStroke(5);
            draw.setFont(new Font("Roboto", Font.BOLD, 80));
            draw.drawString("Empire Simulation",(1000 - draw.getFontWidth("Empire Simulation")) / 2, 150);

            draw.setColour(Color.ORANGE);
            draw.fillRoundRec(200, 775, 625, 100, 25);

            draw.setStroke(10);
            draw.setColour(Color.BLACK);
            draw.drawRoundRec(200, 775, 625, 100, 25);
        }
    }
