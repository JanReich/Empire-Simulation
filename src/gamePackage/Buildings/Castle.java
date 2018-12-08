package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

    public class Castle extends Building {

                //Attribute

                //Referenzen
            private DatabaseConnector connector;

        public Castle(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.type = "Castle";
            loadImage();

            upgradable = generateUpgradeCost(type, false, level);
        }

        @Override
        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/Castle/Castle_St" + level + ".png");
        }
    }
