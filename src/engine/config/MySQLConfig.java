package engine.config;

import engine.toolBox.FileHelper;
import engine.toolBox.MySQL;

import java.io.File;
import java.util.HashMap;

    public class MySQLConfig {

                //Attribute

                //Referenzen
            private String host;
            private String port;
            private String database;
            private String username;
            private String password;

        public MySQLConfig() {

            setStandart();
            read();
        }

        // --------------- MySQL - Configreader --------------- //

        private void read() {

            host = FileHelper.getProperty(getFile(), "host");
            port = FileHelper.getProperty(getFile(), "port");
            database = FileHelper.getProperty(getFile(), "database");
            username = FileHelper.getProperty(getFile(), "username");
            password = FileHelper.getProperty(getFile(), "password");
        }

        private void setStandart() {

            if(!FileHelper.isFileExisting(getFile())) {

                FileHelper.createFile(getFile());

                HashMap<String, String> config = new HashMap<>();

                config.put("host", "mysql.webhosting24.1blu.de");
                config.put("port", "3306");
                config.put("database", "db85565x2810214");
                config.put("username", "s85565_2810214");
                config.put("password", "kkgbeste");

                FileHelper.setProperty(getFile(), config);
            }
        }

        private File getFile() {

            return new File("res/configs/MySQL.properties");
        }

        // --------------- MySQL - GETTER AND SETTER --------------- //


        public String getHost() {

            return host;
        }

        public String getPort() {

            return port;
        }

        public String getDatabase() {

            return database;
        }

        public String getUsername() {

            return username;
        }

        public String getPassword() {

            return password;
        }
    }
