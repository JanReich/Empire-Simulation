package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

    public class Warehouse extends Building {

                //Attribute

                //Referenzen

        public Warehouse(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.type = "Warehouse";
            loadImage();

            upgradable = generateUpgradeCost(type, true, level);
        }



        @Override
        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/Warehouse/Warehouse_St" + level + ".png");
        }
    }
