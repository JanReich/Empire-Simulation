package gamePackage.LoginSystem;

import engine.graphics.Display;
import engine.graphics.interfaces.AdvancedInteractableObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.Textfield;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

    public class FormLayout implements AdvancedInteractableObject {

                    //Attribute
                private int x;
                private int y;

                private boolean login;
                private boolean hoverRegister;

                private String errorMessage;
                private boolean loginUsernameError;
                private boolean loginPasswordError;
                private String loginResult;
                private boolean loginError;
                private boolean registerError;
                private String registerErrorMessage;

                    //Referenzen
                private LoginManager manager;

                private Color lighterGray = new Color(225, 225, 225);
                private Color skyBlue = new Color(58, 95, 205).brighter();
                private Color lightGray = Color.darkGray.brighter().brighter().brighter();

                    //Textfield
                private Textfield loginUsername;
                private Textfield loginPassword;

                private Textfield registerMail;
                private Textfield registerUsername;
                private Textfield registerPassword;
                private Textfield registerPassword2;

        public FormLayout(Display display, LoginManager manager) {

                this.x = 230;
                this.y = 250;
                login = true;
                this.manager = manager;

                loginUsername = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 50, y + 155, 460, 50);
                display.getActivePanel().drawObjectOnPanel(loginUsername);
                loginPassword = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 50, y + 255, 460, 50);
                display.getActivePanel().drawObjectOnPanel(loginPassword);

                registerMail = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 130, y + 455, 400, 50);
                display.getActivePanel().drawObjectOnPanel(registerMail);
                registerUsername = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 130, y + 155, 400, 50);
                display.getActivePanel().drawObjectOnPanel(registerUsername);
                registerPassword = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 130, y + 255, 400, 50);
                display.getActivePanel().drawObjectOnPanel(registerPassword);
                registerPassword2 = new Textfield(display, new Font("Roboto", Font.PLAIN, 20), x + 130, y + 355, 400, 50);
                display.getActivePanel().drawObjectOnPanel(registerPassword2);
            }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {


            if(login) {

                draw.setColour(Color.WHITE);
                draw.fillRoundRec(x, y, 600, 460, 30);
                draw.setColour(lighterGray);
                draw.drawRoundRec(x, y, 600, 460, 30);

                    //Heading
                draw.setColour(skyBlue);
                draw.setFont(new Font("Roboto", Font.BOLD, 25));
                draw.drawString("ACCOUNT LOGIN", x + 50, y + 85);

                    //Email-Field
                draw.setColour(Color.GRAY);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("USERNAME", x + 50, y + 145);
                draw.setColour(new Color(225, 225, 225));
                draw.fillRoundRec(x + 50, y + 155, 460, 50, 9);

                if(loginUsernameError) {

                    draw.setColour(Color.RED.brighter());
                    draw.drawString(errorMessage, x + 300, y + 145);
                }
                else draw.setColour(lightGray);
                draw.drawRoundRec(x + 50, y + 155, 460, 50, 9);

                if(loginError) {

                    draw.setColour(Color.RED.brighter());
                    draw.drawString(loginResult, x + 240, y + 145);
                }

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));
                draw.drawString(loginUsername.getInput().getInputQuerry() + "", x + 60, y + 193);

                    //Password-Field
                draw.setColour(Color.GRAY);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("PASSWORD", x + 50, y + 245);
                draw.setColour(new Color(225, 225, 225));
                draw.fillRoundRec(x + 50, y + 255, 460, 50, 9);

                if(loginPasswordError) {

                    draw.setColour(Color.RED.brighter());
                    draw.drawString(errorMessage, x + 300, y + 245);
                }
                else draw.setColour(lightGray);
                draw.drawRoundRec(x + 50, y + 255, 460, 50, 9);

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));

                String password = "";
                for (int i = 0; i < loginPassword.getInput().getInputQuerry().length(); i++) {

                    password += "*";
                }
                draw.drawString(password, x + 60, y + 293);

                    //Forgot-Password
                draw.setColour(skyBlue);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("Passwort vergessen?", x + 368, y + 340);

                    //Login-Button
                draw.setColour(skyBlue);
                draw.fillRoundRec(x + 50, y + 360,460, 50, 9);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x + 50, y + 360,460, 50, 9);
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.BOLD, 25));
                draw.drawString("LOG IN", x + 240, y + 395);

                if(!hoverRegister) {

                    draw.setColour(skyBlue);
                    draw.fillRec(x + 560, y, 25, 460);
                    draw.fillRoundRec(x + 570, y, 30, 460, 30);

                    draw.setColour(lightGray);
                    draw.fillRoundRec(x + 572, y + 65, 2, 35, 5);
                    draw.fillRoundRec(x + 585, y + 65, 2, 35, 5);
                } else {

                    draw.setColour(skyBlue);
                    draw.fillRec(x + 545, y, 40, 460);
                    draw.fillRoundRec(x + 570, y, 30, 460, 30);
                }
            } else {

                draw.setColour(skyBlue);
                draw.fillRoundRec(x, y, 600, 610, 30);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x, y, 600, 610, 30);

                draw.setColour(Color.DARK_GRAY);
                draw.fillRec(x + 20, y, 50, 610);
                draw.fillRoundRec(x, y, 30, 610, 30);

                    //Heading
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.BOLD, 25));
                draw.drawString("REGISTER ACCOUNT", x + 130, y + 85);

                draw.setColour(Color.WHITE);
                draw.fillOval(x + 480, y + 50, 50);
                draw.setColour(skyBlue);
                draw.setStroke(2);
                draw.drawLine(x + 495, y + 65, x + 515, y + 85);
                draw.drawLine(x + 495, y + 85, x + 515, y + 65);

                    //Username
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("USERNAME", x + 130, y + 145);
                draw.setColour(new Color(72, 118,255));
                draw.fillRoundRec(x + 130, y + 155, 400, 50, 9);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x + 130, y + 155, 400, 50, 9);

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));
                draw.drawString(registerUsername.getInput().getInputQuerry() + "", x + 140, y + 193);

                if(registerError) {

                    draw.setColour(Color.RED.brighter());
                    draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                    draw.drawString(registerErrorMessage, x + 300, y + 145);
                }

                    //Password
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("PASSWORD", x + 130, y + 245);
                draw.setColour(new Color(72, 118,255));
                draw.fillRoundRec(x + 130, y + 255, 400, 50, 9);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x + 130, y + 255, 400, 50, 9);

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));

                String password = "";
                for (int i = 0; i < registerPassword.getInput().getInputQuerry().length(); i++) {

                    password += "*";
                }
                draw.drawString(password, x + 140, y + 293);

                    //Confirm Password
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("CONFIRM PASSWORD", x + 130, y + 345);
                draw.setColour(new Color(72, 118,255));
                draw.fillRoundRec(x + 130, y + 355, 400, 50, 9);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x + 130, y + 355, 400, 50, 9);


                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));

                String password2 = "";
                for (int i = 0; i < registerPassword2.getInput().getInputQuerry().length(); i++) {

                    password2 += "*";
                }
                draw.drawString(password2, x + 140, y + 393);

                    //email
                draw.setColour(Color.WHITE);
                draw.setFont(new Font("Roboto", Font.PLAIN, 15));
                draw.drawString("EMAIL ADDRESS", x + 130, y + 445);
                draw.setColour(new Color(72, 118,255));
                draw.fillRoundRec(x + 130, y + 455, 400, 50, 9);
                draw.setColour(skyBlue.darker());
                draw.drawRoundRec(x + 130, y + 455, 400, 50, 9);

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 20));
                draw.drawString(registerMail.getInput().getInputQuerry() + "", x + 140, y + 493);

                draw.setColour(Color.WHITE);
                draw.fillRoundRec(x + 130, y + 530,400, 50, 9);
                draw.setColour(lighterGray);
                draw.drawRoundRec(x + 130, y + 530,400, 50, 9);
                draw.setColour(skyBlue);
                draw.setFont(new Font("Roboto", Font.BOLD, 25));
                draw.drawString("REGISTER", x + 270, y + 565);
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {

            if(loginUsername.getInput().getInputQuerry() != "") {

                errorMessage = "";
                loginUsernameError = false;
            }

            if(loginPassword.getInput().getInputQuerry() != "") {

                errorMessage = "";
                loginPasswordError = false;
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

                if(e.getX() > x + 480 && e.getX() < x + 530 && e.getY() > y + 50 && e.getY() < y + 100 && !login) {

                    login = true;
                    loginError = false;
                    loginPasswordError = false;
                    loginUsernameError = false;
                    loginUsername.getInput().setInputQuerry("");
                    loginPassword.getInput().setInputQuerry("");
                } else if(e.getX() > x + 545 && e.getX() < x + 600 && e.getY() > y && e.getY() < y + 600 && login) {

                    login = false;
                    registerError = false;
                    registerMail.getInput().setInputQuerry("");
                    registerUsername.getInput().setInputQuerry("");
                    registerPassword.getInput().setInputQuerry("");
                    registerPassword2.getInput().setInputQuerry("");
                }


                if(login) {

                    if(e.getX() > loginUsername.getX() && e.getX() < loginUsername.getX() + loginUsername.getWidth() && e.getY() > loginUsername.getY() && e.getY() < loginUsername.getY() + loginUsername.getHeight()) {

                        loginPassword.getInput().setTyping(false);
                        loginUsername.getInput().setTyping(true);
                    } else if(e.getX() > loginPassword.getX() && e.getX() < loginPassword.getX() + loginPassword.getWidth() && e.getY() > loginPassword.getY() && e.getY() < loginPassword.getY() + loginPassword.getHeight()) {

                        loginUsername.getInput().setTyping(false);
                        loginPassword.getInput().setTyping(true);
                    } else {

                        loginPassword.getInput().setTyping(false);
                        loginUsername.getInput().setTyping(false);
                    }
                } else {

                    if(e.getX() > registerUsername.getX() && e.getX() < registerUsername.getX() + registerUsername.getWidth() && e.getY() > registerUsername.getY() && e.getY() < registerUsername.getY() + registerUsername.getHeight()) {

                        registerUsername.getInput().setTyping(true);
                        registerPassword.getInput().setTyping(false);
                        registerPassword2.getInput().setTyping(false);
                        registerMail.getInput().setTyping(false);
                    } else if(e.getX() > registerPassword.getX() && e.getX() < registerPassword.getX() + registerPassword.getWidth() && e.getY() > registerPassword.getY() && e.getY() < registerPassword.getY() + registerPassword.getHeight()) {

                        registerUsername.getInput().setTyping(false);
                        registerPassword.getInput().setTyping(true);
                        registerPassword2.getInput().setTyping(false);
                        registerMail.getInput().setTyping(false);
                    } else if(e.getX() > registerPassword2.getX() && e.getX() < registerPassword2.getX() + registerPassword2.getWidth() && e.getY() > registerPassword2.getY() && e.getY() < registerPassword2.getY() + registerPassword2.getHeight()) {

                        registerUsername.getInput().setTyping(false);
                        registerPassword.getInput().setTyping(false);
                        registerPassword2.getInput().setTyping(true);
                        registerMail.getInput().setTyping(false);
                    } else if(e.getX() > registerMail.getX() && e.getX() < registerMail.getX() + registerMail.getWidth() && e.getY() > registerMail.getY() && e.getY() < registerMail.getY() + registerMail.getHeight()) {

                        registerUsername.getInput().setTyping(false);
                        registerPassword.getInput().setTyping(false);
                        registerPassword2.getInput().setTyping(false);
                        registerMail.getInput().setTyping(true);
                    } else {

                        registerMail.getInput().setTyping(false);
                        registerUsername.getInput().setTyping(false);
                        registerPassword.getInput().setTyping(false);
                        registerPassword2.getInput().setTyping(false);
                    }
                }

                //Login
            if(e.getX() > x + 50 && e.getX() < x + 510 && e.getY() > y + 360 && e.getY() < y + 410 && login) {

                if(loginUsername.getInput().getInputQuerry() == "") {

                    loginUsernameError = true;
                    errorMessage = "Das Feld darf nicht leer bleiben!";
                } else if(loginPassword.getInput().getInputQuerry() == "") {

                    loginPasswordError = true;
                    errorMessage = "Das Feld darf nicht leer bleiben!";
                } else {

                    String result = this.manager.login(loginUsername.getInput().getInputQuerry(), loginPassword.getInput().getInputQuerry());

                    if(result == "failed") {

                        loginError = true;
                        loginResult = "Die Anmeldedaten stimmen nicht überein!";
                    }
                }
            } else if(e.getX() > x + 130 && e.getX() < x + 530 && e.getY() > y + 530 && e.getY() < y + 580 && !login) {

                if(registerPassword.getInput().getInputQuerry().equals(registerPassword2.getInput().getInputQuerry())) {

                    String result = this.manager.register(registerUsername.getInput().getInputQuerry(), registerPassword.getInput().getInputQuerry(), registerMail.getInput().getInputQuerry());

                    if(result == "failed") {

                        registerError = true;
                        registerErrorMessage = "Die Registierung ist fehlgeschlagen!";
                    } else if(result.equals("successful")) {

                        loginUsername.getInput().setInputQuerry("");
                        login = true;
                    }
                } else {

                    registerError = true;
                    registerErrorMessage = "Die Passwörter stimmen nicht überein!";
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            if(e.getX() > x + 560 && e.getX() < x + 600 && e.getY() > y && e.getY() < y + 600) {

                hoverRegister = true;
            } else if(e.getX() > x + 545 && e.getX() < x + 600 && e.getY() > y && e.getY() < y + 600) {

                hoverRegister = true;
            } else {

                hoverRegister = false;
            }
        }

        @Override
        public void mouseDragged(MouseEvent event) {

        }

        @Override
        public void mousePressed(MouseEvent event) {

        }
    }
