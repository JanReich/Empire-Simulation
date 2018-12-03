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
            private SpriteSheet grassTile;
            private GameController controller;
            private DatabaseConnector connector;
            private ArrayList<Building> buildings;

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

            loadBuildings();

            grassTile = new SpriteSheet(ImageHelper.getImage("res/images/Environment/grassTile.png"), 4, 4, true);
        }

        private void loadBuildings() {

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
                                "SELECT Size, WorkerAmount, WoodProduction FROM JansEmpire_WoodcutterHouseing WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Woodcutter woodcutter = new Woodcutter(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(woodcutter, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Warehouse":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_Warehouse WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Warehouse warehouse = new Warehouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(warehouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Farmhouse":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_Farmhouse WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Farmhouse farmhouse = new Farmhouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(farmhouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Stonemason":

                        connector.executeStatement("" +
                                "SELECT Size, WorkerAmount FROM JansEmpire_Stonemason WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Stonemason stonemason = new Stonemason(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(stonemason, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "House":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_House WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        House house = new House(connector, display, this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(house, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Castle":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_Castle WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Castle castle = new Castle(connector,display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(castle, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "Kaserne":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_Kaserne WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        Kaserne kaserne = new Kaserne(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(kaserne, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                    case "GuardHouse":

                        connector.executeStatement("" +
                                "SELECT Size FROM JansEmpire_GuardHouse WHERE Level = '" + result.getData()[i][1] + "';");

                        buildingData = connector.getCurrentQueryResult();
                        position = result.getData()[i][2].split("-");
                        length = buildingData.getData()[0][0].split("x");

                        GuardHouse guardHouse = new GuardHouse(connector, display,this, Integer.parseInt(result.getData()[i][1]), Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        build(guardHouse, Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                        break;
                }
            }

        }

        public void destroyBuilding(Building building, BuildingOverlay overlay) {

            connector.executeStatement("SELECT Type, Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Position = '" + building.getPosition() + "'; ");
            QueryResult level =  connector.getCurrentQueryResult();

            if(connector.getCurrentQueryResult().getData()[0][0].equalsIgnoreCase("House")) {

                connector.executeStatement("SELECT Livingroom FROM JansEmpire_House WHERE Level ='" + connector.getCurrentQueryResult().getData()[0][1] + "';");
                player.payGoods(0, 0, 0, 0, Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            else if(connector.getCurrentQueryResult().getData()[0][0].equalsIgnoreCase("Warehouse")) {

                connector.executeStatement("SELECT Storage FROM JansEmpire_Warehouse WHERE Level = '" + connector.getCurrentQueryResult().getData()[0][1] + "';");
                player.addStorage(- Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            if(building.isNeedWorker()) {

                connector.executeStatement("" +
                        "SELECT WorkerAmount FROM " + building.getDatabase() + " WHERE Level = '" + level.getData()[0][1] + "';");

                player.addGoods(0, 0, 0, 0, Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]));
            }

            connector.executeStatement("DELETE FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Position = '" + building.getPosition() + "';");

            removeFields(building.getPosX(), building.getPosY(), building.getWidth(), building.getHeight());

            buildings.remove(building);
            display.getActivePanel().removeObjectFromPanel(overlay);
            display.getActivePanel().removeObjectFromPanel(building);
            getController().refreshQuests();
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

                for (int row = 0; row < fields.length; row++) {
                    for (int col = 0; col < fields[0].length; col++) {

                        draw.drawImage(grassTile.getSubImage(row % 4, col % 4), fieldX + (row * fieldSquareSize), fieldY + (col * fieldSquareSize), fieldSquareSize, fieldSquareSize);
                    }
                }

                draw.setColour(Color.GRAY.brighter());
                draw.drawRec(fieldX, fieldY, amountOfFields * fieldSquareSize, amountOfFields * fieldSquareSize);
            }
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

            player.payGoods(woodCost, stoneCost, wheatCost, coinCost, worker);
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
    }
