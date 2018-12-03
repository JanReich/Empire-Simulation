package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

    public class House extends Building {

                //Attribute
            private boolean hover;

                //Referenzen
            private BufferedImage overlay;

        public House(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.database = "JansEmpire_House";
            loadImage();

            upgradable = generateUpgradeCost(database, false, level);
            overlay = ImageHelper.getImage("res/images/Gui/Overlay.png");
        }

        @Override
        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/House/House_St" + level + ".png");
        }

        @Override
        public void draw(DrawHelper draw) {

            super.draw(draw);

        }

        @Override
        public void mouseMoved(MouseEvent e) {

            super.mouseMoved(e);

        }
    }
