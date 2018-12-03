package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;

    public class Player {

                //Attribute
            private int wood;
            private int stone;
            private int wheat;
            private int coins;
            private int worker;

            private int xp;
            private int level;
            private int storageAmount;

            private int woodPerHour;
            private int stonePerHour;

            private int neededFood;
            private int wheatPerHour;
            private int wheatProduction;

                //Referenzen
            private DatabaseConnector connector;

            private final String mail;
            private final String username;

        public Player(DatabaseConnector connector, String mail) {

            this.connector = connector;

            this.mail = mail;

            connector.executeStatement("" +
                    "SELECT Level, XP, StorageAmount, Wood, Stone, Wheat, Coins, Population FROM JansEmpire_PlayerData WHERE Mail = '" + mail + "';");

            QueryResult result = connector.getCurrentQueryResult();
            level = Integer.parseInt(result.getData()[0][0]);
            xp = Integer.parseInt(result.getData()[0][1]);
            storageAmount = Integer.parseInt(result.getData()[0][2]);
            wood = Integer.parseInt(result.getData()[0][3]);
            stone = Integer.parseInt(result.getData()[0][4]);
            wheat = Integer.parseInt(result.getData()[0][5]);
            coins = Integer.parseInt(result.getData()[0][6]);
            worker = Integer.parseInt(result.getData()[0][7]);

            connector.executeStatement("SELECT Username FROM JansEmpire_Users WHERE Mail = '" + mail + "';");
            username = connector.getCurrentQueryResult().getData()[0][0];
        }

        public void setStorage(int storage) {

            storageAmount = storage;

            connector.executeStatement("" +
                    "UPDATE JansEmpire_PlayerData SET " +
                    "StorageAmount = '" + storageAmount + "'" +
                    "WHERE Mail = '" + getMail() + "'");
        }

        public void addStorage(int storage) {

            storageAmount += storage;

            connector.executeStatement("" +
                    "UPDATE JansEmpire_PlayerData SET " +
                    "StorageAmount = '" + storageAmount + "' " +
                    "WHERE Mail = '" + getMail() + "'");
        }

        public void updateClientData() {

            connector.executeStatement("" +
                    "SELECT Level, XP, StorageAmount, Wood, Stone, Wheat, Coins, Population FROM JansEmpire_PlayerData WHERE Mail = '" + mail + "';");

            QueryResult result = connector.getCurrentQueryResult();
            level = Integer.parseInt(result.getData()[0][0]);
            xp = Integer.parseInt(result.getData()[0][1]);
            storageAmount = Integer.parseInt(result.getData()[0][2]);
            wood = Integer.parseInt(result.getData()[0][3]);
            stone = Integer.parseInt(result.getData()[0][4]);
            wheat = Integer.parseInt(result.getData()[0][5]);
            coins = Integer.parseInt(result.getData()[0][6]);
            worker = Integer.parseInt(result.getData()[0][7]);
        }

        public void updateMySQLData() {

            connector.executeStatement("" +
                    "UPDATE JansEmpire_PlayerData SET " +
                    "XP = '" + this.xp + "'," +
                    "Level = '" + this.level + "'," +
                    "Wood = '" + this.wood +  "'," +
                    "Stone = '" + this.stone + "'," +
                    "Wheat = '" + this.wheat + "'," +
                    "Coins = '" + this.coins + "'," +
                    "Population = '" + this.worker + "'," +
                    "StorageAmount = '" + this.storageAmount + "'" +
                    "WHERE Mail = '" + mail + "';");
        }

        public void updateData(int level, int xp, int storageAmount, int wood, int stone, int wheat, int coins, int worker) {

            this.wood = wood;
            this.stone = stone;
            this.wheat = wheat;
            this.coins = coins;
            this.worker = worker;

            this.xp = xp;
            this.level = level;
            this.storageAmount = storageAmount;
        }

        public boolean checkGoods(int wood, int stone, int wheat, int coins, int worker) {

            if(wood <= this.wood && stone <= this.stone && wheat <= this.wheat && coins <= this.coins && worker <= this.worker) return true;
            return false;
        }

        public boolean payGoods(int wood, int stone, int wheat, int coins, int worker) {

            if(checkGoods(wood, stone, wheat, coins, worker)) {

                    this.wood -= wood;
                    this.stone -= stone;
                    this.wheat -= wheat;
                    this.coins -= coins;
                    this.worker -= worker;

                        //Update in mySQL
                    connector.executeStatement("" +
                            "UPDATE JansEmpire_PlayerData SET " +
                            "Wood = '" + this.wood +  "'," +
                            "Stone = '" + this.stone + "'," +
                            "Wheat = '" + this.wheat + "'," +
                            "Coins = '" + this.coins + "'," +
                            "Population = '" + this.worker + "'" +
                            "WHERE Mail = '" + mail + "';");

                return true;
            } else return false;
        }

        public int getNextLevelXP() {

            connector.executeStatement("SELECT MAX(Level) FROM JansEmpire_Level");
            int maxLevel = Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]);

            if(maxLevel > level) {

                connector.executeStatement("SELECT xp FROM JansEmpire_Level WHERE Level = '" + (level + 1) + "'");
                return Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]);
            } else return -1;
        }

        public void addGoods(int wood, int stone, int wheat, int coins, int worker) {

            this.wood += wood;
            if(this.wood > storageAmount) this.wood = storageAmount;

            this.stone += stone;
            if(this.stone > storageAmount) this.stone = storageAmount;

            this.wheat += wheat;
            if(this.wheat > storageAmount) this.wheat = storageAmount;

            this.coins += coins;
            this.worker += worker;

            connector.executeStatement("" +
                    "UPDATE JansEmpire_PlayerData SET " +
                    "Wood = '" + this.wood +  "'," +
                    "Stone = '" + this.stone + "'," +
                    "Wheat = '" + this.wheat + "'," +
                    "Coins = '" + this.coins + "'," +
                    "Population = '" + this.worker + "'" +
                    "WHERE Mail = '" + mail + "';");
        }

        public void addXP(int xp) {

            this.xp += xp;

            connector.executeStatement("" +
                            "UPDATE JansEmpire_PlayerData SET XP = '" + this.xp + "' WHERE Mail = '" + mail +"';");

            connector.executeStatement("SELECT MAX(Level) FROM JansEmpire_Level");
            int maxLevel = Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]);


            if(level == maxLevel) {


            } else if(level < maxLevel) {

                connector.executeStatement("SELECT xp FROM JansEmpire_Level WHERE Level = '" + (level + 1) + "'");

                if(this.xp >= Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0])) {

                    level += 1;
                    connector.executeStatement("" +
                            "UPDATE JansEmpire_PlayerData SET Level = '" + level + "' WHERE Mail = '" + mail +"';");
                }
            }
        }

        public int getCurrentProgress() {

            connector.executeStatement("SELECT xp FROM JansEmpire_Level WHERE Level = '" + (level) + "'");
            return Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]);
        }


            //GETTER AND SETTER

        public String getMail() {

            return mail;
        }

        public int getLevel() {

            return level;
        }

        public String getUsername() {

            return username;
        }

        public int getCoins() {

            return coins;
        }

        public int getWood() {

            return wood;
        }

        public int getStone() {

            return stone;
        }

        public int getWheat() {

            return wheat;
        }

        public int getXp() {

            return xp;
        }

        public int getPopulation() {

            return worker;
        }

        public int getStorageAmount() {

            return storageAmount;
        }

        public int getWoodPerHour() {
            return woodPerHour;
        }

        public void setWoodPerHour(int woodPerHour) {
            this.woodPerHour = woodPerHour;
        }

        public int getStonePerHour() {
            return stonePerHour;
        }

        public void setStonePerHour(int stonePerHour) {
            this.stonePerHour = stonePerHour;
        }

        public int getWheatPerHour() {
            return wheatPerHour;
        }

        public int getNeededFood() {
            return neededFood;
        }

        public void setNeededFood(int neededFood) {
            this.neededFood = neededFood;
        }

        public int getFoodProduction() {
            return wheatProduction;
        }

        public void setWheatProduction(int wheatProduction) {
            this.wheatProduction = wheatProduction;
        }

        public void setWheatPerHour(int wheatPerHour) {
            this.wheatPerHour = wheatPerHour;
        }
    }
