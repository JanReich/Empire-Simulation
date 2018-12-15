package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.graphics.Display;
import engine.graphics.interfaces.GraphicalObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import engine.toolBox.SpriteSheet;
import gamePackage.Buildings.*;
import gamePackage.GameController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

    public class GameField implements GraphicalObject {

                //Attribute
            private final int fieldX = 150;
            private final int fieldY = 100;
            private final int amountOfFields = 28;
            private final int fieldSquareSize = 25;

            private boolean buildingMode;

                //Referenzen
            private Player player;
            private Display display;
            private Refresh refresh;
            private String[][] fields;
            private SpriteSheet waterTile;
            private GameController controller;
            private DatabaseConnector connector;
            private ArrayList<Building> buildings;

            private BufferedImage boat;
            private BufferedImage steg;

            private PathManagement pathManagement;

        public GameField(DatabaseConnector connector, Refresh refresh, Display display, Player player, GameController controller) {

            this.player = player;
            this.display = display;
            this.refresh = refresh;
            this.connector = connector;
            this.controller = controller;
            buildings = new ArrayList<>();
            fields = new String[amountOfFields][amountOfFields];

            for (int i = 0; i < fields.length; i++) {
                for (int j = 0; j < fields[0].length; j++) {

                    fields[i][j] = "O";
                }
            }

            pathManagement = new PathManagement(display,this);
            loadBuildings();

            boat = ImageHelper.getImage("res/images/Environment/Boat.png");
            steg = ImageHelper.getImage("res/images/Environment/steg.png");
            waterTile = new SpriteSheet(ImageHelper.getImage("res/images/Environment/waterTile.png"), 4, 4, true);
        }

        public boolean isMenuActive() {

            boolean temp = true;

            for(Building building : buildings) {

                if(building.getBuildingOverlay() != null) temp = false;
            }
            return !temp;
        }

        private void loadBuildings() {

            display.getActivePanel().removeObjectFromPanel(pathManagement);
            display.getActivePanel().drawObjectOnPanel(pathManagement, 40);
            connector.executeStatement("" +
                    "SELECT Type, Level, Position FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "';");

            QueryResult result = connector.getCurrentQueryResult();

            for (int i = 0; i < result.getRowCount(); i++) {

                String[] length;
                String[] position;
                QueryResult buildingData;

                    //Switch Type e.g. Woodcutter
                switch (result.getData()[i][0]) {

                    case "Woodcutter":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount, WoodProduction FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Woodcutter woodcutter = new Woodcutter(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(woodcutter, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Warehouse":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Warehouse warehouse = new Warehouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(warehouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Farmhouse":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Farmhouse farmhouse = new Farmhouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(farmhouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Stonemason":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Stonemason stonemason = new Stonemason(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(stonemason, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "House":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        House house = new House(connector, display, this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(house, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Castle":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Castle castle = new Castle(connector,display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(castle, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Kaserne":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Kaserne kaserne = new Kaserne(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(kaserne, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "GuardHouse":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_StaticBuildings WHERE Level = '" + result.getData()[i][1] + "' AND Type = '" + result.getData()[i][0] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        GuardHouse guardHouse = new GuardHouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(guardHouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Path":

                        position = result.getData()[i][2].split("-");
                        pathManagement.addPath(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
                        break;
                }
            }

        }

        public void destroyBuilding(Building building, BuildingOverlay overlay) {

            connector.executeStatement("SELECT Type, Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Position = '" + building.getPosition() + "'; ");
            QueryResult level =  connector.getCurrentQueryResult();

            if(connector.getCurrentQueryResult().getData()[0][0].equalsIgnoreCase("House")) {

                connector.executeStatement("SELECT Livingroom FROM JansEmpire_StaticBuildings WHERE Level ='" + connector.getCurrentQueryResult().getData()[0][1] + "' AND Type = 'House';");
                player.payResources(0, 0, 0, 0, Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            else if(connector.getCurrentQueryResult().getData()[0][0].equalsIgnoreCase("Warehouse")) {

                connector.executeStatement("SELECT StorageAmount FROM JansEmpire_StaticBuildings WHERE Level = '" + connector.getCurrentQueryResult().getData()[0][1] + "' AND Type = 'Warehouse';");
                player.addStorage(- Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            if(building.isNeedWorker()) {

                connector.executeStatement("" +
                        "SELECT WorkerAmount FROM JansEmpire_StaticBuildings WHERE Level = '" + level.getData()[0][1] + "' AND Type = '" + level.getData()[0][0] + "';");

                player.deposit(0, 0, 0, 0, Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            connector.executeStatement("DELETE FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Position = '" + building.getPosition() + "';");

            removeFields(building.getPosX(), building.getPosY(), building.getWidth(), building.getHeight());

            buildings.remove(building);
            display.getActivePanel().removeObjectFromPanel(overlay);
            display.getActivePanel().removeObjectFromPanel(building);
            getController().refreshQuests();
        }

        public void destroyPath(String position, int x, int y) {

            connector.executeStatement("DELETE FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Position = '" + position + "';");
            removeFields(x, y, 1, 1);
        }

        public void removeFields(int startX, int startY, int width, int height) {

            for (int row = startX; row < startX + width; row++) {
                for (int col = startY; col < startY + height; col++) {

                    fields[row][col] = "O";
                }
            }
        }

        public String getField(int x, int y) {

            if(x < fieldX || y < fieldY || x > fieldX + (fieldSquareSize * amountOfFields) || y > fieldY + (fieldSquareSize * amountOfFields)) return "error";

            int tempFieldX = (x - fieldX) / fieldSquareSize;
            int tempFieldY = (y - fieldY) / fieldSquareSize;

            return tempFieldX + ":" + tempFieldY;
        }

        public void setBuildingMode(boolean active) {

            buildingMode = active;
        }

        @Override
        public void update(double delta) {

        }

        public void refresh() {

            boolean temp = refresh.calculateResources();
            if(temp) refresh.synchronizeData();
        }

        @Override
        public void draw(DrawHelper draw) {

            if(buildingMode) {

                for (int row = 0; row < fields.length; row++) {
                    for (int col = 0; col < fields[0].length; col++) {

                        if (fields[row][col].equalsIgnoreCase("O"))
                            draw.setColour(Color.GRAY.brighter());
                        else
                            draw.setColour(Color.RED.brighter());

                        draw.fillRec(fieldX + (row * fieldSquareSize), fieldY + (col * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }

                draw.setColour(Color.BLACK.brighter());
                for (int i = 0; i < amountOfFields; i++)
                    draw.drawLine(fieldX, fieldY + (fieldSquareSize * i), fieldX + (amountOfFields * fieldSquareSize), fieldY + (fieldSquareSize * i));
                    draw.drawLine(fieldX, fieldY + (fieldSquareSize * amountOfFields), fieldX + (fieldSquareSize * amountOfFields), fieldY + (fieldSquareSize * amountOfFields));

                for (int i = 0; i < amountOfFields; i++)
                    draw.drawLine(fieldX + (fieldSquareSize * i), fieldY, fieldX + (fieldSquareSize * i), fieldY + (amountOfFields * fieldSquareSize));
                    draw.drawLine(fieldX + (fieldSquareSize * amountOfFields), fieldY, fieldX + (fieldSquareSize * amountOfFields), fieldY + (fieldSquareSize * amountOfFields));
            } else {

                for (int col = 0; col < fields.length; col++) {
                    for (int row = 0; row < fields[0].length; row++) {

                        draw.drawImage(waterTile.getSubImage(1, 1), fieldX + (col * fieldSquareSize), fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }
            }

                //Bottom
            for (int col = 0; col < fields.length; col++) {
                for (int row = 0; row < 10; row++) {

                    if(row == 0) {

                        draw.drawImage(waterTile.getSubImage(1, 2),fieldX + (col * fieldSquareSize), fieldY + (fields[0].length * fieldSquareSize) + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else if(row == 1) {

                        draw.drawImage(waterTile.getSubImage(1, 3),fieldX + (col * fieldSquareSize), fieldY + (fields[0].length * fieldSquareSize) + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else {

                        draw.drawImage(waterTile.getSubImage(3, 1),fieldX + (col * fieldSquareSize), fieldY + (fields[0].length * fieldSquareSize) + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }
            }

                //Left
            for (int col = 0; col < 10; col++) {
                for (int row = 0; row < fields[0].length + 10; row++) {

                    if(col == 0 && row < fields[0].length) {

                        draw.drawImage(waterTile.getSubImage(2, 1),fieldX + (fields.length * fieldSquareSize) + (col * fieldSquareSize), fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else if(col == 0 && row == fields[0].length) {

                        draw.drawImage(waterTile.getSubImage(2, 2),fieldX + (fields.length * fieldSquareSize) + (col * fieldSquareSize), fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else if(col == 0 && row == fields[0].length + 1) {

                        draw.drawImage(waterTile.getSubImage(2, 3),fieldX + (fields.length * fieldSquareSize) + (col * fieldSquareSize), fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else {

                        draw.drawImage(waterTile.getSubImage(3, 1),fieldX + (fields.length * fieldSquareSize) + (col * fieldSquareSize), fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }
            }

            for(int col = 0; col < 6; col++) {
                for (int row = 0; row < 5; row++) {

                    draw.drawImage(waterTile.getSubImage(3, 1),col * fieldSquareSize, row * fieldSquareSize, fieldSquareSize, fieldSquareSize);
                }
            }

            for (int col = 0; col < fields.length + 10; col++) {
                for (int row = 0; row < 10; row++) {

                    if(row == 0 && col < fields.length) {

                        draw.drawImage(waterTile.getSubImage(1, 0),fieldX + (col * fieldSquareSize), fieldY - (row * fieldSquareSize) - fieldSquareSize, fieldSquareSize, fieldSquareSize);
                    } else if(row == 0 && col == fields.length) {

                        draw.drawImage(waterTile.getSubImage(2, 0),fieldX + (col * fieldSquareSize), fieldY - (row * fieldSquareSize) - fieldSquareSize, fieldSquareSize, fieldSquareSize);
                    }else {

                            draw.drawImage(waterTile.getSubImage(3, 1),fieldX + (col * fieldSquareSize), fieldY - (row * fieldSquareSize) - fieldSquareSize, fieldSquareSize, fieldSquareSize);
                    }
                }
            }

                //Right
            for (int col = 0; col < 10; col++) {
                for (int row = 0; row < fields[0].length + 10; row++) {

                    if(col == 0 && row < fields[0].length) {

                        draw.drawImage(waterTile.getSubImage(0, 1),fieldX - (col * fieldSquareSize) - fieldSquareSize, fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else if(col == 0 && row == fields[0].length) {

                        draw.drawImage(waterTile.getSubImage(0, 2),fieldX - (col * fieldSquareSize) - fieldSquareSize, fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else if(col == 0 && row == fields[0].length + 1) {

                        draw.drawImage(waterTile.getSubImage(0, 3),fieldX - (col * fieldSquareSize) - fieldSquareSize, fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    } else {

                        draw.drawImage(waterTile.getSubImage(3, 1),fieldX - (col * fieldSquareSize) - fieldSquareSize, fieldY + (row * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }
            }

            draw.drawImage(boat, 75, 905);
            draw.drawImage(steg, 295, 805);
        }

        public void simulateBuild(int fromX, int fromY, int width, int height) {

            for(int x = fromX; x < width + fromX; x++)
                for (int y = fromY; y < height + fromY; y++)
                    fields[x][y] = "X";
        }

        public boolean checkFields(int fromX, int fromY, int width, int height, boolean pay, int woodCost, int stoneCost, int wheatCost, int coinCost, int worker) {

            boolean canBuild = true;
            for(int x = fromX; x < fromX + width; x++) {
                for(int y = fromY; y < fromY + height; y++) {

                    if(fields.length > x && fields[0].length > y) {
                        if (fields[x][y].equalsIgnoreCase("X")) {

                            canBuild = false;
                            break;
                        }
                    } else canBuild = false;
                }
            }

            if(pay) if(!(player.checkGoods(woodCost, stoneCost, wheatCost, coinCost, worker))) canBuild = false;

            return canBuild;
        }

        public void build(Building building, int fromX, int fromY, int width, int height) {

            buildings.add(building);

            for(int x = fromX; x < width + fromX; x++)
                for (int y = fromY; y < height + fromY; y++)
                    fields[x][y] = "X";

            display.getActivePanel().drawObjectOnPanel(building, 3);
        }

        public void build(Building building, int fromX, int fromY, int width, int height, int woodCost, int stoneCost, int wheatCost, int coinCost, int worker) {

            player.payResources(woodCost, stoneCost, wheatCost, coinCost, worker);
            this.build(building, fromX, fromY, width, height);
        }

            // --------------- GETTER AND SETTER --------------- \\

        public int getFieldX() {

            return fieldX;
        }

        public int getFieldY() {

            return fieldY;
        }

        public Player getPlayer() {

            return player;
        }

        public int getFieldSquareSize() {

            return fieldSquareSize;
        }

        public GameController getController() {

            return controller;
        }

        public int getAmountOfFields() {

            return amountOfFields;
        }

        public PathManagement getPathManagement() {
            return pathManagement;
        }
    }
