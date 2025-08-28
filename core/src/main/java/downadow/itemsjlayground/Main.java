/*
   Copyright 2025 Menshikov S.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package downadow.itemsjlayground;

import com.badlogic.gdx.files.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.net.*;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.Input.*;
import java.util.Random;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main implements ApplicationListener {
    SpriteBatch batch;
    ShapeRenderer shape;
    FitViewport viewport;
    Vector2 touch;
    BitmapFont font;
    HttpRequestBuilder rqbuilder;
    Sound click;
    short scene;
    boolean pleaseWait = false, msgSaved = false, nextTexture = false,
        shoot = false;
    final short S_START = 0,
        S_MENU = 1,
        S_LAN_MENU = 2,
        S_GAME = 3;
    Texture blackTexture, black2Texture, fireTexture, fire2Texture, portal0Texture,
        portal1Texture, redTexture, rightTexture, leftTexture, vignetteTexture,
        pricelTexture, upBtnTexture, downBtnTexture, rightBtnTexture,
        leftBtnTexture, helpBtnTexture, selectBtnTexture, fillBtnTexture,
        fireBtnTexture, fire2BtnTexture, boomBtnTexture, delBoomBtnTexture,
        nightBtnTexture, rainBtnTexture, slowBtnTexture, uiBtnTexture,
        qBtnTexture, eBtnTexture, moreBtnTexture, bgColorBtnTexture,
        programmingModeBtnTexture, chatBtnTexture, phBtnTexture;
    Texture[] boomTextures, rainTextures, bgTextures;
    String text;
    boolean done, retval;
    String[] rpList;
    String root;
    int selectedRp = 0;
    boolean up = false, down = false, left = false, right = false;
    boolean cancelMovement = false;
    
    /* ширина и высота карты в объектах */
    private final int WIDTH = 250, HEIGHT = 80;
    /* карта */
    private char[] map = new char[WIDTH * HEIGHT];
    
    private boolean[] forBoom = new boolean[WIDTH * HEIGHT];
    private boolean[] lightMap = new boolean[WIDTH * HEIGHT];
    
    private boolean ph = true, night = false, writeMessage = false;
    
    private int bgColorRed = 80,
        bgColorGreen = 80,
        bgColorBlue = 80;
    
    private int behaviorSelected2 = -1;
    
    private int helpIndex = 0;
    
    private boolean jump = false, blockHelicopter = false;
    
    private boolean programmingMode = false;
    
    private String behavior = "", cmsg = "[joined]", selectNumber = "";
    
    private boolean ui = true;
    /* блокировка управления */
    private boolean block = false;
    /* замедленное время */
    private boolean slow = false;
    
    private boolean noWater = false;
    
    private boolean select = false, step = false;
    
    private boolean fill = false;
    
    private int cameraStart = 125 + 70 * WIDTH;
    /* выбранный блок */
    private int selected = -1;
    /* вывести помощь */
    private boolean help = true;
    
    private char currentBlock = '.';
    
    private int rain = -1;
    
    private int colorPointer = 0;
    
    private String[] helpMessage;
    
    /* 0   одиночная игра
     * 1   сервер
     * 2   клиент
     */
    private int gameState = 0;
    
    private String message = "", connectUrl = "";
    
    private int adminPos = 0, pos2 = 0;
    private int etcPage = 0;
    private int selectedNewPos = -1;
    
    public void create() {
        root = ".items-jlayground.data";
        rqbuilder = new HttpRequestBuilder();
        text = "";
        
        click = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        
        blackTexture = new Texture("black.png");
        black2Texture = new Texture("black2.png");
        redTexture = new Texture("red.png");
        fireTexture = new Texture("fire.png");
        fire2Texture = new Texture("fire2.png");
        vignetteTexture = new Texture("vignette.png");
        pricelTexture = new Texture("pricel.png");
        rightTexture = new Texture("right.png");
        leftTexture = new Texture("left.png");
        portal0Texture = new Texture("portal0.png");
        portal1Texture = new Texture("portal1.png");
        rainTextures = new Texture[] {new Texture("rain0.png"), new Texture("rain1.png"),
            new Texture("rain2.png"), new Texture("rain3.png"), new Texture("rain4.png")};
        bgTextures = new Texture[] {new Texture("bg.png"), new Texture("bg0.png"), new Texture("bg1.png"),
            new Texture("bg2.png"), new Texture("bg3.png"), new Texture("bg4.png")};
        boomTextures = new Texture[] {new Texture("boom0.png"), new Texture("boom1.png"), new Texture("boom2.png"),
            new Texture("boom3.png"), new Texture("boom4.png"), new Texture("boom5.png"), new Texture("boom6.png")};
        
        upBtnTexture = new Texture("up_btn.png");
        downBtnTexture = new Texture("down_btn.png");
        leftBtnTexture = new Texture("left_btn.png");
        rightBtnTexture = new Texture("right_btn.png");
        selectBtnTexture = new Texture("select_btn.png");
        helpBtnTexture = new Texture("help_btn.png");
        fillBtnTexture = new Texture("fill_btn.png");
        fireBtnTexture = new Texture("fire_btn.png");
        fire2BtnTexture = new Texture("fire2_btn.png");
        boomBtnTexture = new Texture("boom_btn.png");
        delBoomBtnTexture = new Texture("delboom_btn.png");
        nightBtnTexture = new Texture("night_btn.png");
        rainBtnTexture = new Texture("rain_btn.png");
        slowBtnTexture = new Texture("slow_btn.png");
        uiBtnTexture = new Texture("ui_btn.png");
        qBtnTexture = new Texture("q_btn.png");
        eBtnTexture = new Texture("e_btn.png");
        moreBtnTexture = new Texture("more_btn.png");
        phBtnTexture = new Texture("ph_btn.png");
        bgColorBtnTexture = new Texture("bgcolor_btn.png");
        chatBtnTexture = new Texture("chat_btn.png");
        programmingModeBtnTexture = new Texture("programmingmode_btn.png");
        
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int key) {
                if(scene == S_GAME) {
                    try {
                        if(selected != -1 && (map[selected] == '.' || Blocks.isWater(map[selected]))) {
                            if(Blocks.isWater(map[selected]) && gameState != 2)
                                noWater = true;
                            selected = -1;
                        }
                        
                        if(select)
                            return false;
                        /* "прыжок" */
                        else if(key == Input.Keys.UP && selected != -1 && ((!Blocks.isHelicopter(map[selected]) && !Blocks.isEraser(map[selected])) && ph)) {
                            new Thread() {
                                public void run() {
                                    try {
                                        jump = true;
                                        
                                        if(map[selected + WIDTH] != '.') {
                                            for(int i = 0; i < 3; i++) {
                                                if(map[selected - WIDTH] == '.')
                                                    selectedNewPos = selected - WIDTH;
                                                Thread.sleep(!slow ? 150 : 300);
                                            }
                                        }
                                        Thread.sleep(!slow ? 200 : 400);
                                        
                                        jump = false;
                                    } catch(Exception e) {}
                                }
                            }.start();
                            
                            return true;
                        /* перемещение выделенного блока */
                        } else if(key == Input.Keys.UP && selected != -1 && (map[selected - WIDTH] == '.' || Blocks.isWater(map[selected - WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            selectedNewPos = selected - WIDTH;
                            up = true;
                        } else if(key == Input.Keys.DOWN && selected != -1 && (map[selected + WIDTH] == '.' || Blocks.isWater(map[selected + WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            selectedNewPos = selected + WIDTH;
                            down = true;
                        } else if(key == Input.Keys.LEFT && selected != -1 && (map[selected - 1] == '.' || Blocks.isWater(map[selected - 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            selectedNewPos = selected - 1;
                            left = true;
                        } else if(key == Input.Keys.RIGHT && selected != -1 && (map[selected + 1] == '.' || Blocks.isWater(map[selected + 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            selectedNewPos = selected + 1;
                            right = true;
                        }
                        /* перемещение */
                        else if(key == Input.Keys.UP && !fill && !help && selected == -1) {
                            cameraStart -= WIDTH;
                            up = true;
                            return true;
                        } else if(key == Input.Keys.DOWN && !fill && !help && selected == -1) {
                            cameraStart += WIDTH;
                            down = true;
                            return true;
                        } else if(key == Input.Keys.RIGHT && !fill && !help && selected == -1) {
                            cameraStart++;
                            right = true;
                            return true;
                        } else if(key == Input.Keys.LEFT && !fill && !help && selected == -1) {
                            cameraStart--;
                            left = true;
                            return true;
                        }
                        
                        if(!programmingMode && !writeMessage) {
                            if(key == Input.Keys.F1) {
                                click.play(0.4f);
                                help = !help;
                                return true;
                            } else if(key == Input.Keys.C) {
                                click.play(1.0f);
                                setBlock(selectedBlockAddr(), currentBlock);
                                return true;
                            } else if(key == Input.Keys.S && selected == -1) {
                                String save = behavior + "\n" + bgColorRed + " " + bgColorGreen + " " + bgColorBlue + "\n";
                                int iii = 0;
                                for(int i = 0; i < HEIGHT; i++) {
                                    for(int ii = 0; ii < WIDTH; ii++) {
                                        save += "" + map[iii];
                                        iii++;
                                    }
                                    save += "\n";
                                }
                                Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").writeString(save, false, "UTF-8");
                                msgSaved = !msgSaved;
                                return true;
                            } else if(key == Input.Keys.BACKSPACE) {
                                click.play(1.0f);
                                setBlock(selectedBlockAddr(), '.');
                                return true;
                            } else if(key == Input.Keys.UP && help && helpIndex > 0) {
                                helpIndex -= 4;
                                return true;
                            } else if(key == Input.Keys.DOWN && help) {
                                helpIndex += 4;
                                return true;
                            /* заполнение */
                            } else if(key == Input.Keys.LEFT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - 1] == '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() - 1, j = 1; map[i] == '.' && j < WIDTH; i--, j++)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.RIGHT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + 1] == '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() + 1, j = 1; map[i] == '.' && j < WIDTH; i++, j++)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.DOWN && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + WIDTH] == '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() + WIDTH; map[i] == '.'; i += WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.UP && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - WIDTH] == '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() - WIDTH; map[i] == '.'; i -= WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            
                            else if(key == Input.Keys.LEFT && fill && map[selectedBlockAddr()] != '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() - 1; map[i] != '.'; i--)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.RIGHT && fill && map[selectedBlockAddr()] != '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() + 1; map[i] != '.'; i++)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.DOWN && fill && map[selectedBlockAddr()] != '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() + WIDTH; map[i] != '.'; i += WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            else if(key == Input.Keys.UP && fill && map[selectedBlockAddr()] != '.') {
                                fill = false;
                                click.play(1.0f);
                                for(int i = selectedBlockAddr() - WIDTH; map[i] != '.'; i -= WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                return true;
                            }
                            /* включение/выключение "физики" */
                            else if(key == Input.Keys.F6) {
                                click.play(0.4f);
                                ph = !ph;
                                return true;
                            }
                            /* изменение цвета фона */
                            else if(key == Input.Keys.F3) {
                                click.play(0.4f);
                                colorPointer++;
                                if(colorPointer == 6)
                                    colorPointer = 0;
                                
                                if(colorPointer == 0) {
                                    bgColorRed = 80;
                                    bgColorGreen = 80;
                                    bgColorBlue = 80;
                                } else if(colorPointer == 1) {
                                    bgColorRed = 60;
                                    bgColorGreen = 60;
                                    bgColorBlue = 60;
                                } else if(colorPointer == 2) {
                                    bgColorRed = 100;
                                    bgColorGreen = 0;
                                    bgColorBlue = 0;
                                } else if(colorPointer == 3) {
                                    bgColorRed = 200;
                                    bgColorGreen = 200;
                                    bgColorBlue = 100;
                                } else if(colorPointer == 4) {
                                    bgColorRed = 160;
                                    bgColorGreen = 160;
                                    bgColorBlue = 255;
                                } else if(colorPointer == 5) {
                                    bgColorRed = 130;
                                    bgColorGreen = 90;
                                    bgColorBlue = 190;
                                }
                                
                                return true;
                            }
                            /* включение/выключение ночи */
                            else if(key == Input.Keys.N) {
                                click.play(0.4f);
                                night = !night;
                                return true;
                            }
                            
                            if(!block) {
                                if(key == Input.Keys.F4) {
                                    click.play(0.4f);
                                    fill = !fill;
                                /* показать/скрыть помощь */
                                } else if(key == Input.Keys.F1 && ui) {
                                    click.play(0.4f);
                                    help = !help;
                                /* включить/выключить замедление времени */
                                } else if(key == Input.Keys.F2) {
                                    click.play(0.4f);
                                    slow = !slow;
                                /*******************************/
                                } else if(key == Input.Keys.F10) {
                                    click.play(0.4f);
                                    rain = (rain < 0 ? 0 : -10);
                                /*******************************/
                                } else if(key == Input.Keys.ESCAPE && ui) {
                                    click.play(0.4f);
                                    help = false;
                                    ui = false;
                                } else if(key == Input.Keys.ESCAPE && !ui) {
                                    click.play(0.4f);
                                    ui = true;
                                /* поставить огонь */
                                } else if(key == Input.Keys.F && map[selectedBlockAddr()] != '.' &&
                                          !Blocks.isFireResistant(map[selectedBlockAddr()])) {
                                    fire(selectedBlockAddr());
                                } else if(key == Input.Keys.V && map[selectedBlockAddr()] != '.')
                                    fire2(selectedBlockAddr());
                                /* выбрать блок под прицелом */
                                else if(key == Input.Keys.SPACE && selected == -1 && map[selectedBlockAddr()] != '.') {
                                    click.play(0.4f);
                                    selectedNewPos = -1;
                                    selected = selectedBlockAddr();
                                /* убрать выделение */
                                } else if(key == Input.Keys.SPACE && selected != -1) {
                                    if(gameState == 2) setBlock(selected, map[selected]);
                                    selected = -1;
                                }
                                /* отражение объектов */
                                else if(key == Input.Keys.Q && map[selected] == '~')
                                    map[selected] = ',';
                                else if(key == Input.Keys.E && map[selected] == ',')
                                    map[selected] = '~';
                                else if(key == Input.Keys.Q)
                                    map[selected] = Blocks.getLeftC(map[selected]);
                                else if(key == Input.Keys.E)
                                    map[selected] = Blocks.getRightC(map[selected]);
                                /* взрыв */
                                else if(key == Input.Keys.ENTER && selected == -1) {
                                    click.play(0.7f);
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
                                } else if(key == Input.Keys.B) {
                                    forBoom[selectedBlockAddr()] = true;
                                }
                                else if(key == Input.Keys.M) {
                                    for(int i = 0; i < forBoom.length; i++)
                                        forBoom[i] = false;
                                }
                                /* активация объектов */
                                
                                else if(key == Input.Keys.ENTER && Blocks.isTank(map[selected]) && Blocks.getRightC(map[selected]) == map[selected]) {
                                    for(int i = 1; i < 16; i++) {
                                        if((map[selected + i - WIDTH] != '.' && !(map[selected + i - WIDTH] >= '0' && map[selected + i - WIDTH] <= '9')) || (map[selected + i] != '.' && !(map[selected + i] >= '0' && map[selected + i] <= '9'))) {
                                            click.play(0.7f);
                                            shoot = true;
                                            boom(selected + i - WIDTH);
                                            
                                            new Thread() {
                                                public void run() {
                                                    try {
                                                        Thread.sleep(60);
                                                        shoot = false;
                                                    } catch(Exception ex) {}
                                                }
                                            }.start();
                                            
                                            break;
                                        }
                                    }
                                } else if(key == Input.Keys.ENTER && Blocks.isTank(map[selected]) && Blocks.getLeftC(map[selected]) == map[selected]) {
                                    for(int i = 1; i < 16; i++) {
                                        if((map[selected - i - WIDTH] != '.' && !(map[selected - i - WIDTH] >= '0' && map[selected - i - WIDTH] <= '9')) || (map[selected - i] != '.' && !(map[selected - i] >= '0' && map[selected - i] <= '9'))) {
                                            click.play(0.7f);
                                            shoot = true;
                                            boom(selected - i - WIDTH);
                                            
                                            new Thread() {
                                                public void run() {
                                                    try {
                                                        Thread.sleep(60);
                                                        shoot = false;
                                                    } catch(Exception ex) {}
                                                }
                                            }.start();
                                            
                                            break;
                                        }
                                    }
                                } else if(key == Input.Keys.ENTER && Blocks.isHelicopter(map[selected]) && map[selected + WIDTH] == '.' && !blockHelicopter) {
                                    click.play(0.7f);                                    
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
                                /* удалить всю воду */
                                else if(key == Input.Keys.F8) {
                                    click.play(0.4f);
                                    noWater = true;
                                }
                            }
                        }
                        return true;
                    } catch(Exception ex) {}
                }
                
                return false;
            }
            
            public boolean keyUp(int key) {
                if(scene == S_GAME) {
                    try {
                        if(select)
                            return false;
                        
                        else if(key == Input.Keys.UP) {
                            up = false;
                            cancelMovement = true;
                        } else if(key == Input.Keys.DOWN) {
                            down = false;
                            cancelMovement = true;
                        } else if(key == Input.Keys.LEFT) {
                            left = false;
                            cancelMovement = true;
                        } else if(key == Input.Keys.RIGHT) {
                            right = false;
                            cancelMovement = true;
                        }
                        
                        /* включить/выключить режим изменения поведения */
                        else if(key == Input.Keys.F5 && !programmingMode) {
                            click.play(0.4f);
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            
                            programmingMode = true;
                            msgSaved = false;
                            return true;
                        }
                        else if(key == Input.Keys.F5 && programmingMode) {
                            click.play(0.4f);
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(false);
                            
                            if(gameState == 2) sendMessage("/p " + behavior);
                            programmingMode = false;
                            return true;
                        }
                        
                        else if((!programmingMode && !writeMessage) && key == Input.Keys.X) {
                            click.play(0.4f);
                            select = true;
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            return true;
                        }
                        
                        /* написать сообщение */
                        else if(key == Input.Keys.F7 && gameState == 2 && !writeMessage) {
                            click.play(0.4f);
                            text = "";
                            writeMessage = true;
                            msgSaved = false;
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            return true;
                        } else if(key == Input.Keys.F7 && gameState == 2) {
                            click.play(0.4f);
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(false);
                            
                            if(!text.isEmpty()) {
                                sendMessage(text);
                                text = "";
                            }
                            
                            writeMessage = false;
                            
                            return true;
                        }
                    } catch(Exception ex) {}
                }
                
                return false;
            }
            
            public boolean touchUp(int x, int y, int ptr, int btn) {
                touch.set(x, y);
                viewport.unproject(touch);
                
                if(Gdx.app.getType() == Application.ApplicationType.Android && scene == S_GAME) {
                    up = false;
                    if(touch.x < 400) {
                        down  = false;
                        left  = false;
                        right = false;
                    }
                    return true;
                }
                return false;
            }
            
            public boolean touchDown(int x, int y, int ptr, int btn) {
                touch.set(x, y);
                viewport.unproject(touch);
                
                if(scene == S_START) {
                    click.play(1.0f);
                    rain = -10;
                    scene = S_MENU;
                    return true;
                } else if(scene == S_MENU) {
                    click.play(1.0f);
                    if(touch.x > 15 && touch.x < 940 && touch.y > 640 && touch.y < 680 && Gdx.app.getType() == Application.ApplicationType.Android) {
                        Gdx.input.setOnscreenKeyboardVisible(true);
                        return true;
                    } else if(touch.x > 940 && touch.y > 640 && touch.y < 680) {
                        return keyTyped('\n');
                    } else if(touch.x > 15 && touch.x < 1000 && touch.y > 100 && touch.y < 200) {
                        try {
                            String tmp = rpList[0];
                            for(int i = 1; i < rpList.length; i++)
                                rpList[i - 1] = rpList[i];
                            rpList[rpList.length - 1] = tmp;
                            return true;
                        } catch(Exception e) {};
                    } else if(touch.x > 15 && touch.x < 940 && touch.y > 100 && touch.y < 580) {
                        try {
                            selectedRp = (480 - ((int)touch.y - 100)) / 25;
                            return true;
                        } catch(Exception ex) {}
                    } else if(touch.x > 15 && touch.x < 265 && touch.y > 50 && touch.y < 80) {
                        try {
                            startGame();
                            return true;
                        } catch(Exception ex) { pleaseWait = false; }
                    } else if(touch.x > 275 && touch.x < (275 + 250) && touch.y > 50 && touch.y < 80) {
                        try {
                            gameState = 2;
                            text = "";
                            scene = S_LAN_MENU;
                            return true;
                        } catch(Exception ex) {}
                    } else if(touch.x > (275 + 260) && touch.x < (275 + 250 * 2) && touch.y > 50 && touch.y < 80) {
                        try {
                            gameState = 1;
                            text = "";
                            scene = S_LAN_MENU;
                            return true;
                        } catch(Exception ex) { pleaseWait = false; }
                    }
                } else if(scene == S_LAN_MENU) {
                    click.play(1.0f);
                    if(touch.x > 15 && touch.x < 940 && touch.y > 640 && touch.y < 680 && Gdx.app.getType() == Application.ApplicationType.Android) {
                        Gdx.input.setOnscreenKeyboardVisible(true);
                        return true;
                    } else if(touch.x > 940 && touch.y > 640 && touch.y < 680) {
                        return keyTyped('\n');
                    }
                }
                /* мобильное управление */
                else if(scene == S_GAME && Gdx.app.getType() == Application.ApplicationType.Android) {
                    if(touch.x > 0 && touch.x < 100 && touch.y > 100 && touch.y < 200) {
                        return keyDown(Input.Keys.LEFT);
                    } else if(touch.x > 200 && touch.x < 300 && touch.y > 100 && touch.y < 200) {
                        return keyDown(Input.Keys.RIGHT);
                    } else if(touch.x > 100 && touch.x < 200 && touch.y > 200 && touch.y < 300) {
                        return keyDown(Input.Keys.UP);
                    } else if(touch.x > 100 && touch.x < 200 && touch.y > 0 && touch.y < 100) {
                        return keyDown(Input.Keys.DOWN);
                    } else if(touch.x > 100 && touch.x < 200 && touch.y > 100 && touch.y < 200) {
                        return keyDown(Input.Keys.SPACE);
                    } else if(touch.x > 1090 && touch.x < 1190 && touch.y > 618 && touch.y < 718) {
                        return keyDown(Input.Keys.F1);
                    } else if(touch.x > 0 && touch.x < 100 && touch.y > 200 && touch.y < 300 && selected != -1) {
                        return keyDown(Input.Keys.Q);
                    } else if(touch.x > 200 && touch.x < 300 && touch.y > 200 && touch.y < 300 && selected != -1) {
                        return keyDown(Input.Keys.E);
                    } else if(touch.x > 0 && touch.x < 200 && touch.y > 600 && touch.y < 728) {
                        select = false;
                        selectNumber = "";
                        return keyUp(Input.Keys.X);
                    } else if(touch.x > 700 && touch.x < 1200 && touch.y > 0 && touch.y < 500) {
                        if(selected != -1 && !Blocks.isTank(map[selected]) && !Blocks.isHelicopter(map[selected])) {
                            return keyDown(Input.Keys.UP);
                        } else if(selected != -1 && (Blocks.isTank(map[selected]) || Blocks.isHelicopter(map[selected]))) {
                            return keyDown(Input.Keys.ENTER);
                        } else {
                            for(boolean val : forBoom)
                                if(val)
                                    return keyDown(Input.Keys.ENTER);
                            
                            return keyDown(Input.Keys.C);
                        }
                    } else if(touch.x > 770 && touch.x < 870 && touch.y > 618 && touch.y < 718) {
                        etcPage++;
                        return true;
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 0) {
                        return keyDown(Input.Keys.F4);
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 1) {
                        return keyDown(Input.Keys.V);
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 2) {
                        return keyDown(Input.Keys.M);
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 3) {
                        return keyDown(Input.Keys.F10);
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 4) {
                        return keyDown(Input.Keys.ESCAPE);
                    } else if(touch.x > 980 && touch.x < 1080 && touch.y > 618 && touch.y < 718 && etcPage == 5) {
                        return keyUp(Input.Keys.F5);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 0) {
                        return keyDown(Input.Keys.F);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 1) {
                        return keyDown(Input.Keys.B);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 2) {
                        return keyDown(Input.Keys.N);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 3) {
                        return keyDown(Input.Keys.F2);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 4) {
                        return keyDown(Input.Keys.F3);
                    } else if(touch.x > 875 && touch.x < 985 && touch.y > 618 && touch.y < 718 && etcPage == 5) {
                        return keyDown(Input.Keys.F6);
                    } else if(touch.x > 1090 && touch.x < 1190 && touch.y > 518 && touch.y < 618) {
                        return keyUp(Input.Keys.F7);
                    } else if(touch.x > 0 && touch.x < 760 && touch.y > 640)
                        return keyDown(Input.Keys.S);
                }
                
                return false;
            }
            
            public boolean keyTyped(char c) {
                if(scene == S_LAN_MENU) {
                    if(c < (char)32 && c != '\n' && c != '\r' && c != '\b' && c != (char)0x7F)
                        return false;
                    if(c == '\b' || c == (char)0x7F) {
                        if(text.length() == 0)
                            return false;
                        String newText = "";
                        char[] chars = text.toCharArray();
                        for(int i = 0; i < chars.length - 1; i++)
                            newText += "" + chars[i];
                        text = newText;
                        return true;
                    } else if(c != '\n' && c != '\r') {
                        text += "" + c;
                        return true;
                    } else {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        
                        if(text.isEmpty()) {
                            scene = S_MENU;
                            return true;
                        }
                        
                        connectUrl = text;
                        text = "";
                        try {
                            startGame();
                        } catch(Exception ex) { pleaseWait = false; }
                        
                        return true;
                    }
                } else if(scene == S_GAME && writeMessage) {
                    if(c < (char)32 && c != '\n' && c != '\r' && c != '\b' && c != (char)0x7F)
                        return false;
                    if(c == '\b' || c == (char)0x7F) {
                        if(text.length() == 0)
                            return false;
                        String newText = "";
                        char[] chars = text.toCharArray();
                        for(int i = 0; i < chars.length - 1; i++)
                            newText += "" + chars[i];
                        text = newText;
                        return true;
                    } else if(c != '\n' && c != '\r') {
                        text += "" + c;
                        return true;
                    } else {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        
                        if(!text.isEmpty()) {
                            sendMessage(text);
                            text = "";
                        }
                        
                        writeMessage = false;
                        
                        return true;
                    }
                } else if(scene == S_GAME && programmingMode) {
                    if(c < (char)32 && c != '\n' && c != '\r' && c != '\b' && c != (char)0x7F)
                        return false;
                    if(c == '\b' || c == (char)0x7F) {
                        if(behavior.length() == 0)
                            return false;
                        String newText = "";
                        char[] chars = behavior.toCharArray();
                        for(int i = 0; i < chars.length - 1; i++)
                            newText += "" + chars[i];
                        behavior = newText;
                        return true;
                    } else if(c != '\n' && c != '\r') {
                        behavior += "" + c;
                        return true;
                    } else {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        
                        if(gameState == 2) sendMessage("/p " + behavior);
                        programmingMode = false;
                        
                        return true;
                    }
                } else if(scene == S_GAME) {
                    if(select && c >= '0' && c <= '9') {
                        selectNumber += "" + c;
                        return true;
                    }
                    if(select && !selectNumber.isEmpty()) {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        try { currentBlock = (char)Integer.parseInt(selectNumber); } catch(Exception ex) {}
                        selectNumber = "";
                        new Thread() {
                            public void run() {
                                try { Thread.sleep(240); } catch(Exception e) {}
                                select = false;
                            }
                        }.start();
                        return true;
                    }
                    if(select && c > ' ') {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        currentBlock = c;
                        new Thread() {
                            public void run() {
                                try { Thread.sleep(240); } catch(Exception e) {}
                                select = false;
                            }
                        }.start();
                        return true;
                    }
                } else if(scene == S_MENU) {
                    if(c < (char)32 && c != '\n' && c != '\r' && c != '\b' && c != (char)0x7F)
                        return false;
                    if(c == '\b' || c == (char)0x7F) {
                        if(text.length() == 0)
                            return false;
                        String newText = "";
                        char[] chars = text.toCharArray();
                        for(int i = 0; i < chars.length - 1; i++)
                            newText += "" + chars[i];
                        text = newText;
                        return true;
                    } else if(c != '\n' && c != '\r') {
                        text += "" + c;
                        return true;
                    } else {
                        if(Gdx.app.getType() == Application.ApplicationType.Android)
                            Gdx.input.setOnscreenKeyboardVisible(false);
                        
                        if(text.isEmpty())
                            return true;
                        
                        pleaseWait = true;
                        new Thread() {
                            public void run() {
                                String mapDir;
                                if(!downloadFile(text + "/name", Gdx.files.external(root + "/name")))
                                    mapDir = "rp" + new Random().nextInt(1000000);
                                else
                                    mapDir = Gdx.files.external(root + "/name").readString("UTF-8").replace("\n", "") + "_" + new Random().nextInt(100000);
                                
                                if(!downloadFile(text + "/desc", Gdx.files.external(root + "/" + mapDir + "/desc"))) {
                                    text = "ERROR!!!"; pleaseWait = false;
                                    return;
                                }
                                
                                if(!downloadFile(text + "/map", Gdx.files.external(root + "/" + mapDir + "/map"))) {
                                    text = "ERROR!!!"; pleaseWait = false;
                                    return;
                                }
                                
                                if(!downloadFile(text + "/help", Gdx.files.external(root + "/" + mapDir + "/help"))) {
                                    text = "ERROR!!!"; pleaseWait = false;
                                    return;
                                }
                                
                                String[] lines = Gdx.files.external(root + "/" + mapDir + "/desc").readString("UTF-8").replace("\r", "").split("\n");
                                for(String line : lines) {
                                    String[] tokens = line.split(" ");
                                    for(String tkn : tokens) {
                                        if(tkn.startsWith("texture:")) {
                                            if(!downloadFile(text + "/images/" + tkn.split(":")[1], Gdx.files.external(root + "/" + mapDir + "/images/" + tkn.split(":")[1]))) {
                                                text = "ERROR!!!"; pleaseWait = false;
                                                return;
                                            }
                                            
                                            if(tkn.split(":").length > 2 && !downloadFile(text + "/images/" + tkn.split(":")[2], Gdx.files.external(root + "/" + mapDir + "/images/" + tkn.split(":")[2]))) {
                                                text = "ERROR!!!"; pleaseWait = false;
                                                return;
                                            }
                                        }
                                    }
                                }
                                
                                Gdx.files.external(root + "/rp_list.txt").writeString(mapDir + "\n" + (Gdx.files.external(root + "/rp_list.txt").exists() ? Gdx.files.external(root + "/rp_list.txt").readString("UTF-8") : ""), false, "UTF-8");
                                text = "(done)";
                                pleaseWait = false;
                                updateRpList();
                            }
                        }.start();
                        
                        return true;
                    }
                }
                
                return false;
            }
        });
        
        touch = new Vector2();
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        viewport = new FitViewport(1200, 728);
        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
        font.setFixedWidthGlyphs(" ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮёйцукенгшщзхъфывапролджэячсмитьбю█№—");
        font.setUseIntegerPositions(false);
        font.setColor(Color.WHITE);
        
        scene = S_START;
        updateRpList();
        Blocks.init();
        
        rain = 0;
        
        /* дождь */
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        if(rain >= 0) {
                            if(rain < 4)
                                rain++;
                            else
                                rain = 0;
                        }
                        
                        Thread.sleep(25);
                    } catch(Exception e) {
                        //e.printStackTrace();
                    }
                }
            }
        }.start();
        
        if(rpList != null)
            for(String name : rpList)
                if(name.equals("game"))
                    return;
        
        if(Gdx.files.internal("game").isDirectory()) {
            Gdx.files.internal("game").copyTo(Gdx.files.external(root));
            Gdx.files.external(root + "/rp_list.txt").writeString("game\n", true, "UTF-8");
            updateRpList();
        }
    }
    
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }
    
    public void pause() {
        ph = false;
    }

    public void resume() {
        ph = true;
    }

    public void dispose() {}
    
    public void render() {
        boolean noEnd = false, noShapeEnd = false;
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shape.setProjectionMatrix(viewport.getCamera().combined);
        
        if(scene == S_START || scene == S_MENU || scene == S_LAN_MENU) {
            batch.begin(); noEnd = true;
            batch.draw((scene == S_START && rain >= 0) ? bgTextures[1 + rain] : bgTextures[0], 0, 85, 1200, 650);
            batch.end(); noEnd = false;
            
            if(scene != S_START) {
                shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                shape.setColor(new Color(0f, 1f, 0f, 1f));
                shape.rect(940, 640, 60, 30);
                if(scene != S_LAN_MENU) {
                    shape.setColor(new Color(0f, 0.7f, 0f, 1f));
                    shape.rect(15, 50, 250, 30);
                    shape.rect(275, 50, 250, 30);
                    shape.rect(275 + 260, 50, 250, 30);
                }
                shape.end(); noShapeEnd = false;
                
                batch.begin(); noEnd = true;
                batch.draw(blackTexture, 15, 640, 920, 30);
                if(scene != S_LAN_MENU)
                    batch.draw(blackTexture, 15, 100, 1000, 480);
                
                font.getData().setScale(0.56f);
                font.draw(batch, (scene != S_LAN_MENU ? "HTTP-адрес для загрузки ресурспака" : "Адрес и порт сервера (<addr>:<port>)"), 15, 700);
                if(pleaseWait)
                    font.draw(batch, "пожалуйста, подождите...", 920, 705);
                font.draw(batch, text + "█", 20, 660);
                if(scene != S_LAN_MENU) {
                    font.draw(batch, "Ресурспаки", 15, 620);
                    try {
                        for(int j = 570, i = 0; i < rpList.length && i < 16; i++) {
                            font.draw(batch, (i == selectedRp ? "> " : "") + rpList[i], 20, j);
                            j -= 25;
                        }
                    } catch(Exception ex) {}
                    font.draw(batch, "Одиночная игра", 25, 75);
                    font.draw(batch, "Подключиться", 285, 75);
                    font.draw(batch, "Создать сервер", 275 + 270, 75);
                }
                batch.end(); noEnd = false;
            }
            
        } else if(scene == S_GAME) {
            if(selected != -1) cameraStart = selected - (12 + 6 * WIDTH);
            final int camPos = cameraStart;
            /* рисование объектов
               ================== */
            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
            shape.setColor(new Color(bgColorRed * (1f / 255), bgColorGreen * (1f / 255), bgColorBlue * (1f / 255), 1f));
            int iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(iii < map.length && iii >= 0)
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                    } catch(Exception ex) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            shape.end(); noShapeEnd = false;
            shape.begin(ShapeRenderer.ShapeType.Line); noShapeEnd = true;
            shape.setColor(bgColorRed < 195 && bgColorGreen < 195 && bgColorBlue < 195 ? new Color((bgColorRed + 60) * (1f / 255), (bgColorGreen + 60) * (1f / 255), (bgColorBlue + 60) * (1f / 255), 1f) : new Color((bgColorRed - 60) * (1f / 255), (bgColorGreen - 60) * (1f / 255), (bgColorBlue - 60) * (1f / 255), 1f));
            iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(iii < map.length && iii >= 0) {
                            if(ui) {
                                try {
                                    shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                                } catch(IllegalArgumentException e) {}
                            }
                        }
                    } catch(Exception ex) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            shape.end(); noShapeEnd = false;
            iii = camPos;
            batch.begin(); noEnd = true;
            iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(rain >= 0 && iii < map.length && iii >= 0)
                            batch.draw(rainTextures[rain], ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        
                        if(map[iii] == ':')
                            batch.draw(leftTexture, ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        else if(map[iii] == ';')
                            batch.draw(rightTexture, ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        /* блок мода */
                        else if(Blocks.getTextureX(map[iii]) == Blocks.defaultX && Blocks.getTextureY(map[iii]) == Blocks.defaultY &&
                                Blocks.getTextureWidth(map[iii]) <= Blocks.defaultW && Blocks.getTextureHeight(map[iii]) <= Blocks.defaultH && !Blocks.isUnknown(map[iii]))
                            batch.draw(((Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.' && nextTexture) || (iii == selected && shoot)) ? Blocks.getTexture2(map[iii]) : Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]) - 1, 728 - i * Blocks.defaultH - Blocks.getTextureHeight(map[iii]) - Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]) + 2, Blocks.getTextureHeight(map[iii]) + 1);
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            batch.end(); noEnd = false;
            iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(map[iii] == '~' || map[iii] == ',') {
                            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                            
                            shape.setColor(new Color(0.03f, 0.03f, 0.03f, 1f));
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            shape.setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
                            shape.rect(ii * Blocks.defaultW + Blocks.defaultW / 3, 728 - ((i + 1) * Blocks.defaultH - Blocks.defaultH / 3), Blocks.defaultW / 3, Blocks.defaultH / 3);
                            
                            shape.end(); noShapeEnd = false;
                            
                            if(ui) {
                                batch.begin(); noEnd = true;
                                font.getData().setScale(0.4f);
                                font.draw(batch, (map[iii] == ',' ? "<" : ">"), ii * Blocks.defaultW + 5, 728 - (i * Blocks.defaultH + 5));
                                batch.end(); noEnd = false;
                            }
                        } else if(map[iii] == 'b') {
                            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                            
                            shape.setColor(new Color(0.03f, 0.03f, 0.03f, 1f));
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            
                            shape.end(); noShapeEnd = false;
                        }
                    } catch(Exception e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            
            
            batch.begin(); noEnd = true;
            
            iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(map[iii] == 'f') {
                            batch.draw(fireTexture, ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3 - (nextTexture ? Blocks.defaultH / 2 : 0));
                            if(rain >= 0 && selected == -1)
                                map[iii] = 'b';
                        } else if(map[iii] == 'F')
                            batch.draw(fire2Texture, ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3 - (nextTexture ? Blocks.defaultH / 2 : 0));
                        else if((int)map[iii] >= (int)'0' && (int)map[iii] <= (int)'9')
                            batch.draw(boomTextures[map[iii] - '0'], ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3);
                        else if(map[iii] == 'p') {
                            batch.draw(portal0Texture, ii * Blocks.defaultW, 728 - ((i + 1) * Blocks.defaultH), Blocks.defaultW, Blocks.defaultH * 2);
                        } else if(map[iii] == 'P') {
                            batch.draw(portal1Texture, ii * Blocks.defaultW, 728 - ((i + 1) * Blocks.defaultH), Blocks.defaultW, Blocks.defaultH * 2);
                        } else if(Blocks.isUnknown(map[iii])) {
                            font.getData().setScale(2.0f);
                            font.draw(batch, "" + map[iii], ii * Blocks.defaultW + Blocks.defaultW / 4, 728 - (i + 1) * Blocks.defaultH);
                        }
                        /* блок мода */
                        else if(Blocks.getTextureX(map[iii]) != Blocks.defaultX || Blocks.getTextureY(map[iii]) != Blocks.defaultY ||
                                Blocks.getTextureWidth(map[iii]) > Blocks.defaultW || Blocks.getTextureHeight(map[iii]) > Blocks.defaultH) {
                            batch.draw(((Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.' && nextTexture) || (iii == selected && shoot)) ? Blocks.getTexture2(map[iii]) : Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]), 728 - i * Blocks.defaultH - Blocks.getTextureHeight(map[iii]) - Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]), Blocks.getTextureHeight(map[iii]));
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            /*********************************/
            
            iii = camPos;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        try {
                            if(!lightMap[iii] && ((!lightMap[iii - 1] && !lightMap[iii + 1] && !lightMap[iii - WIDTH] && !lightMap[iii + WIDTH]) || map[iii] == '.'))
                                batch.draw(((lightMap[iii - 1] || lightMap[iii + 1] || lightMap[iii - WIDTH] || lightMap[iii + WIDTH]) ? black2Texture : blackTexture), ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                        } catch(Exception e) {}
                        
                        if(ui) {
                            if(forBoom[iii] && !programmingMode) {
                                batch.draw(redTexture, (ii - 1) * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW * 3, Blocks.defaultH);
                                batch.draw(redTexture, ii * Blocks.defaultW, 728 - (i + 2) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH * 3);
                            } else if(behaviorSelected2 != -1 && iii == behaviorSelected2 && programmingMode)
                                batch.draw(redTexture, ii * Blocks.defaultW, 728 - i * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            else if((gameState == 2 && iii == adminPos) || (gameState == 1 && iii == pos2))
                                batch.draw(pricelTexture, ii * Blocks.defaultW + Blocks.defaultW / 2 - 8, 728 - i * Blocks.defaultH - Blocks.defaultH / 2 - 5, 16, 8);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            
            if(ui) {
                batch.draw(pricelTexture, 1200 / 2 + 16, 728 - (700 / 2 - 20), 16, 8);
                batch.draw(vignetteTexture, 0, 0, 1200, 728);
                
                font.getData().setScale(0.5f);
                font.setColor(Color.YELLOW);
                if(!fill)
                    font.draw(batch, "" + (System.currentTimeMillis() - startTime) / 60000 + " min", 15, 728 - 20);
                else
                    font.draw(batch, "Выберите сторону...", 15, 728 - 20);
                font.setColor(Color.WHITE);
                
                if(programmingMode) {
                    font.getData().setScale(0.45f);
                    font.draw(batch, "Режим программирования", 15, 728 - 60);
                    char[] behavior2 = behavior.toCharArray();
                    
                    iii = 0;
                    int i = 0, ii = 0;
                    loop: for(; i < 8; i++) {
                        for(ii = 0; ii < 140; ii++, iii++) {
                            try {
                                font.draw(batch, "" + behavior2[iii], 15 + ii * 8, 728 - (80 + i * 20));
                            } catch(ArrayIndexOutOfBoundsException e) {
                                break loop;
                            }
                        }
                    }
                    font.draw(batch, "█", 15 + ii * 8, 728 - (80 + i * 20));
                }
                
                try {font.draw(batch, "" + selectedBlockAddr() + "  " + map[selectedBlockAddr()] + " [" + (select ? ("?" + (selectNumber.isEmpty() ? "?" : selectNumber) + "?") : currentBlock) + "]", 15, 728 - 40);} catch(ArrayIndexOutOfBoundsException e) {}
                
                font.getData().setScale(0.7f);
                
                if(slow)
                    font.draw(batch, "~", 1160, 728 - 20);
                
                if(Gdx.app.getType() == Application.ApplicationType.Android) {
                    if(selected != -1) {
                        batch.draw(qBtnTexture, 0, 200, 100, 100);
                        batch.draw(eBtnTexture, 200, 200, 100, 100);
                    }
                    
                    batch.draw(leftBtnTexture, 0, 100, 100, 100);
                    batch.draw(rightBtnTexture, 200, 100, 100, 100);
                    batch.draw(selectBtnTexture, 100, 100, 100, 100);
                    
                    batch.draw(moreBtnTexture, 770, 618, 100, 100);
                    if(gameState == 2)
                        batch.draw(chatBtnTexture, 1090, 518, 100, 100);
                    
                    if(etcPage == 0) {
                        batch.draw(fillBtnTexture, 980, 618, 100, 100);
                        if(!fill) batch.draw(black2Texture, 980, 618, 100, 100);
                        batch.draw(fireBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 1) {
                        batch.draw(fire2BtnTexture, 980, 618, 100, 100);
                        batch.draw(boomBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 2) {
                        batch.draw(delBoomBtnTexture, 980, 618, 100, 100);
                        batch.draw(nightBtnTexture, 875, 618, 100, 100);
                        if(!night) batch.draw(black2Texture, 875, 618, 100, 100);
                    } else if(etcPage == 3) {
                        batch.draw(rainBtnTexture, 980, 618, 100, 100);
                        if(rain < 0) batch.draw(black2Texture, 980, 618, 100, 100);
                        batch.draw(slowBtnTexture, 875, 618, 100, 100);
                        if(!slow) batch.draw(black2Texture, 875, 618, 100, 100);
                    } else if(etcPage == 4) {
                        batch.draw(uiBtnTexture, 980, 618, 100, 100);
                        batch.draw(bgColorBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 5) {
                        batch.draw(programmingModeBtnTexture, 980, 618, 100, 100);
                        if(!programmingMode) batch.draw(black2Texture, 980, 618, 100, 100);
                        batch.draw(phBtnTexture, 875, 618, 100, 100);
                        if(ph) batch.draw(black2Texture, 875, 618, 100, 100);
                    } else etcPage = 0;
                }
                
                font.getData().setScale(0.5f);
                
                if(gameState == 2 && writeMessage) font.draw(batch, text + "█", 15, 728 - 60);
                else if((gameState == 1 || gameState == 2) && !programmingMode && !message.startsWith("/")) font.draw(batch, message, 15, 728 - 60);
                
                if(msgSaved) font.draw(batch, "сохранено", 15, 728 - 80);
                
                if(help && !programmingMode) {
                    batch.draw(blackTexture, 0, 0, 1200, 728);
                    
                    for(int i = helpIndex; i < helpMessage.length; i++)
                        font.draw(batch, helpMessage[i], 15, 728 - (i + 1 - helpIndex) * 18);
                    
                }
                
                if(Gdx.app.getType() == Application.ApplicationType.Android) {
                    batch.draw(helpBtnTexture, 1090, 618, 100, 100);
                    batch.draw(upBtnTexture, 100, 200, 100, 100);
                    batch.draw(downBtnTexture, 100, 0, 100, 100);
                }
            }
            batch.end(); noEnd = false;
        }
    }
    
    private boolean downloadFile(String url, FileHandle fileToSave) {
        retval = false;
        done = false;
        Net.HttpRequest rq = rqbuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        rq.setFollowRedirects(true);
        
        Gdx.net.sendHttpRequest(rq, new Net.HttpResponseListener() {
            public void cancelled() { done = true; }
            public void failed(Throwable t) { done = true; }
            
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if(httpResponse.getStatus().getStatusCode() < 400) {
                    fileToSave.writeBytes(httpResponse.getResult(), false);
                    retval = true;
                }
                done = true;
            }
        });
        
        while(!done) {
            try { Thread.sleep(20); } catch(Exception ex) {}
        }
        
        return retval;
    }
    
    private void updateRpList() {
        if(!Gdx.files.external(root + "/rp_list.txt").exists()) {
            rpList = null;
            return;
        }
        
        rpList = Gdx.files.external(root + "/rp_list.txt").readString("UTF-8").split("\n");
        for(int i = 0; i < rpList.length; i++) {
            if(!Gdx.files.external(root + "/" + rpList[i] + "/map").exists())
                rpList[i] = "";
        }
    }
    
    
    /* отправить сообщение */
    private void sendMessage(String msg) {
        if(!cmsg.isEmpty())
            cmsg += ";;;";
        cmsg += msg.replace("$$", "" + selectedBlockAddr());
    }
    
    private void setBlock(int addr, char blk) {
        map[addr] = blk;
        if(gameState == 2) sendMessage("/c " + addr + " " + blk);
    }
    
    private void fire(int addr) {
        if(gameState != 2) {
            final int saved = addr;
            if(saved >= WIDTH && saved < map.length - WIDTH && !Blocks.isFireResistant(map[saved])) {
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
                                map[saved] = new Random().nextInt(3) == 1 ? 'b' : '.';
                        } catch(Exception e) {}
                    }
                }.start();
            }
        } else {
            sendMessage("/f " + addr);
        }
    }
    
    private void fire2(int addr) {
        if(gameState != 2) {
            final int saved = addr;
            if((!(saved >= WIDTH && saved < map.length - WIDTH)) || Blocks.isWater(map[saved]))
                return;
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
    
    private void boom(int addr) {
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
                        
                        if(!(map[saved - 1 - WIDTH] >= '0' && map[saved - 1 - WIDTH] <= '9') && !Blocks.isStrong(map[saved - 1 - WIDTH]) && map[saved - 1 - WIDTH] != '.') {
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
                    //ex.printStackTrace();
                }
            }
        }.start();
        
        if(gameState == 2) sendMessage("/b " + addr);
    }
    
    /* возвращает адрес блока, на который указывает прицел */
    private int selectedBlockAddr() {
        return cameraStart + 12 + 6 * WIDTH;
    }
    
    private long startTime = 0;
    
    private void startGame() {
        pleaseWait = true;
        
        /* загрузка карты, поведения, мода и текста помощи */
        
        String[] lines;
        
        lines = Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").readString("UTF-8").split("\n");
        behavior = lines[0];
        bgColorRed = Integer.parseInt(lines[1].split(" ")[0]);
        bgColorGreen = Integer.parseInt(lines[1].split(" ")[1]);
        bgColorBlue = Integer.parseInt(lines[1].split(" ")[2]);
        int ii = 0;
        for(int j = 2; j < HEIGHT + 2; j++) {
            char[] line = lines[j].toCharArray();
            for(int i = 0; i < WIDTH; i++)
                map[ii++] = line[i];
        }
        
        helpMessage = Gdx.files.external(root + "/" + rpList[selectedRp] + "/help").readString("UTF-8").split("\n");
        
        lines = Gdx.files.external(root + "/" + rpList[selectedRp] + "/desc").readString("UTF-8").split("\n");
        for(String line : lines) {
            String[] tokens = line.replace("  ", " ").replace("\t", "").split(" ");
            
            if(tokens[0].startsWith("#") || tokens[0].isEmpty())
                continue;
            else if(tokens[0].equals("type:simple")) {
                char c = '\0';
                char leftC = c;
                char rightC = c;
                Texture texture = null;
                boolean isFallen = false;
                boolean isStrong = false;
                boolean isSticky = false;
                boolean isFireResistant = false;
                boolean isEraser = false;
                boolean isTranslucent = false;
                int light = 0;
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
                        texture = new Texture(Gdx.files.external(root + "/" + rpList[selectedRp] + "/images/" + tokens[i].split(":")[1]));
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
                    } else if(tokens[i].equals("translucent")) {
                        isTranslucent = true;
                    } else if(tokens[i].startsWith("light:")) {
                        light = Integer.parseInt(tokens[i].split(":")[1]);
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
                    isFireResistant, isEraser, isTranslucent, light, x, y, w, h);
            } else if(tokens[0].equals("type:tank") || tokens[0].equals("type:helicopter")) {
                char c = '\0';
                char leftC = c;
                char rightC = c;
                Texture texture = null;
                Texture texture2 = null;
                boolean isStrong = false;
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
                        texture  = new Texture(Gdx.files.external(root + "/" + rpList[selectedRp] + "/images/" + tokens[i].split(":")[1]));
                        texture2 = new Texture(Gdx.files.external(root + "/" + rpList[selectedRp] + "/images/" + tokens[i].split(":")[2]));
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
                    Blocks.addTank(c, leftC, rightC, texture, texture2, isStrong, x, y, w, h);
                else
                    Blocks.addHelicopter(c, leftC, rightC, texture, texture2, isStrong, x, y, w, h);
            } else if(tokens[0].equals("type:water")) {
                char c = '\0';
                int x = Blocks.defaultX;
                int y = Blocks.defaultY;
                int w = Blocks.defaultW;
                int h = Blocks.defaultH;
                Texture texture = null;
                
                for(int i = 1; i < tokens.length; i++) {
                    if(tokens[i].startsWith("c:")) {
                        c = tokens[i].toCharArray()[2];
                    } else if(tokens[i].startsWith("texture:")) {
                        texture = new Texture(Gdx.files.external(root + "/" + rpList[selectedRp] + "/images/" + tokens[i].split(":")[1]));
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
                
                Blocks.addWaterType(c, texture, x, y, w, h);
            }
        }
        
        startTime = System.currentTimeMillis();
        pleaseWait = false;
        scene = S_GAME;
        
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        if(selected == -1) Thread.sleep(50);
                        else if(selectedNewPos != -1 && selectedNewPos != selected) {
                            int saved = selectedNewPos;
                            selectedNewPos = -1;
                            map[saved] = map[selected];
                            if(Blocks.isEraser(map[selected])) setBlock(selected, '.');
                            else map[selected] = '.';
                            selected = saved;
                        }
                        Thread.sleep(2);
                    } catch(Exception ex) {}
                }
            }
        }.start();
        
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        while(selected != -1 && !jump && selected > WIDTH && !Blocks.isHelicopter(map[selected]) && !Blocks.isEraser(map[selected]) && map[selected + WIDTH] == '.' && ph) {
                            selectedNewPos = selected + WIDTH;
                            Thread.sleep(!slow ? 30 : 60);
                        }
                        
                        if(!ui && (select || programmingMode || writeMessage))
                            ui = true;
                        if(help && (select || programmingMode || writeMessage))
                            help = false;
                        
                        nextTexture = !nextTexture;
                        
                        Thread.sleep(40);
                    } catch(Exception e) {
                        //e.printStackTrace();
                    }
                }
            }
        }.start();
        
        /* перемещение */
        new Thread() {
            public void run() {
                boolean mvWait = false;
                
                while(true) {
                    try {
                        while(!(up || down || left || right)) {
                            mvWait = true;
                            Thread.sleep(500);
                        }
                        if(mvWait) {
                            mvWait = false;
                            Thread.sleep(250);
                        }
                        
                        if(cancelMovement) {
                            cancelMovement = false;
                            continue;
                        }
                        
                        Thread.sleep(50);
                        
                        int add = 0;
                        
                        if(up && selected != -1 && (map[selected - WIDTH] == '.' || Blocks.isWater(map[selected - WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            add -= WIDTH;
                        }
                        if(down && selected != -1 && (map[selected + WIDTH] == '.' || Blocks.isWater(map[selected + WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            add += WIDTH;
                        }
                        if(left && selected != -1 && (map[selected - 1] == '.' || Blocks.isWater(map[selected - 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            add--;
                        }
                        if(right && selected != -1 && (map[selected + 1] == '.' || Blocks.isWater(map[selected + 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                            add++;
                        }
                        
                        if(selected != -1) selectedNewPos = selected + add;
                        
                        if(up && selected == -1) cameraStart -= WIDTH;
                        if(down && selected == -1) cameraStart += WIDTH;
                        if(left && selected == -1) cameraStart--;
                        if(right && selected == -1) cameraStart++;
                    } catch(Exception ex) {}
                }
            }
        }.start();
        
        /* обновление света */
        new Thread() {
            public void run() {
                boolean[] tmp = new boolean[map.length];
                while(true) {
                    try {
                        for(int i = 0; i < tmp.length; i++)
                            tmp[i] = false;
                        
                        /* общее освещение */
                        if(!night) {
                            for(int i = WIDTH; i < WIDTH * 2; i++) {
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH + 1) {
                                    tmp[ii] = true;
                                    if(!Blocks.isTranslucent(map[ii + WIDTH]) && !Blocks.isTranslucent(map[ii + 1]))
                                        break;
                                }
                            }
                            
                            for(int i = WIDTH * 2 - 1; i >= WIDTH; i--) {
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH - 1) {
                                    tmp[ii] = true;
                                    if(!Blocks.isTranslucent(map[ii + WIDTH]) && !Blocks.isTranslucent(map[ii - 1]))
                                        break;
                                }
                            }
                            
                            for(int i = WIDTH; i < WIDTH * 2; i++) {
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH)
                                    tmp[ii] = true;
                            }
                        }
                        
                        /* свет от блоков */
                        for(int i = 0; i < lightMap.length; i++) {
                            if(Blocks.getLight(map[i]) > 0) {
                                final int max = Blocks.getLight(map[i]);
                                for(float angle = 0.0f; angle < 6.28f; angle += 0.01f) {
                                    final float eY = (float)Math.sin(angle);
                                    final float eX = (float)Math.cos(angle);
                                    try {
                                        for(float dist = 0.1f; dist < max && (i == i + (int)(eX * dist) + (int)(eY * dist) * WIDTH || Blocks.isTranslucent(map[i + (int)(eX * dist) + (int)(eY * dist) * WIDTH])); dist += 0.1f)
                                            tmp[i + (int)(eX * dist) + (int)(eY * dist) * WIDTH] = true;
                                    } catch(Exception ex) {}
                                }
                            }
                        }
                        
                        for(int i = 0; i < lightMap.length; i++) {
                            lightMap[i] = tmp[i];
                        }
                        
                        Thread.sleep(200);
                    } catch(Exception e) {}
                }
            }
        }.start();
        
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
                            //e.printStackTrace();
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
                                    /* работа порталов */
                                    if(map[i] == 'p') {
                                        for(int ii = 0; ii < map.length - 1; ii++) {
                                            if(map[ii] == 'P') {
                                                if((i + 1) == selected) { selectedNewPos = ii - 1; Thread.sleep(50); }
                                                else if((i - 1) == selected) { selectedNewPos = ii + 1; Thread.sleep(50); }
                                                else if((i - WIDTH) == selected) { selectedNewPos = ii + WIDTH; Thread.sleep(50); }
                                                
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
                                    else if(map[i] == '~' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH + 1] == '.') {
                                        map[i - WIDTH + 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    } else if(map[i] == '~' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH + 1] != '.' && map[i - WIDTH * 2 + 1] == '.' && map[i - WIDTH * 2] == '.') {
                                        map[i - WIDTH * 2 + 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    } else if(map[i] == ',' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH - 1] == '.') {
                                        map[i - WIDTH - 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    } else if(map[i] == ',' && map[i - WIDTH] != '.' && !Blocks.isWater(map[i - WIDTH]) && map[i - WIDTH - 1] != '.' && map[i - WIDTH * 2 - 1] == '.' && map[i - WIDTH * 2] == '.') {
                                        map[i - WIDTH * 2 - 1] = map[i - WIDTH];
                                        map[i - WIDTH] = '.';
                                        Thread.sleep(!slow ? 200 : 500);
                                    }
                                } catch(Exception e) {
                                    //e.printStackTrace();
                                }
                            }
                        }
                        try {Thread.sleep(80);} catch(Exception e) {}
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
                                if((map[i] == ':' || map[i] == ';') && (Blocks.isSticky(map[i - 1]) || Blocks.isSticky(map[i + 1]) || Blocks.isSticky(map[i - WIDTH])) || Blocks.isSticky(map[i + WIDTH])) {
                                    continue;
                                } else if(map[i] == ':' && map[i - 1] == '.') {
                                    map[i] = '.';
                                    map[i - 1] = ':';
                                } else if(map[i] == ':' && map[i - 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH - 1] == '.') {
                                    map[i] = '.';
                                    map[i - WIDTH - 1] = ':';
                                } else if(map[i] == ';' && map[i + 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH + 1] == '.') {
                                    map[i] = '.';
                                    map[i - WIDTH + 1] = ';';
                                } else if(map[i] == ':') {
                                    map[i] = ';';
                                } else if(map[i] == ';' && map[i + 1] == '.') {
                                    map[i] = '.';
                                    map[i + 1] = ';';
                                    i++;
                                } else if(map[i] == ';') {
                                    map[i] = ':';
                                }
                            } catch(Exception e) {}
                        }
                        try {Thread.sleep(!slow ? 400 : 1000);} catch(Exception e) {}
                    }
                }
            }.start();
            
            /* выполнение поведения */
            for(int thread = 0; thread < 4; thread++) {
                final int thrd = thread;
                new Thread() {
                    public void run() {
                        int behaviorSelected = 0;
                        while(true) {
                            try {
                                if(!behavior.isEmpty() && ph && !programmingMode) {
                                    if(behavior.split(" \\| ").length <= thrd) {
                                        Thread.sleep(500);
                                        continue;
                                    }
                                    
                                    String[] behaviorSplitted = behavior.split(" \\| ")[thrd].split(" ");
                                    
                                    for(int i = 0; i < behaviorSplitted.length; i++) {
                                        behaviorSplitted[i] = behaviorSplitted[i].replace("selected", "" + (selected != -1 ? selected : selectedBlockAddr()));
                                        
                                        if(behaviorSplitted[i].isEmpty())
                                            continue;
                                        /* sel */
                                        else if(behaviorSplitted[i].split(":")[0].equals("sel") && behaviorSelected == 0) {
                                            behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                        }
                                        /* no_sel */
                                        else if(behaviorSplitted[i].equals("no_sel")) {
                                            behaviorSelected = 0;
                                        }
                                        /* find */
                                        else if(behaviorSplitted[i].split(":")[0].equals("find") && behaviorSelected == 0) {
                                            char need = behaviorSplitted[i].split(":")[1].toCharArray()[0];
                                            
                                            findLoop:
                                            while(!behavior.isEmpty() && ph && !programmingMode) {
                                                for(int j = 1; j < map.length; j++) {
                                                    if(map[j] == need) {
                                                        behaviorSelected = j;
                                                        break findLoop;
                                                    }
                                                }
                                                Thread.sleep(40);
                                            }
                                        }
                                        /* findn */
                                        else if(behaviorSplitted[i].split(":")[0].equals("findn") && behaviorSelected == 0) {
                                            char need = (char)Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                            
                                            findLoop:
                                            while(!behavior.isEmpty() && ph && !programmingMode) {
                                                for(int j = 1; j < map.length; j++) {
                                                    if(map[j] == need) {
                                                        behaviorSelected = j;
                                                        break findLoop;
                                                    }
                                                }
                                                Thread.sleep(40);
                                            }
                                        }
                                        /* перемещение */
                                        else if(behaviorSplitted[i].equals("up") && behaviorSelected != 0) {
                                            map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected -= WIDTH;
                                        } else if(behaviorSplitted[i].equals("down") && behaviorSelected != 0) {
                                            map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected += WIDTH;
                                        } else if(behaviorSplitted[i].equals("right") && behaviorSelected != 0) {
                                            map[behaviorSelected + 1] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected++;
                                        } else if(behaviorSplitted[i].equals("left") && behaviorSelected != 0) {
                                            map[behaviorSelected - 1] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected--;
                                        } else if(behaviorSplitted[i].equals("up:lift") && behaviorSelected != 0) {
                                            map[behaviorSelected - WIDTH * 2] = map[behaviorSelected - WIDTH];
                                            map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected -= WIDTH;
                                        } else if(behaviorSplitted[i].equals("down:lift") && behaviorSelected != 0) {
                                            map[behaviorSelected + WIDTH * 2] = map[behaviorSelected + WIDTH];
                                            map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected += WIDTH;
                                        } else if(behaviorSplitted[i].equals("right:lift") && behaviorSelected != 0) {
                                            map[behaviorSelected + 2] = map[behaviorSelected + 1];
                                            map[behaviorSelected + 1] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected++;
                                        } else if(behaviorSplitted[i].equals("left:lift") && behaviorSelected != 0) {
                                            map[behaviorSelected - 2] = map[behaviorSelected - 1];
                                            map[behaviorSelected - 1] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected--;
                                        } else if(behaviorSplitted[i].equals("up:copy") && behaviorSelected != 0) {
                                            map[behaviorSelected - WIDTH] = map[behaviorSelected];
                                            behaviorSelected -= WIDTH;
                                        } else if(behaviorSplitted[i].equals("down:copy") && behaviorSelected != 0) {
                                            map[behaviorSelected + WIDTH] = map[behaviorSelected];
                                            behaviorSelected += WIDTH;
                                        } else if(behaviorSplitted[i].equals("right:copy") && behaviorSelected != 0) {
                                            map[behaviorSelected + 1] = map[behaviorSelected];
                                            behaviorSelected++;
                                        } else if(behaviorSplitted[i].equals("left:copy") && behaviorSelected != 0) {
                                            map[behaviorSelected - 1] = map[behaviorSelected];
                                            behaviorSelected--;
                                        }
                                        /* fire, fire2 и boom */
                                        else if(behaviorSplitted[i].split(":")[0].equals("fire") && behaviorSelected != 0) {
                                            fire(behaviorSelected);
                                        } else if(behaviorSplitted[i].split(":")[0].equals("fire2") && behaviorSelected != 0) {
                                            fire2(behaviorSelected);
                                        } else if(behaviorSplitted[i].split(":")[0].equals("boom") && behaviorSelected != 0) {
                                            boom(behaviorSelected);
                                        }
                                        /* set */
                                        else if(behaviorSplitted[i].split(":")[0].equals("set") && behaviorSelected != 0) {
                                            map[behaviorSelected] = behaviorSplitted[i].split(":")[1].toCharArray()[0];
                                        }
                                        /* setn */
                                        else if(behaviorSplitted[i].split(":")[0].equals("setn") && behaviorSelected != 0) {
                                            map[behaviorSelected] = (char)Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                        }
                                        /* ~<...> */
                                        else if(behaviorSplitted[i].startsWith("~")) {
                                            Thread.sleep(!slow ? Integer.parseInt(behaviorSplitted[i].replace("~", "")) : Integer.parseInt(behaviorSplitted[i].replace("~", "")) * 2);
                                        }
                                        /* wait */
                                        else if(behaviorSplitted[i].equals("wait:up") && behaviorSelected != 0) {
                                            while(map[behaviorSelected - WIDTH] == '.' && (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait:down") && behaviorSelected != 0) {
                                            while(map[behaviorSelected + WIDTH] == '.' && (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait:right") && behaviorSelected != 0) {
                                            while(map[behaviorSelected + 1] == '.' && (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait:left") && behaviorSelected != 0) {
                                            while(map[behaviorSelected - 1] == '.' && (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait:light") && behaviorSelected != 0) {
                                            while(!lightMap[behaviorSelected] &&
                                                  !lightMap[behaviorSelected - 1] &&
                                                  !lightMap[behaviorSelected + 1] &&
                                                  !lightMap[behaviorSelected - WIDTH] &&
                                                  !lightMap[behaviorSelected + WIDTH] &&
                                                  (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait:dark") && behaviorSelected != 0) {
                                            while((lightMap[behaviorSelected] ||
                                                  lightMap[behaviorSelected - 1] ||
                                                  lightMap[behaviorSelected + 1] ||
                                                  lightMap[behaviorSelected - WIDTH] ||
                                                  lightMap[behaviorSelected + WIDTH]) &&
                                                  (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        } else if(behaviorSplitted[i].equals("wait") && behaviorSelected != 0) {
                                            while(map[behaviorSelected - 1] == '.'     &&
                                                  map[behaviorSelected + 1] == '.'     &&
                                                  map[behaviorSelected + WIDTH] == '.' &&
                                                  map[behaviorSelected - WIDTH] == '.' && (!behavior.isEmpty() && ph && !programmingMode))
                                                Thread.sleep(40);
                                        }
                                        /* tp */
                                        else if(behaviorSplitted[i].startsWith("tp:") && behaviorSelected != 0) {
                                            map[Integer.parseInt(behaviorSplitted[i].split(":")[1])] = map[behaviorSelected];
                                            map[behaviorSelected] = '.';
                                            behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                        }
                                        /* step */
                                        else if(behaviorSplitted[i].startsWith("step:") && behaviorSelected != 0) {
                                            int delay  = Integer.parseInt(behaviorSplitted[i].split(":")[2]),
                                                target = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
                                            
                                            while(behaviorSelected != target && (!behavior.isEmpty() && ph && !programmingMode)) {
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
                                                    map[behaviorSelected + 1] = Blocks.getRightC(map[behaviorSelected]);
                                                    map[behaviorSelected] = '.';
                                                    behaviorSelected++;
                                                } else if(behaviorSelected % WIDTH < target % WIDTH && map[behaviorSelected + 1] != '.') {
                                                    break;
                                                } else if(behaviorSelected % WIDTH > target % WIDTH && map[behaviorSelected - 1] == '.') {
                                                    map[behaviorSelected - 1] = Blocks.getLeftC(map[behaviorSelected]);
                                                    map[behaviorSelected] = '.';
                                                    behaviorSelected--;
                                                } else if(behaviorSelected % WIDTH > target % WIDTH && map[behaviorSelected - 1] != '.') {
                                                    break;
                                                }
                                                
                                                Thread.sleep(!slow ? (long)delay : (long)delay * 2L);
                                            }
                                        }
                                    }
                                }
                                
                                Thread.sleep(50);
                            } catch(Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }
        
        /* для многопользовательской игры */
        
        if(gameState == 1) {
            new Thread() {
                public void run() {
                    String save;
                    
                    ServerSocketHints hints = new ServerSocketHints();
                    hints.backlog = 1;
                    hints.acceptTimeout = 0;
                    ServerSocket server = Gdx.net.newServerSocket(Protocol.TCP, connectUrl.split(":")[0], Integer.parseInt(connectUrl.split(":")[1]), hints);
                    Socket client = server.accept(null);
                    BufferedReader stream = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                    while(true) {
                        try {
                            String need = stream.readLine();
                            if(need.equals("map")) {
                                save = behavior + "\n" + bgColorRed + " " + bgColorGreen + " " + bgColorBlue + "\n";
                                int iii = 0;
                                for(int i = 0; i < HEIGHT; i++) {
                                    for(int ii = 0; ii < WIDTH; ii++) {
                                        save += "" + map[iii];
                                        iii++;
                                    }
                                    save += "\n";
                                }
                                client.getOutputStream().write(save.getBytes(StandardCharsets.UTF_8));
                            } else if(need.equals("pos"))
                                client.getOutputStream().write((selectedBlockAddr() + "\n").getBytes(StandardCharsets.UTF_8));
                            else if(need.equals("setmsg")) {
                                String[] messages = stream.readLine().split(";;;");
                                
                                for(int msgi = 0; msgi < messages.length; msgi++) {
                                    message = messages[msgi];
                                    /* выполнение команд */
                                    if(message.startsWith("/")) {
                                        String[] command = message.substring(1).split(" ");
                                        
                                        try {
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
                                                behavior = message.substring(3);
                                            } else if(command[0].startsWith("B")) {
                                                bgColorRed = Integer.parseInt(command[1]);
                                                bgColorGreen = Integer.parseInt(command[2]);
                                                bgColorBlue = Integer.parseInt(command[3]);
                                            }
                                        } catch(Exception e) {}
                                        
                                        message = "";
                                    }
                                }
                            } else if(need.equals("setpos"))
                                pos2 = Integer.parseInt(stream.readLine());
                            
                            Thread.sleep(17);
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        } else if(gameState == 2) {
            new Thread() {
                public void run() {
                    Socket client = Gdx.net.newClientSocket(Protocol.TCP, connectUrl.split(":")[0], Integer.parseInt(connectUrl.split(":")[1]), new SocketHints());
                    BufferedReader stream = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                    while(true) {
                        try {
                            if(selected == -1) {
                                client.getOutputStream().write("map\n".getBytes(StandardCharsets.UTF_8));
                                if(!programmingMode) behavior = stream.readLine();
                                else stream.readLine();
                                String[] color = stream.readLine().split(" ");
                                bgColorRed   = Integer.parseInt(color[0]);
                                bgColorGreen = Integer.parseInt(color[1]);
                                bgColorBlue  = Integer.parseInt(color[2]);
                                for(int y = 0; y < HEIGHT; y++) {
                                    char[] chars = stream.readLine().toCharArray();
                                    for(int x = 0; x < WIDTH; x++) {
                                        if(selected == -1)
                                            map[y * WIDTH + x] = chars[x];
                                    }
                                }
                            }
                            
                            client.getOutputStream().write("pos\n".getBytes(StandardCharsets.UTF_8));
                            adminPos = Integer.parseInt(stream.readLine());
                            
                            client.getOutputStream().write(("setpos\n" + selectedBlockAddr() + "\n").getBytes(StandardCharsets.UTF_8));
                            
                            if(!cmsg.isEmpty()) {
                                client.getOutputStream().write(("setmsg\n" + cmsg + "\n").getBytes(StandardCharsets.UTF_8));
                                message = cmsg;
                                cmsg = "";
                            }
                            
                            Thread.sleep(17);
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        }
    }
}
