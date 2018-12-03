package engine.graphics;

import engine.config.DisplayConfig;
import engine.graphics.interfaces.*;
import engine.toolBox.DrawHelper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

    public class DrawingPanel extends Panel implements KeyListener, MouseListener, MouseMotionListener {

            //Attribute
        private int fps;
        private int frames;
        private double delta;

        private long lastLoop;
        private long firstFrame;
        private long elapsedTime;
        private long currentFrame;

        private boolean requested;

            //Referenzen
        private Timer time;
        private DisplayConfig config;
        private DrawHelper drawHelper;

            // Integer = zIndex && BasicInterface == Object
        private LinkedHashMap<GraphicalObject, Integer> graphicalObjects;
        private ArrayList<ManagementObject> managementObjects;

        public DrawingPanel(DisplayConfig config) {

            this.config = config;
            managementObjects = new ArrayList<>();
            this.graphicalObjects = new LinkedHashMap<>();

            setDoubleBuffered(true);

            time = new Timer(10, this);
            time.start();
        }

        public void addManagement(ManagementObject obj) {

            managementObjects.add(obj);
        }

        public void paintComponent(Graphics g) {

            if(!requested) {

                addKeyListener(this);
                addMouseListener(this);
                addMouseMotionListener(this);

                requestFocusInWindow();
                requested = !requested;
            }

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //ANTIALIASING einschalten
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Graphics setzen oder updaten
            if(!(drawHelper != null)) drawHelper = new DrawHelper(g2d);
            else drawHelper.updateGraphics(g2d);

            drawHelper.setScreenWidth(getWidth());
            drawHelper.setScreenHeight(getHeight());

            //Updating Frame
            if(graphicalObjects != null) {

                for(Map.Entry<GraphicalObject, Integer> entrySet : graphicalObjects.entrySet()) {

                    if(entrySet.getValue() != null) {

                        entrySet.getKey().draw(drawHelper);
                        entrySet.getKey().update(delta / 1000);
                    } else System.err.println("[Error] Es wird versucht ein Objekt == null zuzeichnen");
                }

                graphicalObjects = sortHashMap(graphicalObjects);

                for(ManagementObject obj : managementObjects) {

                    obj.update(delta / 1000);
                }
            }

            //FPS-Settings
            frames++;
            currentFrame = System.currentTimeMillis();
            if(currentFrame > firstFrame + 1000) {

                firstFrame = currentFrame;
                fps = frames;
                frames = 0;
            }

            if(config.isShowFPS()) {

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("", Font.BOLD, 24));
                g2d.drawString("FPS: " + fps, 5, 24);
            }
        }

        public boolean contains(Object o) {

            if(graphicalObjects.containsKey(o)) return true; return false;
        }


        @Override
        public void actionPerformed(ActionEvent e) {

            elapsedTime = System.nanoTime() - lastLoop;
            lastLoop = System.nanoTime();
            delta = (int) ((elapsedTime / 1000000L) + 0.5);
            if ( delta == 0 ) delta = 1;
            repaint();
        }

        public boolean containsObject(GraphicalObject object) {

            if(graphicalObjects.containsValue(object)) return true; return false;
        }

        public void drawObjectOnPanel(GraphicalObject object) {

            drawObjectOnPanel(object, 1);
        }

        public void drawObjectOnPanel(GraphicalObject object, int zIndex) {

            SwingUtilities.invokeLater(() -> graphicalObjects.put(object, zIndex));
            graphicalObjects = sortHashMap(graphicalObjects);
        }

        public void removeObjectFromPanel(Object object) {

            if(object != null) {

                if(graphicalObjects.containsKey(object)) {

                    if(graphicalObjects.containsKey(object)) SwingUtilities.invokeLater(() -> graphicalObjects.remove(object));
                    graphicalObjects = sortHashMap(graphicalObjects);
                } else if(managementObjects.contains(object)) {

                    SwingUtilities.invokeLater(() -> managementObjects.remove(object));
                }
            }
        }

        public void removeAllObjectsFromPanel() {

            if(graphicalObjects != null && managementObjects != null) {

                graphicalObjects.clear();
                managementObjects.clear();
            }
        }

        public int getZIndex(GraphicalObject object) {

            return graphicalObjects.get(object);
        }

        public void setZIndex(GraphicalObject object, int index) {

            if(graphicalObjects.containsKey(object)) {

                graphicalObjects.put(object, index);
                graphicalObjects = sortHashMap(graphicalObjects);
            }
        }

        public LinkedHashMap<GraphicalObject, Integer> sortHashMap(Map<GraphicalObject, Integer> unsorted) {

            Object[] objects = unsorted.entrySet().toArray();
            Arrays.sort(objects, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<GraphicalObject, Integer>) o1).getValue().compareTo(((Map.Entry<GraphicalObject, Integer>) o2).getValue());
                }
            });

            LinkedHashMap<GraphicalObject, Integer> map = new LinkedHashMap<>();
            for (Object e : objects) {

                map.put(((Map.Entry<GraphicalObject, Integer>) e).getKey(), ((Map.Entry<GraphicalObject, Integer>) e).getValue());
            }
            return map;
        }


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof BasicInteractableObject) {

                    ((BasicInteractableObject) obj).keyPressed(e);
                }
            }

            for(ManagementObject entry : managementObjects) {

                entry.keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof BasicInteractableObject) {

                    ((BasicInteractableObject) obj).keyReleased(e);
                }
            }

            for(ManagementObject entry : managementObjects) {

                entry.keyReleased(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof BasicInteractableObject) {

                    ((BasicInteractableObject) obj).mouseReleased(e);
                }

                if(obj instanceof LiteInteractableObject) {

                    ((LiteInteractableObject) obj).mouseReleased(e);
                }
            }

            for(ManagementObject entry : managementObjects) {

                entry.mouseReleased(e);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof AdvancedInteractableObject) {

                    ((AdvancedInteractableObject) obj).mouseDragged(e);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof AdvancedInteractableObject) {

                    ((AdvancedInteractableObject) obj).mouseMoved(e);
                }

                if(obj instanceof LiteInteractableObject) {

                    ((LiteInteractableObject) obj).mouseMoved(e);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

            for(Map.Entry<GraphicalObject, Integer> entry : graphicalObjects.entrySet()) {

                GraphicalObject obj = entry.getKey();
                if(obj instanceof AdvancedInteractableObject) {

                    ((AdvancedInteractableObject) obj).mousePressed(e);
                }
            }
        }
    }