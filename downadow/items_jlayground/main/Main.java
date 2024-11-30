package downadow.items_jlayground.main;

import downadow.items_jlayground.blocks.Blocks;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;

public class Main extends JPanel {
    /* ширина и высота карты в объектах */
    private static final int WIDTH = 250, HEIGHT = 60;
    /* карта */
    private static char[] map = new char[WIDTH * HEIGHT];
    
    private static boolean[] forBoom = new boolean[WIDTH * HEIGHT];
    
    private static boolean ph = true, following = false;
    
    private static Color bgColor = new Color(80, 80, 80);
    
    private static int behaviorSelected2 = -1;
    
    private static boolean jump = false, blockHelicopter = false;
    
    private static boolean programmingMode = false;
    
    private static String behavior = "", cmsg = "";
    private static int behaviorSelected = 0;
    
    private static boolean ui = true;
    /* блокировка управления */
    private static boolean block = false;
    /* замедленное время */
    private static boolean slow = false;
    
    private static boolean noWater = false;
    
    private static boolean select = false, selectz = false, step = false;
    
    private static boolean fill = false;
    
    private static int cameraStart = 115 + 50 * WIDTH;
    /* выбранный блок */
    private static int selected = -1;
    /* вывести помощь */
    private static boolean help = true;
    
    private static char currentBlock = '.';
    
    private static int rain = -1;
    
    private static String helpMessage = "";
    
    private static JFrame fr;
    
    /* 0   одиночная игра
     * 1   сервер
     * 2   клиент
     */
    private static int gameState = 0;
    
    /* могут ли клиенты выполнять команды? */
    private static boolean serverAllowCreators = false;
    
    private static String message = "";
    
    private static int adminPos = 0;
    
    /* возвращает адрес блока, на который указывает прицел */
    private static int selectedBlockAddr() {
        return cameraStart + 10 + 6 * WIDTH;
    }
    
    private static long startTime = 0;
    
    /* отправить сообщение */
    private static void sendMessage(String msg) {
        if(!cmsg.isEmpty())
            cmsg += ";;;";
        cmsg += msg.replace("$$", "" + selectedBlockAddr());
    }
    
    private static void setBlock(int addr, char blk) {
        map[addr] = blk;
        if(gameState == 2) sendMessage("/c " + addr + " " + blk);
    }
    
    private static void fire(int addr) {
        if(gameState != 2) {
            final int saved = addr;
            if(!Blocks.isFireResistant(map[saved])) {
                map[saved] = 'f';

                new Thread() {
                    public void run() {
                        try {
                            for(int i = 0; i < 6; i++) {
                                Thread.sleep(5000);
                                if(map[saved] != 'f')
                                    return;
                                if(!Blocks.isFireResistant(map[saved - 1]))
                                    fire(saved - 1);
                                if(!Blocks.isFireResistant(map[saved + 1]))
                                    fire(saved + 1);
                                if(!Blocks.isFireResistant(map[saved + WIDTH]))
                                    fire(saved + WIDTH);
                                if(!Blocks.isFireResistant(map[saved - WIDTH]))
                                    fire(saved - WIDTH);
                            }
                            if(slow)
                                Thread.sleep(15000);
                            if(map[saved] == 'f');
                                map[saved] = new java.util.Random().nextInt(3) == 1 ? 'b' : '.';
                        } catch(Exception e) {}
                    }
                }.start();
            }
        } else {
            sendMessage("/f " + addr);
        }
    }
    
    private static void fire2(int addr) {
        if(gameState != 2) {
            final int saved = addr;
            map[saved] = 'F';

            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(!slow ? 500 : 1200);
                        if(map[saved - 1] != '.' && map[saved - 1] != 'F' && !Blocks.isWater(map[saved - 1]))
                            fire2(saved - 1);
                        if(map[saved + 1] != '.' && map[saved + 1] != 'F' && !Blocks.isWater(map[saved + 1]))
                            fire2(saved + 1);
                        if(map[saved + WIDTH] != '.' && map[saved + WIDTH] != 'F' && !Blocks.isWater(map[saved + WIDTH]))
                            fire2(saved + WIDTH);
                        if(map[saved - WIDTH] != '.' && map[saved - WIDTH] != 'F' && !Blocks.isWater(map[saved - WIDTH]))
                            fire2(saved - WIDTH);
                        if(map[saved - WIDTH - 1] != '.' && map[saved - WIDTH - 1] != 'F' && !Blocks.isWater(map[saved - WIDTH - 1]))
                            fire2(saved - WIDTH - 1);
                        if(map[saved - WIDTH + 1] != '.' && map[saved - WIDTH + 1] != 'F' && !Blocks.isWater(map[saved - WIDTH + 1]))
                            fire2(saved - WIDTH + 1);
                        if(map[saved + WIDTH - 1] != '.' && map[saved + WIDTH - 1] != 'F' && !Blocks.isWater(map[saved + WIDTH - 1]))
                            fire2(saved + WIDTH - 1);
                        if(map[saved + WIDTH + 1] != '.' && map[saved + WIDTH + 1] != 'F' && !Blocks.isWater(map[saved + WIDTH + 1]))
                            fire2(saved + WIDTH + 1);
                        
                        Thread.sleep(!slow ? 7000 : 15000);
                        map[saved] = '.';
                    } catch(Exception e) {}
                }
            }.start();
        } else {
            sendMessage("/F " + addr);
        }
    }
    
    private static void boom(int addr) {
        final int saved = addr;
        new Thread() {
            public void run() {
                try {
                    try {
                        if(!Blocks.isStrong(map[saved]))         map[saved] = '.';
                        if(!Blocks.isStrong(map[saved + 1]))     map[saved + 1] = '.';
                        if(!Blocks.isStrong(map[saved - 1]))     map[saved - 1] = '.';
                        if(!Blocks.isStrong(map[saved + WIDTH])) map[saved + WIDTH] = '.';
                        if(!Blocks.isStrong(map[saved - WIDTH])) map[saved - WIDTH] = '.';
                        
                        if(!(map[saved - 1 - WIDTH] >= '0' && map[saved - 1 - WIDTH] <= '9') && !Blocks.isStrong(map[saved -1 - WIDTH]) && map[saved - 1 - WIDTH] != '.') {
                            if(!Blocks.isStrong(map[saved - 2 - WIDTH * 2]))
                                map[saved - 2 - WIDTH * 2] = map[saved - 1 - WIDTH];
                            map[saved - 1 - WIDTH] = '.';
                        }
                        
                        if(!(map[saved + 1 - WIDTH] >= '0' && map[saved + 1 - WIDTH] <= '9') && !Blocks.isStrong(map[saved + 1 - WIDTH]) && map[saved + 1 - WIDTH] != '.') {
                            if(!Blocks.isStrong(map[saved + 2 - WIDTH * 2]))
                                map[saved + 2 - WIDTH * 2] = map[saved + 1 - WIDTH];
                            map[saved + 1 - WIDTH] = '.';
                        }
                        
                        if(!(map[saved + 1 + WIDTH] >= '0' && map[saved + 1 + WIDTH] <= '9') && !Blocks.isStrong(map[saved + 1 + WIDTH]) && map[saved + 1 + WIDTH] != '.') {
                            if(!Blocks.isStrong(map[saved + 2 + WIDTH * 2]))
                                map[saved + 2 + WIDTH * 2] = map[saved + 1 + WIDTH];
                            map[saved + 1 + WIDTH] = '.';
                        }
                        
                        if(!(map[saved - 1 + WIDTH] >= '0' && map[saved - 1 + WIDTH] <= '9') && !Blocks.isStrong(map[saved - 1 + WIDTH]) && map[saved - 1 + WIDTH] != '.') {
                            if(!Blocks.isStrong(map[saved - 2 + WIDTH * 2]))
                                map[saved - 2 + WIDTH * 2] = map[saved - 1 + WIDTH];
                            map[saved - 1 + WIDTH] = '.';
                        }
                        
                        if(gameState != 2) {
                            fire(saved - 2);
                            fire(saved + 2);
                            fire(saved + WIDTH * 2);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    for(int i = 0; i < 7; i++) {
                        if(!Blocks.isStrong(map[saved]))
                            map[saved] = (char)((int)'0' + i);
                        if(!slow)
                            Thread.sleep(30);
                        else
                            Thread.sleep(110);
                    }
                    if(!Blocks.isStrong(map[saved]))
                        map[saved] = '.';
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        
        if(gameState == 2) sendMessage("/b " + addr);
    }
    
    private static void shootRight(int addr) {
        if(gameState != 2) {
            new Thread() {
                public void run() {
                    try {
                        while(!ph) {
                            Thread.sleep(15);
                            continue;
                        }
                        int i;
                        int ii = 0;
                        for(i = addr; map[i + 1] == '.' && ii < 45; i++, ii++) {
                            map[i + 1] = map[i];
                            map[i] = '.';
                            Thread.sleep(!slow ? 25 : 60);
                        }
                        if(map[i + 1] != '.')
                            map[i] = '.';
                        if(map[i + 1] != 'f' && map[i + 1] != 'F' && !Blocks.isStrong(map[i + 1]))
                            map[i + 1] = '.';
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            int i;
            for(i = addr + 1; map[i] == '.'; i++);
            setBlock(i, '.');
            setBlock(addr, '.');
        }
    }
    private static void shootLeft(int addr) {
        if(gameState != 2) {
            new Thread() {
                public void run() {
                    try {
                        while(!ph) {
                            Thread.sleep(15);
                            continue;
                        }
                        int i;
                        int ii = 0;
                        for(i = addr; map[i - 1] == '.' && ii < 45; i--, ii++) {
                            map[i - 1] = map[i];
                            map[i] = '.';
                            Thread.sleep(!slow ? 25 : 60);
                        }
                        if(map[i - 1] != '.')
                            map[i] = '.';
                        if(map[i - 1] != 'f' && map[i - 1] != 'F' && !Blocks.isStrong(map[i - 1]))
                            map[i - 1] = '.';
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            int i;
            for(i = addr - 1; map[i] == '.'; i--);
            setBlock(i, '.');
            setBlock(addr, '.');
        }
    }
    
    
    /**********
     *  main  *
     **********/
    
    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("--server")) {
            gameState = 1;
            serverAllowCreators = true;
        } else if(args.length > 0 && args[0].equals("--server-no-creators")) {
            gameState = 1;
            serverAllowCreators = false;
        } else if(args.length > 0 && args[0].equals("--client")) {
            gameState = 2;
        }
        
        try {
            /* загрузка карты, поведения, мода и текста помощи */
            
            Scanner sc = new Scanner(new File("current/map"));
            behavior = sc.nextLine();
            String bgColorNew = sc.nextLine();
            bgColor = new Color(Integer.parseInt(bgColorNew.split(" ")[0]), Integer.parseInt(bgColorNew.split(" ")[1]), Integer.parseInt(bgColorNew.split(" ")[2]));
            
            int ii = 0;
            while(sc.hasNextLine()) {
                char[] line = sc.nextLine().toCharArray();
                for(int i = 0; i < WIDTH; i++)
                    map[ii++] = line[i];
            }
            sc.close();
            
            sc = new Scanner(new File("current/help"));
            while(sc.hasNextLine())
                helpMessage += sc.nextLine() + "\n";
            sc.close();
            
            sc = new Scanner(new File("current/desc"));
            while(sc.hasNextLine()) {
                String[] tokens = sc.nextLine().replace("  ", " ").replace("\t", "").split(" ");
                
                if(tokens[0].startsWith("#") || tokens[0].isEmpty())
                    continue;
                else if(tokens[0].equals("type:simple")) {
                    char c = '\0';
                    char leftC = c;
                    char rightC = c;
                    Image texture = null;
                    boolean isFallen = false;
                    boolean isStrong = false;
                    boolean isSticky = false;
                    boolean isFireResistant = false;
                    boolean isEraser = false;
                    int x = Blocks.defaultX;
                    int y = Blocks.defaultY;
                    int w = Blocks.defaultW;
                    int h = Blocks.defaultH;
                    
                    for(int i = 1; i < tokens.length; i++) {
                        if(tokens[i].startsWith("c:")) {
                            c = tokens[i].toCharArray()[2];
                            if(tokens[i].length() > 3) {
                                leftC = tokens[i].toCharArray()[3];
                                rightC = tokens[i].toCharArray()[4];
                            } else {
                                leftC = c;
                                rightC = c;
                            }
                        } else if(tokens[i].startsWith("texture:")) {
                            texture = new ImageIcon("current/images/" + tokens[i].split(":")[1]).getImage();
                        } else if(tokens[i].equals("fallen")) {
                            isFallen = true;
                        } else if(tokens[i].equals("strong")) {
                            isStrong = true;
                        } else if(tokens[i].equals("sticky")) {
                            isSticky = true;
                        } else if(tokens[i].equals("fire_resistant")) {
                            isFireResistant = true;
                        } else if(tokens[i].equals("eraser")) {
                            isEraser = true;
                        } else if(tokens[i].startsWith("x:")) {
                            x = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("y:")) {
                            y = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("w:")) {
                            w = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("h:")) {
                            h = Integer.parseInt(tokens[i].split(":")[1]);
                        }
                    }
                    
                    Blocks.addSimple(c, leftC, rightC, texture, isFallen, isStrong, isSticky,
                        isFireResistant, isEraser, x, y, w, h);
                } else if(tokens[0].equals("type:tank") || tokens[0].equals("type:helicopter")) {
                    char c = '\0';
                    char leftC = c;
                    char rightC = c;
                    Image texture = null;
                    boolean isStrong = false;
                    char c2 = c;
                    int x = Blocks.defaultX;
                    int y = Blocks.defaultY;
                    int w = Blocks.defaultW;
                    int h = Blocks.defaultH;
                    
                    for(int i = 1; i < tokens.length; i++) {
                        if(tokens[i].startsWith("c:")) {
                            c = tokens[i].toCharArray()[2];
                            if(tokens[i].length() > 3) {
                                leftC = tokens[i].toCharArray()[3];
                                rightC = tokens[i].toCharArray()[4];
                            } else {
                                leftC = c;
                                rightC = c;
                            }
                            c2 = c;
                        } else if(tokens[i].startsWith("c2:")) {
                            c2 = tokens[i].toCharArray()[3];
                        } else if(tokens[i].startsWith("texture:")) {
                            texture = new ImageIcon("current/images/" + tokens[i].split(":")[1]).getImage();
                        } else if(tokens[i].equals("strong")) {
                            isStrong = true;
                        } else if(tokens[i].startsWith("x:")) {
                            x = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("y:")) {
                            y = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("w:")) {
                            w = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("h:")) {
                            h = Integer.parseInt(tokens[i].split(":")[1]);
                        }
                    }
                    
                    if(tokens[0].equals("type:tank"))
                        Blocks.addTank(c, leftC, rightC, texture, c2, isStrong, x, y, w, h);
                    else
                        Blocks.addHelicopter(c, leftC, rightC, texture, c2, isStrong, x, y, w, h);
                } else if(tokens[0].equals("type:water")) {
                    char c = '\0';
                    Image texture = null;
                    
                    for(int i = 1; i < tokens.length; i++) {
                        if(tokens[i].startsWith("c:")) {
                            c = tokens[i].toCharArray()[2];
                        } else if(tokens[i].startsWith("texture:")) {
                            texture = new ImageIcon("current/images/" + tokens[i].split(":")[1]).getImage();
                        }
                    }
                    
                    Blocks.addWaterType(c, texture);
                } else if(tokens[0].equals("type:arrow")) {
                    char c = '\0', leftC = '\0', rightC = '\0';
                    Image texture = null;
                    boolean isFallen = false;
                    boolean isStrong = false;
                    boolean isFireResistant = false;
                    int x = Blocks.defaultX, y = Blocks.defaultY, w = Blocks.defaultW, h = Blocks.defaultH;
                    
                    for(int i = 1; i < tokens.length; i++) {
                        if(tokens[i].startsWith("c:")) {
                            c = tokens[i].toCharArray()[2];
                            if(tokens[i].length() > 3) {
                                leftC = tokens[i].toCharArray()[3];
                                rightC = tokens[i].toCharArray()[4];
                            } else {
                                leftC = c;
                                rightC = c;
                            }
                        } else if(tokens[i].startsWith("texture:")) {
                            texture = new ImageIcon("current/images/" + tokens[i].split(":")[1]).getImage();
                        } else if(tokens[i].equals("fallen")) {
                            isFallen = true;
                        } else if(tokens[i].equals("strong")) {
                            isStrong = true;
                        } else if(tokens[i].equals("fire_resistant")) {
                            isFireResistant = true;
                        } else if(tokens[i].startsWith("x:")) {
                            x = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("y:")) {
                            y = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("w:")) {
                            w = Integer.parseInt(tokens[i].split(":")[1]);
                        } else if(tokens[i].startsWith("h:")) {
                            h = Integer.parseInt(tokens[i].split(":")[1]);
                        }
                    }
                    
                    Blocks.addArrow(c, leftC, rightC, texture, isFallen, isStrong,
                        isFireResistant, x, y, w, h);
                } else {
                    throw new Exception("unknown type `" + tokens[0] + "`");
                }
            }
            sc.close();
            
            /******************/
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        fr = new JFrame("Items Jlayground");
        fr.setSize(1024, 728);
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setLayout(null);
        Main p = new Main();
        p.setLayout(null);
        p.setBounds(0, 0, 1024, 728);
        fr.add(p);
        fr.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                try {
                    /* режим программирования поведения */
                    if(programmingMode) {
                        if(select && e.getKeyChar() != ' ' && e.getKeyChar() != ':' && e.getKeyChar() != (char)65535) {
                            behavior += "set:" + e.getKeyChar() + " ";
                            select = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        } else if(selectz && e.getKeyCode() == KeyEvent.VK_W) {
                            behavior += "wait:up ";
                            selectz = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        } else if(selectz && e.getKeyCode() == KeyEvent.VK_S) {
                            behavior += "wait:down ";
                            selectz = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        } else if(selectz && e.getKeyCode() == KeyEvent.VK_A) {
                            behavior += "wait:left ";
                            selectz = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        } else if(selectz && e.getKeyCode() == KeyEvent.VK_D) {
                            behavior += "wait:right ";
                            selectz = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        }
                        
                        else if(step) {
                            behavior += "step:" + selectedBlockAddr() + ":";
                            if(e.getKeyChar() == '0')
                                behavior += "50";
                            else if(e.getKeyChar() == '9')
                                behavior += "1000";
                            else
                                behavior += "" + (((int)e.getKeyChar() - (int)'0') * 100);
                            behavior += " ";
                            behaviorSelected2 = selectedBlockAddr();
                            step = false;
                            if(gameState == 2) sendMessage("/p " + behavior);
                            return;
                        }
                        
                        if(e.getKeyCode() == KeyEvent.VK_HOME) {
                            behavior = "";
                            behaviorSelected2 = -1;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'W') {
                            behavior += "up:copy ";
                            behaviorSelected2 -= WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'S') {
                            behavior += "down:copy ";
                            behaviorSelected2 += WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'A') {
                            behavior += "left:copy ";
                            behaviorSelected2--;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'D') {
                            behavior += "right:copy ";
                            behaviorSelected2++;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_W) {
                            behavior += "up ";
                            behaviorSelected2 -= WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_S) {
                            behavior += "down ";
                            behaviorSelected2 += WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_A) {
                            behavior += "left ";
                            behaviorSelected2--;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_D) {
                            behavior += "right ";
                            behaviorSelected2++;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_I) {
                            behavior += "up:lift ";
                            behaviorSelected2 -= WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_K) {
                            behavior += "down:lift ";
                            behaviorSelected2 += WIDTH;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_J) {
                            behavior += "left:lift ";
                            behaviorSelected2--;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_L) {
                            behavior += "right:lift ";
                            behaviorSelected2++;
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'f') {
                            behavior += "fire ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_F) {
                            behavior += "fire2 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_T) {
                            behavior += "tp:" + selectedBlockAddr() + " ";
                            behaviorSelected2 = selectedBlockAddr();
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_B) {
                            behavior += "boom ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_E) {
                            step = true;
                        } else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                            behavior += "sel:" + selectedBlockAddr() + " ";
                            behaviorSelected2 = selectedBlockAddr();
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                            select = true;
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_UP)
                            cameraStart -= WIDTH;
                        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                            cameraStart += WIDTH;
                        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                            cameraStart++;
                        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                            cameraStart--;
                        else if(e.getKeyCode() == KeyEvent.VK_F5)
                            programmingMode = false;
                        
                        else if(e.getKeyCode() == KeyEvent.VK_0) {
                            behavior += "~50 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_1) {
                            behavior += "~100 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_2) {
                            behavior += "~200 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_3) {
                            behavior += "~300 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_4) {
                            behavior += "~400 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_5) {
                            behavior += "~500 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_6) {
                            behavior += "~600 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_7) {
                            behavior += "~700 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_8) {
                            behavior += "~800 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_9) {
                            behavior += "~1000 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                            behavior += "~5000 ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'Z') {
                            behavior += "wait ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyChar() == 'z') {
                            selectz = true;
                        } else if(e.getKeyChar() == (char)24 /* Ctrl+X */) {
                            behavior += "no_sel ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                            String[] behavior2 = behavior.split(" ");
                            behavior = "";
                            for(int i = 0; i < behavior2.length - 1; i++)
                                behavior += behavior2[i] + " ";
                            if(gameState == 2) sendMessage("/p " + behavior);
                        } else if(e.getKeyCode() == KeyEvent.VK_INSERT) {
                            JFrame sb_fr = new JFrame("изменить поведение");
                            sb_fr.setAlwaysOnTop(true);
                            sb_fr.setSize(560, 140);
                            sb_fr.setResizable(false);
                            sb_fr.setLocationRelativeTo(null);
                            JPanel sb_p = new JPanel();
                            sb_p.setBounds(0, 0, 560, 140);
                            sb_fr.setLayout(null);
                            JTextField sb_tf = new JTextField(48);
                            sb_tf.setText(behavior);
                            JButton sb_b = new JButton("OK");
        
                            sb_tf.addKeyListener(new KeyListener() {
                                public void keyPressed(KeyEvent e) {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        behavior = sb_tf.getText();
                                        if(gameState == 2) sendMessage("/p " + behavior);
                                        sb_fr.setVisible(false);
                                    }
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
        
                            sb_b.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    behavior = sb_tf.getText();
                                    if(gameState == 2) sendMessage("/p " + behavior);
                                    sb_fr.setVisible(false);
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
        
                            sb_p.add(sb_tf);
                            sb_p.add(sb_b);
                            sb_fr.add(sb_p);
                            sb_fr.setVisible(true);
                        }
                        /* показать/скрыть помощь */
                        else if(e.getKeyCode() == KeyEvent.VK_F1 && !help && ui)
                            help = true;
                        else if(e.getKeyCode() == KeyEvent.VK_F1 && help && ui)
                            help = false;
                        
                        return;
                    }
                    if(select && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        JFrame select_fr = new JFrame("выбор блока (введите число символа в кодировке)");
                        select_fr.setAlwaysOnTop(true);
                        select_fr.setSize(300, 100);
                        select_fr.setResizable(false);
                        select_fr.setLocationRelativeTo(null);
                        JPanel select_p = new JPanel();
                        select_p.setBounds(0, 0, 300, 100);
                        select_fr.setLayout(null);
                        JTextField select_tf = new JTextField(16);
                        JButton select_b = new JButton("Готово");
    
                        select_tf.addKeyListener(new KeyListener() {
                            public void keyPressed(KeyEvent e) {
                                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                    currentBlock = (char)Integer.parseInt(select_tf.getText());
                                    select = false;
                                    select_fr.setVisible(false);
                                }
                            }
                            public void keyTyped(KeyEvent e) {}
                            public void keyReleased(KeyEvent e) {}
                        });
                        select_b.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent e) {
                                currentBlock = (char)Integer.parseInt(select_tf.getText());
                                select = false;
                                select_fr.setVisible(false);
                            }
                        });
    
                        select_p.add(select_tf);
                        select_p.add(select_b);
                        select_fr.add(select_p);
                        select_fr.setVisible(true);
                        return;
                    }
                    if(select && e.getKeyChar() != 65535) {
                        currentBlock = e.getKeyChar();
                        select = false;
                        return;
                    }
                    
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - 1] == '.') {
                        for(int i = selectedBlockAddr() - 1; map[i] == '.'; i--)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_RIGHT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + 1] == '.') {
                        for(int i = selectedBlockAddr() + 1; map[i] == '.'; i++)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + WIDTH] == '.') {
                        for(int i = selectedBlockAddr() + WIDTH; map[i] == '.'; i += WIDTH)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_UP && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - WIDTH] == '.') {
                        for(int i = selectedBlockAddr() - WIDTH; map[i] == '.'; i -= WIDTH)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT && fill && map[selectedBlockAddr()] != '.') {
                        for(int i = selectedBlockAddr() - 1; map[i] != '.'; i--)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_RIGHT && fill && map[selectedBlockAddr()] != '.') {
                        for(int i = selectedBlockAddr() + 1; map[i] != '.'; i++)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN && fill && map[selectedBlockAddr()] != '.') {
                        for(int i = selectedBlockAddr() + WIDTH; map[i] != '.'; i += WIDTH)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_UP && fill && map[selectedBlockAddr()] != '.') {
                        for(int i = selectedBlockAddr() - WIDTH; map[i] != '.'; i -= WIDTH)
                            setBlock(i, map[selectedBlockAddr()]);
                        fill = false;
                        return;
                    }
                    /* "прыжок" */
                    else if(e.getKeyCode() == KeyEvent.VK_SPACE && selected != -1 && following) {
                        new Thread() {
                            public void run() {
                                try {
                                    jump = true;
                                    
                                    if(map[selected + WIDTH] != '.') {
                                        for(int i = 0; i < 3; i++) {
                                            if(map[selected - WIDTH] == '.') {
                                                selected -= WIDTH;
                                                map[selected] = map[selected + WIDTH];
                                                map[selected + WIDTH] = '.';
                                            }
                                            Thread.sleep(150);
                                        }
                                    }
                                    Thread.sleep(160);
                                    
                                    jump = false;
                                } catch(Exception e) {}
                            }
                        }.start();
                        
                        return;
                    }
                    /* включить/выключить режим изменения поведения */
                    else if(e.getKeyCode() == KeyEvent.VK_F5 && !programmingMode) {
                        programmingMode = true;
                        return;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_F5 && programmingMode) {
                        programmingMode = false;
                        return;
                    }
                    /* включение/выключение "физики" */
                    else if(e.getKeyCode() == KeyEvent.VK_F6 && !ph && gameState != 2) {
                        ph = true;
                        return;
                    } else if(e.getKeyCode() == KeyEvent.VK_F6 && ph && gameState != 2) {
                        ph = false;
                        return;
                    }
                    /* включение/выключение следования за объектом */
                    else if(e.getKeyCode() == KeyEvent.VK_F7 && !following) {
                        following = true;
                        return;
                    }else if(e.getKeyCode() == KeyEvent.VK_F7 && following) {
                        following = false;
                        return;
                    }
                    
                    if(!block) {
                        if(e.getKeyCode() == KeyEvent.VK_F4 && !fill)
                            fill = true;
                        else if(e.getKeyCode() == KeyEvent.VK_F4 && fill)
                            fill = false;
                        /* перемещение камеры */
                        else if(e.getKeyCode() == KeyEvent.VK_UP)
                            cameraStart -= WIDTH;
                        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                            cameraStart += WIDTH;
                        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                            cameraStart++;
                        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                            cameraStart--;
                        /* показать/скрыть помощь */
                        else if(e.getKeyCode() == KeyEvent.VK_F1 && !help && ui)
                            help = true;
                        else if(e.getKeyCode() == KeyEvent.VK_F1 && help && ui)
                            help = false;
                        /* включить/выключить замедление времени */
                        else if(e.getKeyCode() == KeyEvent.VK_F2 && !slow)
                            slow = true;
                        else if(e.getKeyCode() == KeyEvent.VK_F2 && slow)
                            slow = false;
                        /*******************************/
                        else if(e.getKeyCode() == KeyEvent.VK_F9 && gameState != 2) {
                            for(int i = map.length - WIDTH - 1; i >= 0; i--) {
                                setBlock(i + WIDTH, map[i]);
                                setBlock(i, '.');
                            }
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_F10)
                            rain = (rain == -1 ? 0 : -1);
                        /*******************************/
                        
                        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && gameState == 2 && ui) {
                            ui = false;
                            help = false;
                            
                            JFrame chat_fr = new JFrame("написать сообщение");
                            chat_fr.setAlwaysOnTop(true);
                            chat_fr.setSize(320, 140);
                            chat_fr.setResizable(false);
                            chat_fr.setLocationRelativeTo(null);
                            JPanel chat_p = new JPanel();
                            chat_p.setBounds(0, 0, 320, 140);
                            chat_fr.setLayout(null);
                            JTextField chat_tf = new JTextField(25);
                            JButton chat_b = new JButton("Отправить");
        
                            chat_tf.addKeyListener(new KeyListener() {
                                public void keyPressed(KeyEvent e) {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        try {
                                            sendMessage(chat_tf.getText());
                                            ui = true;
                                            chat_fr.setVisible(false);
                                        } catch(Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
                            chat_b.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                    try {
                                        sendMessage(chat_tf.getText());
                                        ui = true;
                                        chat_fr.setVisible(false);
                                    } catch(Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
        
                            chat_p.add(chat_tf);
                            chat_p.add(chat_b);
                            chat_fr.add(chat_p);
                            chat_fr.setVisible(true);
                        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && ui) {
                            // сохранение карты
                            
                            if(gameState == 0) {
                                Files.deleteIfExists(Paths.get("current/map"));
                                FileWriter fw = new FileWriter("current/map");
                                fw.write(behavior + "\n");
                                fw.write("" + bgColor.getRed() + " " + bgColor.getGreen() + " " + bgColor.getBlue() + "\n");
                                int iii = 0;
                                for(int i = 0; i < HEIGHT; i++) {
                                    for(int ii = 0; ii < WIDTH; ii++) {
                                        fw.write(map[iii]);
                                        iii++;
                                    }
                                    fw.write("\n");
                                }
                                fw.close();
                            }
                            
                            //////////////////////
                            
                            help = false;
                            ui = false;
                        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !ui)
                            ui = true;
                        /* удаление блока */
                        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                            setBlock(selectedBlockAddr(), '.');
                        else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                            setBlock(selected, '.');
                        /* поставить огонь */
                        } else if(e.getKeyChar() == 'f' && map[selectedBlockAddr()] != '.' &&
                                  !Blocks.isFireResistant(map[selectedBlockAddr()])) {
                            fire(selectedBlockAddr());
                        } else if(e.getKeyChar() == 'F' && map[selectedBlockAddr()] != '.')
                            fire2(selectedBlockAddr());
                        /* выбрать блок под прицелом */
                        else if(e.getKeyCode() == KeyEvent.VK_SPACE && selected == -1 && map[selectedBlockAddr()] != '.')
                            selected = selectedBlockAddr();
                        /* убрать выделение */
                        else if(e.getKeyCode() == KeyEvent.VK_SPACE && selected != -1) {
                            if(gameState == 2) setBlock(selected, map[selected]);
                            selected = -1;
                        } /* выбор блока */
                        else if(e.getKeyCode() == KeyEvent.VK_X)
                            select = true;
                        /* бросать блок */
                        else if(e.getKeyCode() == KeyEvent.VK_F11 && selected == -1)
                            shootLeft(selectedBlockAddr());
                        else if(e.getKeyCode() == KeyEvent.VK_F12 && selected == -1)
                            shootRight(selectedBlockAddr());
                        /* отражение объектов */
                        else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == '~')
                            setBlock(selected, ',');
                        else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == ',')
                            setBlock(selected, '~');
                        else if(e.getKeyCode() == KeyEvent.VK_Q)
                            setBlock(selected, Blocks.getLeftC(map[selected]));
                        else if(e.getKeyCode() == KeyEvent.VK_E)
                            setBlock(selected, Blocks.getRightC(map[selected]));
                        /* поставить блок */
                        else if(e.getKeyCode() == KeyEvent.VK_C)
                            setBlock(selectedBlockAddr(), currentBlock);
                        /* взрыв */
                        else if(e.getKeyCode() == KeyEvent.VK_ENTER && selected == -1) {
                            new Thread() {
                                public void run() {
                                    for(int i = 0; i < forBoom.length; i++) {
                                        if(forBoom[i]) {
                                            forBoom[i] = false;
                                            boom(i);
                                            try {Thread.sleep(50);} catch(Exception ex) {}
                                        }
                                    }
                                }
                            }.start();
                        } else if(e.getKeyCode() == KeyEvent.VK_INSERT) {
                            forBoom[selectedBlockAddr()] = true;
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                            for(int i = 0; i < forBoom.length; i++)
                                forBoom[i] = false;
                        }
                        /* активация объектов */
                        
                        else if(e.getKeyCode() == KeyEvent.VK_ENTER && Blocks.isTank(map[selected]) && Blocks.getRightC(map[selected]) == map[selected]) {
                            for(int i = 1; i < 16; i++) {
                                if((map[selected + i - WIDTH] != '.' && !(map[selected + i - WIDTH] >= '0' && map[selected + i - WIDTH] <= '9')) || (map[selected + i] != '.' && !(map[selected + i] >= '0' && map[selected + i] <= '9'))) {
                                    map[selected] = Blocks.getC2(map[selected]);
                                    boom(selected + i - WIDTH);
                                    
                                    new Thread() {
                                        public void run() {
                                            try {Thread.sleep(25);} catch(Exception ex) {}
                                            map[selected] = Blocks.getC2(map[selected]);
                                        }
                                    }.start();
                                    
                                    break;
                                }
                            }
                        } else if(e.getKeyCode() == KeyEvent.VK_ENTER && Blocks.isTank(map[selected]) && Blocks.getLeftC(map[selected]) == map[selected]) {
                            for(int i = 1; i < 16; i++) {
                                if((map[selected - i - WIDTH] != '.' && !(map[selected - i - WIDTH] >= '0' && map[selected - i - WIDTH] <= '9')) || (map[selected - i] != '.' && !(map[selected - i] >= '0' && map[selected - i] <= '9'))) {
                                    map[selected] = Blocks.getC2(map[selected]);
                                    boom(selected - i - WIDTH);
                                    
                                    new Thread() {
                                        public void run() {
                                            try {Thread.sleep(25);} catch(Exception ex) {}
                                            map[selected] = Blocks.getC2(map[selected]);
                                        }
                                    }.start();
                                    
                                    break;
                                }
                            }
                        } else if(e.getKeyCode() == KeyEvent.VK_ENTER && Blocks.isHelicopter(map[selected]) && map[selected + WIDTH] == '.' && !blockHelicopter) {
                            new Thread() {
                                public void run() {
                                    blockHelicopter = true;
                                    try {
                                        int i;
                                        for(i = selected + WIDTH * 2; map[i] == '.'; i += WIDTH) {
                                            map[i] = 'b';
                                            Thread.sleep(slow ? 90 : 40);
                                            map[i] = '.';
                                        }
                                        boom(i);
                                    } catch(Exception e) {}
                                    blockHelicopter = false;
                                }
                            }.start();
                        }
                        /* операции с выделенным блоком */
                        else if(e.getKeyCode() == KeyEvent.VK_W && selected != -1 && (map[selected - WIDTH] == '.' || Blocks.isWater(map[selected - WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            map[selected - WIDTH] = map[selected];
                            map[selected] = '.';
                            selected -= WIDTH;
                        } else if(e.getKeyCode() == KeyEvent.VK_S && selected != -1 && (map[selected + WIDTH] == '.' || Blocks.isWater(map[selected + WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            map[selected + WIDTH] = map[selected];
                            map[selected] = '.';
                            selected += WIDTH;
                        } else if(e.getKeyCode() == KeyEvent.VK_A && selected != -1 && (map[selected - 1] == '.' || Blocks.isWater(map[selected - 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            map[selected - 1] = map[selected];
                            map[selected] = '.';
                            selected--;
                        } else if(e.getKeyCode() == KeyEvent.VK_D && selected != -1 && (map[selected + 1] == '.' || Blocks.isWater(map[selected + 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            map[selected + 1] = map[selected];
                            map[selected] = '.';
                            selected++;
                        }
                        /* удалить всю воду */
                        else if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
                            noWater = true;
                        }
                        /* изменить цвет фона */
                        else if(e.getKeyCode() == KeyEvent.VK_F3) {
                            JFrame setbg_fr = new JFrame("изменить цвет фона");
                            setbg_fr.setAlwaysOnTop(true);
                            setbg_fr.setSize(320, 140);
                            setbg_fr.setResizable(false);
                            setbg_fr.setLocationRelativeTo(null);
                            JPanel setbg_p = new JPanel();
                            setbg_p.setBounds(0, 0, 320, 140);
                            setbg_fr.setLayout(null);
                            JTextField setbg_tfR = new JTextField(8), setbg_tfG = new JTextField(8),
                                setbg_tfB = new JTextField(8);
                            setbg_tfR.setText("" + bgColor.getRed());
                            setbg_tfG.setText("" + bgColor.getGreen());
                            setbg_tfB.setText("" + bgColor.getBlue());
                            JButton setbg_b = new JButton("OK");
        
                            setbg_tfR.addKeyListener(new KeyListener() {
                                public void keyPressed(KeyEvent e) {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        if(gameState != 2) bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
                                        else sendMessage("/B " + setbg_tfR.getText() + " " + setbg_tfG.getText() + " " + setbg_tfB.getText());
                                        setbg_fr.setVisible(false);
                                    }
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
                            setbg_tfG.addKeyListener(new KeyListener() {
                                public void keyPressed(KeyEvent e) {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        if(gameState != 2) bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
                                        else sendMessage("/B " + setbg_tfR.getText() + " " + setbg_tfG.getText() + " " + setbg_tfB.getText());
                                        setbg_fr.setVisible(false);
                                    }
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
                            setbg_tfB.addKeyListener(new KeyListener() {
                                public void keyPressed(KeyEvent e) {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        if(gameState != 2) bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
                                        else sendMessage("/B " + setbg_tfR.getText() + " " + setbg_tfG.getText() + " " + setbg_tfB.getText());
                                        setbg_fr.setVisible(false);
                                    }
                                }
                                public void keyTyped(KeyEvent e) {}
                                public void keyReleased(KeyEvent e) {}
                            });
                            setbg_b.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                    if(gameState != 2) bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
                                    else sendMessage("/B " + setbg_tfR.getText() + " " + setbg_tfG.getText() + " " + setbg_tfB.getText());
                                    setbg_fr.setVisible(false);
                                }
                            });
        
                            setbg_p.add(setbg_tfR);
                            setbg_p.add(setbg_tfG);
                            setbg_p.add(setbg_tfB);
                            setbg_p.add(setbg_b);
                            setbg_fr.add(setbg_p);
                            setbg_fr.setVisible(true);
                        }
                        
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });
        
        fr.setVisible(true);
        startTime = System.currentTimeMillis();
        
        /* "физика" и пр. */
        if(gameState != 2) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            for(int i = map.length - WIDTH; i > WIDTH; i--) {
                                if(ph) {
                                    if(selected == -1 && Blocks.isFallen(map[i]) && (map[i + WIDTH] == '.' || Blocks.isWater(map[i + WIDTH]) ||
                                    map[i + WIDTH] == 'b') &&
                                    (!Blocks.isSticky(map[i - 1]) && !Blocks.isSticky(map[i + 1]) && !Blocks.isSticky(map[i - WIDTH]))) {
                                        map[i + WIDTH] = map[i];
                                        map[i] = '.';
                                    } else if(Blocks.isFallen(map[i]) && Blocks.isEraser(map[i + WIDTH])) {
                                        map[i] = '.';
                                    }
                                }
                            }
                            
                            if(!slow)
                                Thread.sleep(30);
                            else
                                Thread.sleep(70);
                        } catch(Exception e) {}
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            for(int i = WIDTH; i < map.length - WIDTH; i++) {
                                if(ph) {
                                    if(Blocks.isWater(map[i]) && !noWater) {
                                        if(map[i - 1] == 'f')
                                            map[i - 1] = 'b';
                                        if(map[i + 1] == 'f')
                                            map[i + 1] = 'b';
                                        if(map[i + WIDTH] == 'f')
                                            map[i + WIDTH] = 'b';
                                        if(map[i - WIDTH] == 'f')
                                            map[i - WIDTH] = 'b';
                                        
                                        if((map[i + WIDTH] != '.' && !Blocks.isWater(map[i + WIDTH])) || i + WIDTH * 2 > map.length) {
                                            if(map[i - 1] == '.') {
                                                if(!slow)
                                                    Thread.sleep(100);
                                                else
                                                    Thread.sleep(210);
                                                map[i - 1] = map[i];
                                            }
                                    
                                            if(map[i + 1] == '.') {
                                                if(!slow)
                                                    Thread.sleep(100);
                                                else
                                                    Thread.sleep(210);
                                                map[i + 1] = map[i];
                                            }
                                        }
                                
                                        if(map[i + WIDTH] == '.')
                                            map[i + WIDTH] = map[i];
                                    } else if(noWater) {
                                        for(int ii = 0; ii < map.length; ii++)
                                            if(Blocks.isWater(map[ii]))
                                                map[ii] = '.';
                                        noWater = false;
                                    }
                                }
                            }
                            Thread.sleep(30);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            
            new Thread() {
                public void run() {
                    while(true) {
                        for(int i = 0; i < map.length; i++) {
                            if(ph) {
                                try {
                                    /* работа батута */
                                    if(map[i] == '%' && (map[i - WIDTH] != '.' && map[i - WIDTH * 2] == '.' && map[i - WIDTH * 3] == '.' && map[i - WIDTH * 4] == '.')) {
                                        map[i - WIDTH * 4] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                    }
                                    /* работа порталов */
                                    else if(map[i] == 'p') {
                                        for(int ii = 0; ii < map.length - 1; ii++) {
                                            if(map[ii] == 'P') {
                                                if(map[i + 1] != '.')
                                                    map[ii - 1] = map[i + 1];
                                                if(map[i - 1] != '.')
                                                    map[ii + 1] = map[i - 1];
                                                if(map[i - WIDTH] != '.')
                                                    map[ii + WIDTH] = map[i - WIDTH];
                                    
                                                map[i - 1] = '.';
                                                map[i + 1] = '.';
                                                map[i - WIDTH] = '.';
                                                break;
                                            }
                                        }
                                    }
                                    /* работа блоков движения */
                                    else if(map[i] == '~' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH] != '~' && map[i - WIDTH + 1] == '.') {
                                        map[i - WIDTH + 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    } else if(map[i] == ',' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH] != '~' && map[i - WIDTH - 1] == '.') {
                                        map[i - WIDTH - 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    }
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        try {Thread.sleep(40);} catch(Exception e) {}
                    }
                }
            }.start();
        }
        
        /* обновление экрана и пр. */
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        if(selected != -1 && (map[selected] == '.' || Blocks.isWater(map[selected])))
                            selected = -1;
                        
                        if(following && selected != -1)
                            cameraStart = selected - (selectedBlockAddr() - cameraStart);
                        else if(following && selected == -1)
                            following = false;
                        
                        while(following && !jump && selected > WIDTH && !Blocks.isHelicopter(map[selected]) && !Blocks.isEraser(map[selected]) && map[selected + WIDTH] == '.' && ph) {
                            selected += WIDTH;
                            cameraStart = selected - (selectedBlockAddr() - cameraStart);
                            map[selected] = map[selected - WIDTH];
                            map[selected - WIDTH] = '.';
                            fr.repaint();
                            Thread.sleep(30);
                        }
                        
                        if(rain != -1) {
                            if(rain < 4)
                                rain++;
                            else
                                rain = 0;
                        }
                        
                        fr.repaint();
                        Thread.sleep(20);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        
        /* отправка сообщения */
        if(gameState == 2) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            if(!cmsg.isEmpty()) {
                                FileWriter fw = new FileWriter("current/msg");
                                fw.write(cmsg);
                                fw.close();
                                cmsg = "";
                            }
                            Thread.sleep(1000);
                        } catch(Exception e) {}
                    }
                }
            }.start();
        }
        
        /* логика стрелок */
        if(gameState != 2) {
            new Thread() {
                public void run() {
                    while(true) {
                            for(int i = 0; i < map.length; i++) {
                                try {
                                    if(Blocks.isArrow(map[i]) && Blocks.getLeftC(map[i]) == map[i] && (Blocks.isSticky(map[i - 1]) || Blocks.isSticky(map[i + 1]) || Blocks.isSticky(map[i - WIDTH]) || Blocks.isSticky(map[i + WIDTH]))) {
                                        continue;
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getLeftC(map[i]) == map[i] && map[i - 1] == '.') {
                                        map[i - 1] = map[i];
                                        map[i] = '.';
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getLeftC(map[i]) == map[i] && map[i - 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH - 1] == '.') {
                                        map[i - WIDTH - 1] = map[i];
                                        map[i] = '.';
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getRightC(map[i]) == map[i] && map[i + 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH + 1] == '.') {
                                        map[i - WIDTH + 1] = map[i];
                                        map[i] = '.';
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getLeftC(map[i]) == map[i]) {
                                        map[i] = Blocks.getRightC(map[i]);
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getRightC(map[i]) == map[i] && map[i + 1] == '.') {
                                        map[i + 1] = map[i];
                                        map[i] = '.';
                                        i++;
                                    } else if(Blocks.isArrow(map[i]) && Blocks.getRightC(map[i]) == map[i]) {
                                        map[i] = Blocks.getLeftC(map[i]);
                                    }
                                } catch(Exception e) {}
                            }
                            try {Thread.sleep(!slow ? 400 : 1000);} catch(Exception e) {}
                    }
                }
            }.start();
            
            /* выполнение поведения */
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            if(!behavior.isEmpty() && ph) {
                                String[] behaviorSplitted = behavior.split(" ");
                                
                                for(int i = 0; i < behaviorSplitted.length; i++) {
                                    behaviorSplitted[i] = behaviorSplitted[i].replace("selected", "" + (selected != -1 ? selected : selectedBlockAddr()));
                                    
                                    if(behaviorSplitted[i].isEmpty())
                                        continue;
                                    /* sel */
                                    else if(behaviorSplitted[i].split(":")[0].equals("sel") && behaviorSelected == 0) {
                                        behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                    }
                                    /* no_sel */
                                    else if(behaviorSplitted[i].split(":")[0].equals("no_sel")) {
                                        behaviorSelected = 0;
                                    }
                                    /* перемещение */
                                    else if(behaviorSplitted[i].equals("up")) {
                                        map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected -= WIDTH;
                                    } else if(behaviorSplitted[i].equals("down")) {
                                        map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected += WIDTH;
                                    } else if(behaviorSplitted[i].equals("right")) {
                                        map[behaviorSelected + 1] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected++;
                                    } else if(behaviorSplitted[i].equals("left")) {
                                        map[behaviorSelected - 1] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected--;
                                    } else if(behaviorSplitted[i].equals("up:lift")) {
                                        map[behaviorSelected - WIDTH * 2] = map[behaviorSelected - WIDTH];
                                        map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected -= WIDTH;
                                    } else if(behaviorSplitted[i].equals("down:lift")) {
                                        map[behaviorSelected + WIDTH * 2] = map[behaviorSelected + WIDTH];
                                        map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected += WIDTH;
                                    } else if(behaviorSplitted[i].equals("right:lift")) {
                                        map[behaviorSelected + 2] = map[behaviorSelected + 1];
                                        map[behaviorSelected + 1] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected++;
                                    } else if(behaviorSplitted[i].equals("left:lift")) {
                                        map[behaviorSelected - 2] = map[behaviorSelected - 1];
                                        map[behaviorSelected - 1] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected--;
                                    } else if(behaviorSplitted[i].equals("up:copy")) {
                                        map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                        behaviorSelected -= WIDTH;
                                    } else if(behaviorSplitted[i].equals("down:copy")) {
                                        map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                        behaviorSelected += WIDTH;
                                    } else if(behaviorSplitted[i].equals("right:copy")) {
                                        map[behaviorSelected + 1] = map[behaviorSelected];
                                        behaviorSelected++;
                                    } else if(behaviorSplitted[i].equals("left:copy")) {
                                        map[behaviorSelected - 1] = map[behaviorSelected];
                                        behaviorSelected--;
                                    }
                                    /* fire, fire2 и boom */
                                    else if(behaviorSplitted[i].split(":")[0].equals("fire")) {
                                        fire(behaviorSelected);
                                    } else if(behaviorSplitted[i].split(":")[0].equals("fire2")) {
                                        fire2(behaviorSelected);
                                    } else if(behaviorSplitted[i].split(":")[0].equals("boom")) {
                                        boom(behaviorSelected);
                                    }
                                    /* set */
                                    else if(behaviorSplitted[i].split(":")[0].equals("set")) {
                                        map[behaviorSelected] = behaviorSplitted[i].split(":")[1].toCharArray()[0];
                                    }
                                    /* ~<...> */
                                    else if(behaviorSplitted[i].startsWith("~")) {
                                        Thread.sleep(!slow ? Integer.parseInt(behaviorSplitted[i].replace("~", "")) : Integer.parseInt(behaviorSplitted[i].replace("~", "")) * 2);
                                    }
                                    /* wait */
                                    else if(behaviorSplitted[i].equals("wait:up")) {
                                        while(map[behaviorSelected - WIDTH] == '.' && !behavior.isEmpty())
                                            Thread.sleep(40);
                                    } else if(behaviorSplitted[i].equals("wait:down")) {
                                        while(map[behaviorSelected + WIDTH] == '.' && !behavior.isEmpty())
                                            Thread.sleep(40);
                                    } else if(behaviorSplitted[i].equals("wait:right")) {
                                        while(map[behaviorSelected + 1] == '.' && !behavior.isEmpty())
                                            Thread.sleep(40);
                                    } else if(behaviorSplitted[i].equals("wait:left")) {
                                        while(map[behaviorSelected - 1] == '.' && !behavior.isEmpty())
                                            Thread.sleep(40);
                                    } else if(behaviorSplitted[i].equals("wait")) {
                                        while(map[behaviorSelected - 1] == '.'     &&
                                              map[behaviorSelected + 1] == '.'     &&
                                              map[behaviorSelected + WIDTH] == '.' &&
                                              map[behaviorSelected - WIDTH] == '.' && !behavior.isEmpty())
                                            Thread.sleep(40);
                                    }
                                    /* tp */
                                    else if(behaviorSplitted[i].startsWith("tp:")) {
                                        map[Integer.parseInt(behaviorSplitted[i].split(":")[1])] = map[behaviorSelected];
                                        map[behaviorSelected] = '.';
                                        behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                    }
                                    /* step */
                                    else if(behaviorSplitted[i].startsWith("step:")) {
                                        int delay  = Integer.parseInt(behaviorSplitted[i].split(":")[2]),
                                            target = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                        
                                        while(behaviorSelected != target && !behavior.isEmpty()) {
                                            if(behaviorSelected / WIDTH < target / WIDTH && map[behaviorSelected + WIDTH] == '.') {
                                                map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                                map[behaviorSelected] = '.';
                                                behaviorSelected += WIDTH;
                                            } else if(behaviorSelected / WIDTH < target / WIDTH && map[behaviorSelected + WIDTH] != '.') {
                                                break;
                                            } else if(behaviorSelected / WIDTH > target / WIDTH && map[behaviorSelected - WIDTH] == '.') {
                                                map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                                map[behaviorSelected] = '.';
                                                behaviorSelected -= WIDTH;
                                            } else if(behaviorSelected / WIDTH > target / WIDTH && map[behaviorSelected - WIDTH] != '.') {
                                                break;
                                            }
                                            
                                            if(behaviorSelected % WIDTH < target % WIDTH && map[behaviorSelected + 1] == '.') {
                                                map[behaviorSelected + 1] = map[behaviorSelected];
                                                map[behaviorSelected] = '.';
                                                behaviorSelected++;
                                            } else if(behaviorSelected % WIDTH < target % WIDTH && map[behaviorSelected + 1] != '.') {
                                                break;
                                            } else if(behaviorSelected % WIDTH > target % WIDTH && map[behaviorSelected - 1] == '.') {
                                                map[behaviorSelected - 1] = map[behaviorSelected];
                                                map[behaviorSelected] = '.';
                                                behaviorSelected--;
                                            } else if(behaviorSelected % WIDTH > target % WIDTH && map[behaviorSelected - 1] != '.') {
                                                break;
                                            }
                                            
                                            Thread.sleep((long)delay);
                                        }
                                    }
                                }
                            }
                            
                            Thread.sleep(500);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        
        /* для многопользовательской игры */
        
        if(gameState == 1) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            FileWriter fw = new FileWriter("current/map");
                            fw.write(behavior + "\n");
                            fw.write("" + bgColor.getRed() + " " + bgColor.getGreen() + " " + bgColor.getBlue() + "\n");
                            int iii = 0;
                            for(int i = 0; i < HEIGHT; i++) {
                                for(int ii = 0; ii < WIDTH; ii++) {
                                    fw.write(map[iii]);
                                    iii++;
                                }
                                fw.write("\n");
                            }
                            fw.close();
                            
                            fw = new FileWriter("current/adminPos");
                            fw.write("" + selectedBlockAddr());
                            fw.close();
                            
                            try {
                                Scanner sc = new Scanner(new File("current/msg"));
                                String[] messages = sc.nextLine().split(";;;");
                                sc.close();
                                
                                for(int msgi = 0; msgi < messages.length; msgi++) {
                                    message = messages[msgi];
                                    /* выполнение команд */
                                    if(serverAllowCreators && message.startsWith("/")) {
                                        message = message.split(" <")[0];
                                        String[] command = message.subSequence(1, message.length()).toString().split(" ");
                                        
                                        if(command[0].startsWith("c")) {
                                            char[] chrs = command[2].toCharArray();
                                            int start = Integer.parseInt(command[1]);
                                            for(int i = 0; i < chrs.length; i++)
                                                map[start + i] = chrs[i];
                                        } else if(command[0].startsWith("C")) {
                                            char[] chrs = command[2].toCharArray();
                                            int start = Integer.parseInt(command[1]);
                                            for(int i = 0; i < chrs.length; i++)
                                                map[start + i * WIDTH] = chrs[i];
                                        } else if(command[0].startsWith("f")) {
                                            fire(Integer.parseInt(command[1]));
                                        } else if(command[0].startsWith("F")) {
                                            fire2(Integer.parseInt(command[1]));
                                        } else if(command[0].startsWith("b")) {
                                            boom(Integer.parseInt(command[1]));
                                        } else if(command[0].equals("p")) {
                                            behavior = message.subSequence(3, message.length()).toString();
                                        } else if(command[0].startsWith("B")) {
                                            bgColor = new Color(Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                                        }
                                        
                                        message = "";
                                        
                                        fw = new FileWriter("current/msg");
                                        fw.write("");
                                        fw.close();
                                    }
                                }
                            } catch(Exception ex) {}
                            
                            Thread.sleep(400);
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        } else if(gameState == 2) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            /* загрузка карты, поведения и позиции хоста */
                            
                            if(selected == -1) {
                                Scanner sc = new Scanner(new File("current/map"));
                                if(!programmingMode) behavior = sc.nextLine();
                                else sc.nextLine();
                                String bgColorNew = sc.nextLine();
                                bgColor = new Color(Integer.parseInt(bgColorNew.split(" ")[0]), Integer.parseInt(bgColorNew.split(" ")[1]), Integer.parseInt(bgColorNew.split(" ")[2]));
                                
                                int ii = 0;
                                while(sc.hasNextLine()) {
                                    char[] line = sc.nextLine().toCharArray();
                                    for(int i = 0; i < WIDTH; i++)
                                        map[ii++] = line[i];
                                }
                                sc.close();
                            }
                            
                            try {
                                Scanner sc = new Scanner(new File("current/adminPos"));
                                adminPos = Integer.parseInt(sc.nextLine());
                                sc.close();
                            } catch(Exception ex) {}
                            /******************/
                            
                            Thread.sleep(400);
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.drawRect(0, 0, 1024, 728); g.fillRect(0, 0, 1024, 728);
        /* рисование объектов
           ================== */
        int iii = cameraStart;
        for(int i = 0; i < 17; i++) {
            for(int ii = 0; ii < 22; ii++) {
                try {
                    if(iii < map.length && iii >= 0) {
                        g.setColor(bgColor);
                        g.fillRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        if(ui) {
                            try {
                                g.setColor(bgColor.getRed() < 195 && bgColor.getGreen() < 195 && bgColor.getBlue() < 195 ? new Color(bgColor.getRed() + 60, bgColor.getGreen() + 60, bgColor.getBlue() + 60) : new Color(bgColor.getRed() - 60, bgColor.getGreen() - 60, bgColor.getBlue() - 60));
                                g.drawRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            } catch(IllegalArgumentException e) {}
                        }
                        if(rain != -1)
                            g.drawImage(new ImageIcon("current/images/engine/rain" + rain + ".png").getImage(), ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH, null);
                    }
                    
                    if(map[iii] == '~' || map[iii] == ',') {
                        g.setColor(new Color(5, 5, 5));
                        g.fillRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        g.setColor(new Color(60, 60, 60));
                        g.fillRect(ii * Blocks.defaultW + Blocks.defaultW / 3, i * Blocks.defaultH + Blocks.defaultH / 3, Blocks.defaultW / 3, Blocks.defaultH / 3);
                        
                        if(ui) {
                            g.setFont(new Font("Monospaced", Font.PLAIN, 14));
                            g.setColor(new Color(255, 255, 255));
                            g.drawString((map[iii] == ',' ? "<" : ">"), ii * Blocks.defaultW + 5, i * Blocks.defaultH + 5);
                        }
                    } else if(map[iii] == '%') {
                        g.setColor(new Color(255, 160, 0));
                        g.fillRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                    } else if(map[iii] == 'b') {
                        g.setColor(new Color(5, 5, 5));
                        g.fillRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                    }
                    /* блок мода */
                    else if(Blocks.getTextureX(map[iii]) == Blocks.defaultX && Blocks.getTextureY(map[iii]) == Blocks.defaultY &&
                            Blocks.getTextureWidth(map[iii]) <= Blocks.defaultW && Blocks.getTextureHeight(map[iii]) <= Blocks.defaultH) {
                        g.drawImage(Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]), i * Blocks.defaultH + Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]), Blocks.getTextureHeight(map[iii]), null);
                        if(Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.')
                            map[iii] = Blocks.getC2(map[iii]);
                    }
                } catch(ArrayIndexOutOfBoundsException e) {}
                iii++;
            }
            iii += WIDTH - 22;
        }
        
        iii = cameraStart;
        for(int i = 0; i < 17; i++) {
            for(int ii = 0; ii < 22; ii++) {
                try {
                    if(Main.map[iii] == 'f') {
                        g.drawImage(new ImageIcon("current/images/engine/fire.png").getImage(), ii * Blocks.defaultW - Blocks.defaultW / 2, i * Blocks.defaultH - Blocks.defaultH * 2, Blocks.defaultW * 3, Blocks.defaultH * 3, null);
                        if(rain != -1 && selected == -1)
                            Main.map[iii] = 'b';
                    } else if(Main.map[iii] == 'F')
                        g.drawImage(new ImageIcon("current/images/engine/fire2.png").getImage(), ii * Blocks.defaultW - Blocks.defaultW / 2, i * Blocks.defaultH - Blocks.defaultH * 2, Blocks.defaultW * 3, Blocks.defaultH * 3, null);
                    else if((int)Main.map[iii] >= (int)'0' && (int)Main.map[iii] <= (int)'9')
                        g.drawImage(new ImageIcon("current/images/engine/boom" + Main.map[iii] + ".png").getImage(), ii * Blocks.defaultW - Blocks.defaultW, i * Blocks.defaultH - Blocks.defaultH, Blocks.defaultW * 3, Blocks.defaultH * 3, null);
                    else if(map[iii] == 'p') {
                        g.drawImage(new ImageIcon("current/images/engine/portal0.png").getImage(), ii * Blocks.defaultW, i * Blocks.defaultH - Blocks.defaultH, Blocks.defaultW, Blocks.defaultH * 2, null);
                    } else if(map[iii] == 'P') {
                        g.drawImage(new ImageIcon("current/images/engine/portal1.png").getImage(), ii * Blocks.defaultW, i * Blocks.defaultH - Blocks.defaultH, Blocks.defaultW, Blocks.defaultH * 2, null);
                    } else if((map[iii] >= 'а' && map[iii] <= 'я') || (map[iii] >= 'А' && map[iii] <= 'Я') || map[iii] == '-' || map[iii] == '!' || map[iii] == '?' || map[iii] == 'ё' || map[iii] == 'Ё') {
                        g.setColor(new Color(255, 255, 255));
                        g.setFont(new Font("Monospaced", Font.PLAIN, Blocks.defaultW));
                        g.drawString("" + map[iii], ii * Blocks.defaultW + Blocks.defaultW / 4, i * Blocks.defaultH + Blocks.defaultH);
                    }
                    /* блок мода */
                    else if(Blocks.getTextureX(map[iii]) != Blocks.defaultX || Blocks.getTextureY(map[iii]) != Blocks.defaultY ||
                            Blocks.getTextureWidth(map[iii]) > Blocks.defaultW || Blocks.getTextureHeight(map[iii]) > Blocks.defaultH) {
                        g.drawImage(Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]), i * Blocks.defaultH + Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]), Blocks.getTextureHeight(map[iii]), null);
                        if(Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.')
                            map[iii] = Blocks.getC2(map[iii]);
                    }
                    
                    /* подсветка выбранного блока */
                    if(iii == selected && ui && !following) {
                        g.setColor(new Color(0, 255, 0));
                        g.drawRect(ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                    }
                } catch(ArrayIndexOutOfBoundsException e) {}
                iii++;
            }
            iii += WIDTH - 22;
        }
        /*********************************/
        
        if(ui) {
            iii = cameraStart;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 22; ii++) {
                    try {
                        if(forBoom[iii] && !programmingMode) {
                            g.drawImage(new ImageIcon("current/images/engine/red.png").getImage(), ii * Blocks.defaultW - Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW * 3, Blocks.defaultH, null);
                            g.drawImage(new ImageIcon("current/images/engine/red.png").getImage(), ii * Blocks.defaultW, i * Blocks.defaultH - Blocks.defaultH, Blocks.defaultW, Blocks.defaultH * 3, null);
                        } else if(behaviorSelected2 != -1 && iii == behaviorSelected2 && programmingMode)
                            g.drawImage(new ImageIcon("current/images/engine/red.png").getImage(), ii * Blocks.defaultW, i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH, null);
                        else if(gameState == 2 && iii == adminPos)
                            g.drawImage(new ImageIcon("current/images/engine/pricel.png").getImage(), ii * Blocks.defaultW + Blocks.defaultW / 2, i * Blocks.defaultH + Blocks.defaultH / 2, 12, 8, null);
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 22;
            }
            
            g.drawImage(new ImageIcon("current/images/engine/pricel.png").getImage(), 1024 / 2 + 8, 700 / 2 - 30, 12, 8, null);
            g.drawImage(new ImageIcon("current/images/engine/vignette.png").getImage(), 0, 0, 1024, 728, null);
            
            g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
            g.setColor(new Color(255, 255, 0));
            if(!fill)
                g.drawString("" + fill((System.currentTimeMillis() - startTime) / 60000, 2) + ":" + fill((System.currentTimeMillis() - startTime) / 1000 % 60, 2), 15, 20);
            else
                g.drawString("Выберите сторону...", 15, 20);
            
            if(programmingMode) {
                g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 11));
                g.drawString("Режим программирования", 15, 60);
                char[] behavior2 = behavior.toCharArray();
                
                iii = 0;
                loop: for(int i = 0; i < 8; i++) {
                    for(int ii = 0; ii < 128; ii++, iii++) {
                        try {
                            g.drawString("" + behavior2[iii], 15 + ii * 7, 80 + i * 20);
                        } catch(ArrayIndexOutOfBoundsException e) {
                            break loop;
                        }
                    }
                }
            }
            
            g.setColor(new Color(255, 255, 255));
            g.drawString("" + selectedBlockAddr(), 15, 40);
            try {g.drawString("" + map[selectedBlockAddr()] + " [" + (select ? "???" : currentBlock) + "]", 65, 40);} catch(ArrayIndexOutOfBoundsException e) {}
            
            g.setColor(new Color(250, 250, 250));
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            
            if(slow)
                g.drawString("~", 970, 20);
            
            g.setFont(new Font("Monospaced", Font.PLAIN, 14));
            
            if(gameState == 1) g.drawString(message, 15, 60);
            
            if(help && !programmingMode) {
                g.drawImage(new ImageIcon("current/images/engine/black.png").getImage(), 0, 0, 1024, 728, null);
                
                String[] helpMessageSplitted = helpMessage.split("\n");
                for(int i = 0; i < helpMessageSplitted.length; i++)
                    g.drawString(helpMessageSplitted[i], 15, (i + 1) * 18);
            } else if(help && programmingMode) {
                g.drawImage(new ImageIcon("current/images/engine/black.png").getImage(), 0, 0, 1024, 728, null);
                
                g.setFont(new Font("Monospaced", Font.PLAIN, 15));
                
                g.drawString("<стрелки>.......:   перемещение", 20, 20);
                g.drawString("wasd............:   up, left, down, right", 20, 40);
                g.drawString("WASD............:   up:copy, left:copy, down:copy, right:copy", 20, 60);
                g.drawString("ijkl............:   up:lift, left:lift, down:lift, right:lift", 20, 80);
                g.drawString("fF..............:   fire, fire2", 20, 100);
                g.drawString("b...............:   boom", 20, 120);
                g.drawString("<Home>..........:   стереть весь код", 20, 140);
                g.drawString("<Backspace>.....:   стереть последний блок кода", 20, 160);
                g.drawString("<пробел>........:   sel:<...> (выбрать блок под прицелом)", 20, 180);
                g.drawString("<Ctrl+X>........:   no_sel (убрать выделение)", 20, 200);
                g.drawString("<Enter>.........:   set:<след. клавиша>", 20, 220);
                g.drawString("<Insert>........:   изменение кода в текстовом режиме", 20, 240);
                g.drawString("0123456789-.....:   ~<...> (приостановить выполнение на 50/100/200/.../1000/5000 миллисекунд", 20, 260);
                g.drawString("Z...............:   wait (ждать любого столкновения)", 20, 280);
                g.drawString("z, а после w/a/s/d: wait:up/left/down/right (ждать столкновения в опр. стороне)", 20, 300);
                g.drawString("<F5>............:   выход из режима программирования", 20, 320);
                g.drawString("<F1>............:   скрыть/показать эту помощь", 20, 340);
                g.drawString("T...............:   tp:<адрес блока под прицелом>", 20, 360);
                g.drawString("E, а после 0..9 :   step:<...>:<...> (шагать с перерывами в 50/100/.../1000 миллисекунд)", 20, 380);
            }
        }
    }
    
    /* предназначен для возвращения строки из max символов,
       где символы, которые не относятся к num, равны '0'   */
    private static String fill(long num, int max) {
        String result = "";
        for(int i = max; i > ("" + num).length(); i--)
            result += "0";
        result += ("" + num).replace("-", "0");
        return result;
    }
}

