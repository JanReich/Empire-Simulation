package gamePackage.Buildings;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.graphics.Display;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.DrawHelper;
import gamePackage.Game.GameField;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

    public abstract class Building implements LiteInteractableObject {

                //Attribute
            private int movingX;
            private int movingY;

            protected int posX;
            protected int posY;
            protected int level;
            protected int width;
            protected int height;

            protected int worker;
            protected int woodCost;
            protected int coinCost;
            protected int stoneCost;
            protected int wheatCost;

            protected boolean moving;
            protected boolean upgrading;
            protected boolean needWorker;
            protected boolean upgradable;

                //Referenzen
            protected Display display;
            protected GameField gameField;
            protected BuildingOverlay overlay;
            protected DatabaseConnector connector;

            protected String database;
            protected BufferedImage building;

        public Building(DatabaseConnector connector, Display display, GameField gameField, int level, int posX, int posY, int width, int height) {

            this.posX = posX;
            this.posY = posY;
            this.level = level;
            this.width = width;
            this.height = height;

            this.display = display;
            this.connector = connector;
            this.gameField = gameField;
        }

        public abstract void loadImage();

        /**
         * Wenn diese Methode aufgerufen wird, kann man ein Gebäude in
         * Bewegung versetzen
         * Beim verschieben oder beim upgraden(wenn das Gebäude größer wird)
         */
        public void move() {

            gameField.setBuildingMode(true);
            gameField.removeFields(posX, posY, width, height);
            moving = true;
        }

        private void moveBuilding(MouseEvent e, boolean upgrade) {

            if (!gameField.getField(e.getX(), e.getY()).equalsIgnoreCase("error")) {

                String[] field = gameField.getField(e.getX(), e.getY()).split(":");

                if (gameField.checkFields(Integer.parseInt(field[0]), Integer.parseInt(field[1]), width, height, upgrade, woodCost, stoneCost, wheatCost, coinCost, worker)) {

                    connector.executeStatement("" +
                            "UPDATE JansEmpire_Buildings SET Position = '" + field[0] + "-" + field[1] + "' WHERE Mail = '" + gameField.getPlayer().getMail() + "' AND Position = '" + getPosition() + "';");

                    moving = false;
                    upgrading = false;
                    gameField.setBuildingMode(false);
                    posX = Integer.parseInt(field[0]);
                    posY = Integer.parseInt(field[1]);
                    if(upgrade) upgradeBuilding();
                    else undoReplacing(false);
                } else undoReplacing(upgrade);
            } else undoReplacing(upgrade);
        }

        private void undoReplacing(boolean upgrade) {

            if(upgrade) {

                level -= 1;
                loadImage();

                connector.executeStatement("" +
                        "SELECT Size FROM " + database + " WHERE Level = '" + level + "';" );
                QueryResult size = connector.getCurrentQueryResult();
                String[] position = size.getData()[0][0].split("x");
                width = Integer.parseInt(position[0]);
                height = Integer.parseInt(position[1]);
                upgrading = false;
            }
            moving = false;
            gameField.setBuildingMode(false);
            gameField.simulateBuild(posX, posY, width, height);
        }

        public void upgrade() {

            if(gameField.getPlayer().checkGoods(woodCost, stoneCost, wheatCost, coinCost, worker)) {

                level += 1;
                loadImage();

                connector.executeStatement("" +
                        "SELECT Size FROM " + database + " WHERE Level = '" + level + "';" );
                QueryResult size = connector.getCurrentQueryResult();

                //Größe ändert sich nicht
                if(getSize().equals(size.getData()[0][0])) upgradeBuilding();
                else { //Größe ändert sich

                    gameField.setBuildingMode(true);
                    gameField.removeFields(posX, posY, width, height);
                    String[] position = size.getData()[0][0].split("x");
                    width = Integer.parseInt(position[0]);
                    height = Integer.parseInt(position[1]);
                    moving = true;
                    upgrading = true;
                }
            } else removeOverlay();
        }

        private void upgradeBuilding() {

            gameField.simulateBuild(posX, posY, width, height);
            gameField.getPlayer().payGoods(woodCost, stoneCost, wheatCost, coinCost, worker);
            connector.executeStatement("UPDATE JansEmpire_Buildings SET Level = '" + level + "' WHERE Mail = '" + gameField.getPlayer().getMail() + "' AND Position = '" + getPosition() + "';");

            if(worker == 0) {

                upgradable = generateUpgradeCost(database,false, level);

            } else if(worker > 0) {

                upgradable = generateUpgradeCost(database, false, level);
            }

            if(database.equalsIgnoreCase("JansEmpire_House")) {

                connector.executeStatement("SELECT Livingroom FROM " + database + " WHERE Level = '" + level + "' OR Level = '" + (level - 1) + "';");
                gameField.getPlayer().addGoods(0, 0, 0, 0, (Integer.parseInt(connector.getCurrentQueryResult().getData()[1][0]) - Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0])));
            }

            else if(database.equalsIgnoreCase("JansEmpire_Warehouse")) {

                connector.executeStatement("SELECT Storage FROM " + database + " WHERE Level = '" + level + "' OR Level = '" + (level - 1) + "';");
                gameField.getPlayer().addStorage(Integer.parseInt(connector.getCurrentQueryResult().getData()[1][0]) - Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }
        }

        public void removeOverlay() {

            display.getActivePanel().removeObjectFromPanel(overlay);
            overlay = null;
        }

        /**
         * Mit dieser Methode kann herzausgefunden werden, ob ein weiteres Upgrade verfügbar ist.
         * @param level
         * @param needWorker
         * @param database
         * @return
         */
        public boolean generateUpgradeCost(String database, boolean needWorker, int level) {

            this.database = database;
            this.needWorker = needWorker;
            connector.executeStatement("SELECT MAX(Level) FROM " + database);
            QueryResult upgrades = connector.getCurrentQueryResult();

            if(needWorker) {

                if(level < Integer.parseInt(upgrades.getData()[0][0])) {

                    connector.executeStatement("SELECT WoodCost, StoneCost, WheatCost, CoinCost, WorkerAmount FROM " + database + " WHERE Level = '" + (level + 1) + "';");

                    QueryResult result = connector.getCurrentQueryResult();
                    worker = Integer.parseInt(result.getData()[0][4]);
                    coinCost = Integer.parseInt(result.getData()[0][3]);
                    woodCost = Integer.parseInt(result.getData()[0][0]);
                    stoneCost = Integer.parseInt(result.getData()[0][1]);
                    wheatCost = Integer.parseInt(result.getData()[0][2]);
                    return true;
                } else return false;
            } else {

                if(level < Integer.parseInt(upgrades.getData()[0][0])) {

                    connector.executeStatement("SELECT WoodCost, StoneCost, WheatCost, CoinCost FROM " + database + " WHERE Level = '" + (level + 1) + "';");

                    QueryResult result = connector.getCurrentQueryResult();
                    worker = 0;
                    woodCost = Integer.parseInt(result.getData()[0][0]);
                    coinCost = Integer.parseInt(result.getData()[0][3]);
                    stoneCost = Integer.parseInt(result.getData()[0][1]);
                    wheatCost = Integer.parseInt(result.getData()[0][2]);
                    return true;
                } else return false;
            }
        }

        @Override
        public void draw(DrawHelper draw) {

            if(!moving) draw.drawImage(building, gameField.getFieldX() + (posX * gameField.getFieldSquareSize()), gameField.getFieldY() + (posY * gameField.getFieldSquareSize()), width * gameField.getFieldSquareSize(), height * gameField.getFieldSquareSize());
            else draw.drawImage(building, movingX, movingY, width * gameField.getFieldSquareSize(), height * gameField.getFieldSquareSize());
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            if(moving) {

                moveBuilding(e, upgrading);
                removeOverlay();
            }

            else if(e.getX() >= gameField.getFieldX() + (posX * gameField.getFieldSquareSize()) && e.getX() <= gameField.getFieldX() + (posX * gameField.getFieldSquareSize()) + (width * gameField.getFieldSquareSize()) && e.getY() >= gameField.getFieldY() + (posY * gameField.getFieldSquareSize()) && e.getY() <= gameField.getFieldY() + (posY * gameField.getFieldSquareSize()) + (height * gameField.getFieldSquareSize()) && overlay == null && !gameField.getController().getQuestbook().isActive()) {

                display.getActivePanel().removeObjectFromPanel(overlay);
                overlay = null;
                overlay = new BuildingOverlay(this, getPosition(), gameField.getFieldX() + (posX * gameField.getFieldSquareSize()), gameField.getFieldY() + (posY * gameField.getFieldSquareSize()), width * gameField.getFieldSquareSize(), height * gameField.getFieldSquareSize());
                display.getActivePanel().drawObjectOnPanel(overlay, 10);
            } else {

                display.getActivePanel().removeObjectFromPanel(overlay);
                overlay = null;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            if(moving) {

                movingX = e.getX();
                movingY = e.getY();
            }
        }

            //--------------- GETTER AND SETTER ---------------

        public int getWorker() {

            return worker;
        }

        public int getWoodCost() {

            return woodCost;
        }

        public int getCoinCost() {

            return coinCost;
        }

        public int getStoneCost() {

            return stoneCost;
        }

        public int getWheatCost() {

            return wheatCost;
        }

        public boolean isUpgradable() {

            return upgradable;
        }

        public boolean isDestroyable() {

            if(database.equalsIgnoreCase("JansEmpire_Castle")) return false;
            else return true;
        }

        public String getSize() {

            return width + "x" + height;
        }

        public int getPosX() {

            return posX;
        }

        public int getPosY() {

            return posY;
        }

        public int getWidth() {

            return width;
        }

        public int getHeight() {

            return height;
        }

        public GameField getGameField() {

            return gameField;
        }

        public String getPosition() {

            return posX + "-" + posY;
        }

        public String getDatabase() {

            return database;
        }

        public boolean isNeedWorker() {
            return needWorker;
        }

        public boolean isInside(MouseEvent e, int x, int y, int width, int height) {

            if(e.getX() > x && e.getX() < x + width && e.getY() > y && e.getY() < y + height) return true; return false;
        }
    }