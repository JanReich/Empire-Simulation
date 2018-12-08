package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

    public class Woodcutter extends Building {

                //Attribute

                //Referenzen

        public Woodcutter(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.type = "Woodcutter";
            loadImage();

            upgradable = generateUpgradeCost(type, true, level);
        }

        @Override
        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/Woodcutter/Woodcutter_St" + level + ".png");
        }
    }
