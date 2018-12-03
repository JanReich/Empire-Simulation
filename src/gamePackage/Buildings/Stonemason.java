package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

    public class Stonemason extends Building{

                //Attribute

                //Referenzen

        public Stonemason(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            super(connector, display, gameField, level, posX, posY, width, height);

            this.connector = connector;
            this.database = "JansEmpire_Stonemason";
            loadImage();

            upgradable = generateUpgradeCost(database, true, level);
        }

        @Override
        public void loadImage() {

            building = ImageHelper.getImage("res/images/Buildings/Stonemason/Stonemason_St" + level + ".png");
        }
    }
