package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

    public class GuardHouse extends Building {

                //Attribute

                //Referenzen

        public GuardHouse(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.type = "GuardHouse";
            loadImage();

            upgradable = generateUpgradeCost(type, true, level);
        }

        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/GuardHouse/GuardHouse_St" + level + ".png");
        }
    }
