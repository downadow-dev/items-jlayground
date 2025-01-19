/*
   Copyright 2025 downadow

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
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.Input.*;
import java.util.Random;

public class Main implements ApplicationListener {
    SpriteBatch batch;
    ShapeRenderer shape;
    FitViewport viewport;
    Vector2 touch;
    BitmapFont font;
    HttpRequestBuilder rqbuilder;
    short scene;
    final short S_INFO = 0,
        S_MENU = 1,
        S_CLIENT_MENU = 2,
        S_GAME = 3;
    Texture blackTexture, black2Texture, fireTexture, fire2Texture, portal0Texture,
        portal1Texture, redTexture, rightTexture, leftTexture, vignetteTexture,
        pricelTexture, upBtnTexture, downBtnTexture, rightBtnTexture,
        leftBtnTexture, helpBtnTexture, selectBtnTexture, fillBtnTexture,
        fireBtnTexture, fire2BtnTexture, boomBtnTexture, delBoomBtnTexture,
        nightBtnTexture, rainBtnTexture, slowBtnTexture, uiBtnTexture,
        qBtnTexture, eBtnTexture, moreBtnTexture, bgColorBtnTexture,
        programmingModeBtnTexture, chatBtnTexture, phBtnTexture;
    Texture[] boomTextures, rainTextures;
    String text;
    boolean done, retval;
    String[] rpList;
    String root;
    int selectedRp = -1;
    
    /* ширина и высота карты в объектах */
    private final int WIDTH = 250, HEIGHT = 60;
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
    
    private String behavior = "", cmsg = "", selectNumber = "";
    private int behaviorSelected = 0;
    
    private boolean ui = true;
    /* блокировка управления */
    private boolean block = false;
    /* замедленное время */
    private boolean slow = false;
    
    private boolean noWater = false;
    
    private boolean select = false, step = false;
    
    private boolean fill = false;
    
    private int cameraStart = 115 + 50 * WIDTH;
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
    
    private int adminPos = 0;
    private int etcPage = 0;
    
    public void create() {
        root = ".items-jlayground.data";
        rqbuilder = new HttpRequestBuilder();
        text = "";
        
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
                        if(select)
                            return false;
                        
                        if(key == Input.Keys.UP && !fill && !help) {
                            cameraStart -= WIDTH;
                            return true;
                        } else if(key == Input.Keys.DOWN && !fill && !help) {
                            cameraStart += WIDTH;
                            return true;
                        } else if(key == Input.Keys.RIGHT && !fill && !help) {
                            cameraStart++;
                            return true;
                        } else if(key == Input.Keys.LEFT && !fill && !help) {
                            cameraStart--;
                            return true;
                        }
                        
                        if(!programmingMode && !writeMessage) {
                            if(key == Input.Keys.O) {
                                cameraStart -= 3;
                                return true;
                            } else if(key == Input.Keys.P) {
                                cameraStart += 3;
                                return true;
                            } else if(key == Input.Keys.F1) {
                                help = (help ? false : true);
                                return true;
                            } else if(key == Input.Keys.C) {
                                setBlock(selectedBlockAddr(), currentBlock);
                                return true;
                            } else if(key == Input.Keys.BACKSPACE) {
                                setBlock(selectedBlockAddr(), '.');
                                return true;
                            } else if(key == Input.Keys.UP && help && helpIndex > 0) {
                                helpIndex -= 2;
                                return true;
                            } else if(key == Input.Keys.DOWN && help) {
                                helpIndex += 2;
                                return true;
                            } else if(key == Input.Keys.LEFT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - 1] == '.') {
                                for(int i = selectedBlockAddr() - 1; map[i] == '.'; i--)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.RIGHT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + 1] == '.') {
                                for(int i = selectedBlockAddr() + 1; map[i] == '.'; i++)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.DOWN && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + WIDTH] == '.') {
                                for(int i = selectedBlockAddr() + WIDTH; map[i] == '.'; i += WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.UP && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - WIDTH] == '.') {
                                for(int i = selectedBlockAddr() - WIDTH; map[i] == '.'; i -= WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            
                            else if(key == Input.Keys.LEFT && fill && map[selectedBlockAddr()] != '.') {
                                for(int i = selectedBlockAddr() - 1; map[i] != '.'; i--)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.RIGHT && fill && map[selectedBlockAddr()] != '.') {
                                for(int i = selectedBlockAddr() + 1; map[i] != '.'; i++)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.DOWN && fill && map[selectedBlockAddr()] != '.') {
                                for(int i = selectedBlockAddr() + WIDTH; map[i] != '.'; i += WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            else if(key == Input.Keys.UP && fill && map[selectedBlockAddr()] != '.') {
                                for(int i = selectedBlockAddr() - WIDTH; map[i] != '.'; i -= WIDTH)
                                    setBlock(i, map[selectedBlockAddr()]);
                                fill = false;
                                return true;
                            }
                            /* "прыжок" */
                            else if(key == Input.Keys.W && selected != -1 && ((!Blocks.isHelicopter(map[selected]) && !Blocks.isEraser(map[selected])) && ph)) {
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
                                            Thread.sleep(200);
                                            
                                            jump = false;
                                        } catch(Exception e) {}
                                    }
                                }.start();
                                
                                return true;
                            }
                            /* включение/выключение "физики" */
                            else if(key == Input.Keys.F6 && !ph && gameState != 2) {
                                ph = true;
                                return true;
                            } else if(key == Input.Keys.F6 && ph && gameState != 2) {
                                ph = false;
                                return true;
                            }
                            /* изменение цвета фона */
                            else if(key == Input.Keys.F3) {
                                colorPointer++;
                                if(colorPointer == 5)
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
                                    bgColorRed = 115;
                                    bgColorGreen = 0;
                                    bgColorBlue = 0;
                                } else if(colorPointer == 3) {
                                    bgColorRed = 130;
                                    bgColorGreen = 130;
                                    bgColorBlue = 75;
                                } else if(colorPointer == 4) {
                                    bgColorRed = 160;
                                    bgColorGreen = 160;
                                    bgColorBlue = 255;
                                }
                                
                                return true;
                            }
                            /* включение/выключение ночи */
                            else if(key == Input.Keys.N && !night) {
                                night = true;
                                return true;
                            } else if(key == Input.Keys.N && night) {
                                night = false;
                                return true;
                            }
                            
                            if(!block) {
                                if(key == Input.Keys.F4 && !fill)
                                    fill = true;
                                else if(key == Input.Keys.F4 && fill)
                                    fill = false;
                                /* перемещение камеры */
                                else if(key == Input.Keys.UP)
                                    cameraStart -= WIDTH;
                                else if(key == Input.Keys.DOWN)
                                    cameraStart += WIDTH;
                                else if(key == Input.Keys.RIGHT)
                                    cameraStart++;
                                else if(key == Input.Keys.LEFT)
                                    cameraStart--;
                                /* показать/скрыть помощь */
                                else if(key == Input.Keys.F1 && !help && ui)
                                    help = true;
                                else if(key == Input.Keys.F1 && help && ui)
                                    help = false;
                                /* включить/выключить замедление времени */
                                else if(key == Input.Keys.F2 && !slow)
                                    slow = true;
                                else if(key == Input.Keys.F2 && slow)
                                    slow = false;
                                /*******************************/
                                else if(key == Input.Keys.F10) {
                                    rain = (rain == -1 ? 0 : -1);
                                /*******************************/
                                } else if(key == Input.Keys.ESCAPE && ui) {
                                    help = false;
                                    ui = false;
                                } else if(key == Input.Keys.ESCAPE && !ui)
                                    ui = true;
                                /* поставить огонь */
                                else if(key == Input.Keys.F && map[selectedBlockAddr()] != '.' &&
                                          !Blocks.isFireResistant(map[selectedBlockAddr()])) {
                                    fire(selectedBlockAddr());
                                } else if(key == Input.Keys.V && map[selectedBlockAddr()] != '.' && !Blocks.isWater(map[selectedBlockAddr()]))
                                    fire2(selectedBlockAddr());
                                /* выбрать блок под прицелом */
                                else if(key == Input.Keys.SPACE && selected == -1 && map[selectedBlockAddr()] != '.')
                                    selected = selectedBlockAddr();
                                /* убрать выделение */
                                else if(key == Input.Keys.SPACE && selected != -1) {
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
                                /* поставить блок */
                                else if(key == Input.Keys.C)
                                    setBlock(selectedBlockAddr(), currentBlock);
                                /* взрыв */
                                else if(key == Input.Keys.ENTER && selected == -1) {
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
                                            map[selected] = Blocks.getC2(map[selected]);
                                            boom(selected + i - WIDTH);
                                            
                                            new Thread() {
                                                public void run() {
                                                    try {Thread.sleep(60);} catch(Exception ex) {}
                                                    map[selected] = Blocks.getC2(map[selected]);
                                                }
                                            }.start();
                                            
                                            break;
                                        }
                                    }
                                } else if(key == Input.Keys.ENTER && Blocks.isTank(map[selected]) && Blocks.getLeftC(map[selected]) == map[selected]) {
                                    for(int i = 1; i < 16; i++) {
                                        if((map[selected - i - WIDTH] != '.' && !(map[selected - i - WIDTH] >= '0' && map[selected - i - WIDTH] <= '9')) || (map[selected - i] != '.' && !(map[selected - i] >= '0' && map[selected - i] <= '9'))) {
                                            map[selected] = Blocks.getC2(map[selected]);
                                            boom(selected - i - WIDTH);
                                            
                                            new Thread() {
                                                public void run() {
                                                    try {Thread.sleep(60);} catch(Exception ex) {}
                                                    map[selected] = Blocks.getC2(map[selected]);
                                                }
                                            }.start();
                                            
                                            break;
                                        }
                                    }
                                } else if(key == Input.Keys.ENTER && Blocks.isHelicopter(map[selected]) && map[selected + WIDTH] == '.' && !blockHelicopter) {
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
                                else if(key == Input.Keys.W && selected != -1 && (map[selected - WIDTH] == '.' || Blocks.isWater(map[selected - WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                                    map[selected - WIDTH] = map[selected];
                                    
                                    if(!Blocks.isEraser(map[selected]))
                                        map[selected] = '.';
                                    else
                                        setBlock(selected, '.');
                                    
                                    selected -= WIDTH;
                                } else if(key == Input.Keys.S && selected != -1 && (map[selected + WIDTH] == '.' || Blocks.isWater(map[selected + WIDTH]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                                    map[selected + WIDTH] = map[selected];
                                    
                                    if(!Blocks.isEraser(map[selected]))
                                        map[selected] = '.';
                                    else
                                        setBlock(selected, '.');
                                    
                                    selected += WIDTH;
                                } else if(key == Input.Keys.A && selected != -1 && (map[selected - 1] == '.' || Blocks.isWater(map[selected - 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                                    map[selected - 1] = map[selected];
                                    
                                    if(!Blocks.isEraser(map[selected]))
                                        map[selected] = '.';
                                    else
                                        setBlock(selected, '.');
                                    
                                    selected--;
                                } else if(key == Input.Keys.D && selected != -1 && (map[selected + 1] == '.' || Blocks.isWater(map[selected + 1]) || Blocks.isEraser(map[selected])) && map[selected] != 'f') {
                                    map[selected + 1] = map[selected];
                                    
                                    if(!Blocks.isEraser(map[selected]))
                                        map[selected] = '.';
                                    else
                                        setBlock(selected, '.');
                                    
                                    selected++;
                                }
                                /* удалить всю воду */
                                else if(key == Input.Keys.F8) {
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
                        
                        /* включить/выключить режим изменения поведения */
                        else if(key == Input.Keys.F5 && !programmingMode) {
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            
                            programmingMode = true;
                            return true;
                        }
                        else if(key == Input.Keys.F5 && programmingMode) {
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(false);
                            
                            if(gameState == 2) sendMessage("/p " + behavior);
                            programmingMode = false;
                            return true;
                        }
                        
                        if((!programmingMode && !writeMessage) && key == Input.Keys.X) {
                            select = true;
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            return true;
                        }
                            
                        /* написать сообщение */
                        else if(key == Input.Keys.F7 && gameState == 2 && !writeMessage) {
                            text = "";
                            writeMessage = true;
                            if(Gdx.app.getType() == Application.ApplicationType.Android)
                                Gdx.input.setOnscreenKeyboardVisible(true);
                            return true;
                        } else if(key == Input.Keys.F7 && gameState == 2) {
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
            
            public boolean touchDown(int x, int y, int ptr, int btn) {
                touch.set(x, y);
                viewport.unproject(touch);
                
                if(scene == S_INFO) {
                    scene = S_MENU;
                    text = Gdx.files.external(root + "/lastUrl").readString();
                    return true;
                } else if(scene == S_MENU) {
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
                        } catch(Exception ex) {}
                    } else if(touch.x > 275 && touch.x < (275 + 250) && touch.y > 50 && touch.y < 80) {
                        try {
                            text = "";
                            scene = S_CLIENT_MENU;
                            return true;
                        } catch(Exception ex) {}
                    } else if(touch.x > (275 + 260) && touch.x < (275 + 250 * 2) && touch.y > 50 && touch.y < 80 && Gdx.app.getType() == Application.ApplicationType.Desktop) {
                        try {
                            gameState = 1;
                            startGame();
                            return true;
                        } catch(Exception ex) {}
                    }
                } else if(scene == S_CLIENT_MENU) {
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
                        return keyDown(selected != -1 ? Input.Keys.A : Input.Keys.LEFT);
                    } else if(touch.x > 0 && touch.x < 100 && touch.y > 0 && touch.y < 100) {
                        return keyDown(Input.Keys.O);
                    } else if(touch.x > 200 && touch.x < 300 && touch.y > 100 && touch.y < 200) {
                        return keyDown(selected != -1 ? Input.Keys.D : Input.Keys.RIGHT);
                    } else if(touch.x > 200 && touch.x < 300 && touch.y > 0 && touch.y < 100) {
                        return keyDown(Input.Keys.P);
                    } else if(touch.x > 100 && touch.x < 200 && touch.y > 200 && touch.y < 300) {
                        return keyDown(selected != -1 ? Input.Keys.W : Input.Keys.UP);
                    } else if(touch.x > 100 && touch.x < 200 && touch.y > 0 && touch.y < 100) {
                        return keyDown(selected != -1 ? Input.Keys.S : Input.Keys.DOWN);
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
                        return keyUp(Input.Keys.X);
                    } else if(touch.x > 900 && touch.x < 1200 && touch.y > 0 && touch.y < 300) {
                        for(boolean val : forBoom)
                            if(val && selected == -1)
                                return keyDown(Input.Keys.ENTER);
                        
                        return keyDown((selected != -1 && (Blocks.isTank(map[selected]) || Blocks.isHelicopter(map[selected]))) ? Input.Keys.ENTER : Input.Keys.C);
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
                    }
                }
                
                return false;
            }
            
            public boolean keyTyped(char c) {
                if(scene == S_CLIENT_MENU) {
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
                        
                        gameState = 2;
                        connectUrl = text;
                        text = "";
                        startGame();
                        
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
                        currentBlock = (char)Integer.parseInt(selectNumber);
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
                        
                        new Thread() {
                            public void run() {
                                Gdx.files.external(root + "/lastUrl").writeString(text, false);
                                
                                String mapDir;
                                if(!downloadFile(text + "/name", Gdx.files.external(root + "/name")))
                                    mapDir = "rp" + new Random().nextInt(1000000000);
                                else
                                    mapDir = Gdx.files.external(root + "/name").readString().replace("\n", "") + "_" + new Random().nextInt(1000000);
                                
                                Gdx.files.external(root + "/rp_list.txt").writeString(mapDir + "\n", true);
                                if(!downloadFile(text + "/desc", Gdx.files.external(root + "/" + mapDir + "/desc"))) {
                                    text = "ERROR!!!";
                                    return;
                                }
                                
                                if(!downloadFile(text + "/map", Gdx.files.external(root + "/" + mapDir + "/map"))) {
                                    text = "ERROR!!!";
                                    return;
                                }
                                
                                if(!downloadFile(text + "/help", Gdx.files.external(root + "/" + mapDir + "/help"))) {
                                    text = "ERROR!!!";
                                    return;
                                }
                                
                                String[] lines = Gdx.files.external(root + "/" + mapDir + "/desc").readString().replace("\r", "").split("\n");
                                for(String line : lines) {
                                    String[] tokens = line.split(" ");
                                    for(String tkn : tokens) {
                                        if(tkn.startsWith("texture:")) {
                                            if(!downloadFile(text + "/images/" + tkn.split(":")[1], Gdx.files.external(root + "/" + mapDir + "/images/" + tkn.split(":")[1]))) {
                                                text = "ERROR!!!";
                                                return;
                                            }
                                        }
                                    }
                                }
                                
                                text = "(done)";
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
        font = new BitmapFont(Gdx.files.internal("font/NotoSansMonoRegular.fnt"), false);
        font.setFixedWidthGlyphs("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮёйцукенгшщзхъфывапролджэячсмитьбю█");
        
        scene = S_INFO;
        
        if(!Gdx.files.external(root + "/lastUrl").exists())
            Gdx.files.external(root + "/lastUrl").writeString("https://raw.githubusercontent.com/downadow-dev/items-jlayground/main/game", false);
        
        updateRpList();
        Blocks.init();
    }
    
    public void resize(int width, int height) {
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
        
        if(scene == S_INFO || scene == S_MENU || scene == S_CLIENT_MENU) {
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin(); noEnd = true;
            
            batch.end(); noEnd = false;
            shape.setProjectionMatrix(viewport.getCamera().combined);
            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
            shape.setColor(new Color(0.6f, 0.6f, 1f, 1f));
            shape.rect(0, 0, 1200, 728);
            shape.end(); noShapeEnd = false;
            if(scene == S_INFO) {
                batch.setProjectionMatrix(viewport.getCamera().combined);
                batch.begin(); noEnd = true;
                
                batch.draw(blackTexture, 0, 0, 1200, 728);
                font.getData().setScale(0.65f);
                font.draw(batch, "Items Jlayground --- это свободная игра-песочница, в которой игроку", 10, 680);
                font.draw(batch, "предоставляется свобода строить, разрушать, программировать, взрывать,", 10, 660);
                font.draw(batch, "летать на вертолёте, стрелять из танка и другое. Игра написана на Java", 10, 640);
                font.draw(batch, "с использованием LibGDX. Items Jlayground поддерживает многопользова-", 10, 620);
                font.draw(batch, "тельскую игру. Автор игры --- downadow.", 10, 600);
                
                font.draw(batch, "Мир Items Jlayground представляет собой одномерный массив с заданными", 10, 560);
                font.draw(batch, "шириной (250) и высотой (60). Ячейка может определяться как либо отсутствие", 10, 540);
                font.draw(batch, "блока (.), либо танк, либо вертолёт, либо вода, либо примитив. Примитивы", 10, 520);
                font.draw(batch, "могут иметь множество атрибутов (падающий, крепкий, липкий и т. д.).", 10, 500);
                
                font.getData().setScale(0.5f);
                font.draw(batch, "Нажмите для продолжения...", 900, 60);
                
                batch.end(); noEnd = false;
            } else {
                shape.setProjectionMatrix(viewport.getCamera().combined);
                shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                shape.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
                shape.rect(15, 640, 940, 30);
                shape.setColor(new Color(0f, 1f, 0f, 1f));
                shape.rect(940, 640, 60, 30);
                if(scene != S_CLIENT_MENU) {
                    shape.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
                    shape.rect(15, 100, 1000, 480);
                    shape.setColor(new Color(0f, 0.8f, 0f, 1f));
                    shape.rect(15, 50, 250, 30);
                    shape.rect(275, 50, 250, 30);
                    if(Gdx.app.getType() == Application.ApplicationType.Desktop)
                        shape.rect(275 + 260, 50, 250, 30);
                }
                shape.end(); noShapeEnd = false;
                
                batch.setProjectionMatrix(viewport.getCamera().combined);
                batch.begin(); noEnd = true;
                font.getData().setScale(0.6f);
                font.draw(batch, (scene != S_CLIENT_MENU ? "HTTP-адрес для загрузки ресурспака" : "HTTP-адрес сервера"), 15, 700);
                font.draw(batch, text + "█", 15, 660);
                if(scene != S_CLIENT_MENU) {
                    font.draw(batch, "Ресурспаки", 15, 620);
                    try {
                        for(int j = 570, i = 0; i < rpList.length; i++) {
                            font.draw(batch, (i == selectedRp ? "> " : "") + rpList[i], 20, j);
                            j -= 25;
                        }
                    } catch(Exception ex) {}
                    font.draw(batch, "Одиночная игра", 25, 75);
                    font.draw(batch, "Подключиться", 285, 75);
                    if(Gdx.app.getType() == Application.ApplicationType.Desktop)
                        font.draw(batch, "Server job", 275 + 270, 75);
                }
                batch.end(); noEnd = false;
            }
            
        } else if(scene == S_GAME) {
            /* рисование объектов
               ================== */
            int iii = cameraStart;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(iii < map.length && iii >= 0) {
                            shape.setProjectionMatrix(viewport.getCamera().combined);
                            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                            shape.setColor(new Color(bgColorRed * (1f / 255), bgColorGreen * (1f / 255), bgColorBlue * (1f / 255), 1f));
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            
                            shape.end(); noShapeEnd = false;
                            if(ui) {
                                shape.setProjectionMatrix(viewport.getCamera().combined);
                                shape.begin(ShapeRenderer.ShapeType.Line); noShapeEnd = true;
                                try {
                                    shape.setColor(bgColorRed < 195 && bgColorGreen < 195 && bgColorBlue < 195 ? new Color((bgColorRed + 60) * (1f / 255), (bgColorGreen + 60) * (1f / 255), (bgColorBlue + 60) * (1f / 255), 1f) : new Color((bgColorRed - 60) * (1f / 255), (bgColorGreen - 60) * (1f / 255), (bgColorBlue - 60) * (1f / 255), 1f));
                                    shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                                } catch(IllegalArgumentException e) {}
                                shape.end(); noShapeEnd = false;
                            }
                            
                            if(rain != -1) {
                                batch.setProjectionMatrix(viewport.getCamera().combined);
                                batch.begin(); noEnd = true;
                                batch.draw(rainTextures[rain], ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                                batch.end(); noEnd = false;
                            }
                        }
                        
                        if(map[iii] == '~' || map[iii] == ',') {
                            shape.setProjectionMatrix(viewport.getCamera().combined);
                            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                            
                            shape.setColor(new Color(0.03f, 0.03f, 0.03f, 1f));
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            shape.setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
                            shape.rect(ii * Blocks.defaultW + Blocks.defaultW / 3, 728 - ((i + 1) * Blocks.defaultH - Blocks.defaultH / 3), Blocks.defaultW / 3, Blocks.defaultH / 3);
                            
                            shape.end(); noShapeEnd = false;
                            
                            if(ui) {
                                batch.setProjectionMatrix(viewport.getCamera().combined);
                                batch.begin(); noEnd = true;
                                font.getData().setScale(0.4f);
                                font.draw(batch, (map[iii] == ',' ? "<" : ">"), ii * Blocks.defaultW + 5, 728 - (i * Blocks.defaultH + 5));
                                batch.end(); noEnd = false;
                            }
                        } else if(map[iii] == ':') {
                            batch.setProjectionMatrix(viewport.getCamera().combined);
                            batch.begin(); noEnd = true;
                            batch.draw(leftTexture, ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            batch.end(); noEnd = false;
                        } else if(map[iii] == ';') {
                            batch.setProjectionMatrix(viewport.getCamera().combined);
                            batch.begin(); noEnd = true;
                            batch.draw(rightTexture, ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            batch.end(); noEnd = false;
                        } else if(map[iii] == 'b') {
                            shape.setProjectionMatrix(viewport.getCamera().combined);
                            shape.begin(ShapeRenderer.ShapeType.Filled); noShapeEnd = true;
                            
                            shape.setColor(new Color(0.03f, 0.03f, 0.03f, 1f));
                            shape.rect(ii * Blocks.defaultW, 728 - (i + 1) * Blocks.defaultH, Blocks.defaultW, Blocks.defaultH);
                            
                            shape.end(); noShapeEnd = false;
                        }
                        /* блок мода */
                        else if(Blocks.getTextureX(map[iii]) == Blocks.defaultX && Blocks.getTextureY(map[iii]) == Blocks.defaultY &&
                                Blocks.getTextureWidth(map[iii]) <= Blocks.defaultW && Blocks.getTextureHeight(map[iii]) <= Blocks.defaultH && !Blocks.isUnknown(map[iii])) {
                            batch.setProjectionMatrix(viewport.getCamera().combined);
                            batch.begin(); noEnd = true;
                            batch.draw(Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]) - 1, 728 - i * Blocks.defaultH - Blocks.getTextureHeight(map[iii]) - Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]) + 2, Blocks.getTextureHeight(map[iii]) + 1);
                            batch.end(); noEnd = false;
                            if(Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.')
                                map[iii] = Blocks.getC2(map[iii]);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {
                        if(noShapeEnd) shape.end();
                        else if(noEnd) batch.end();
                        noShapeEnd = false;
                        noEnd = false;
                    }
                    iii++;
                }
                iii += WIDTH - 26;
            }
            
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin(); noEnd = true;
            
            iii = cameraStart;
            for(int i = 0; i < 17; i++) {
                for(int ii = 0; ii < 26; ii++) {
                    try {
                        if(map[iii] == 'f') {
                            batch.draw(fireTexture, ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3);
                            if(rain != -1 && selected == -1)
                                map[iii] = 'b';
                        } else if(map[iii] == 'F')
                            batch.draw(fire2Texture, ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3);
                        else if((int)map[iii] >= (int)'0' && (int)map[iii] <= (int)'9')
                            batch.draw(boomTextures[map[iii] - '0'], ii * Blocks.defaultW - Blocks.defaultW, 728 - ((i + 2) * Blocks.defaultH), Blocks.defaultW * 3, Blocks.defaultH * 3);
                        else if(map[iii] == 'p') {
                            batch.draw(portal0Texture, ii * Blocks.defaultW, 728 - ((i + 1) * Blocks.defaultH), Blocks.defaultW, Blocks.defaultH * 2);
                        } else if(map[iii] == 'P') {
                            batch.draw(portal1Texture, ii * Blocks.defaultW, 728 - ((i + 1) * Blocks.defaultH), Blocks.defaultW, Blocks.defaultH * 2);
                        } else if(Blocks.isUnknown(map[iii])) {
                            font.getData().setScale(2f);
                            font.draw(batch, "" + map[iii], ii * Blocks.defaultW + Blocks.defaultW / 4, 728 - (i + 1) * Blocks.defaultH);
                        }
                        /* блок мода */
                        else if(Blocks.getTextureX(map[iii]) != Blocks.defaultX || Blocks.getTextureY(map[iii]) != Blocks.defaultY ||
                                Blocks.getTextureWidth(map[iii]) > Blocks.defaultW || Blocks.getTextureHeight(map[iii]) > Blocks.defaultH) {
                            batch.draw(Blocks.getTexture(map[iii]), ii * Blocks.defaultW + Blocks.getTextureX(map[iii]), 728 - i * Blocks.defaultH - Blocks.getTextureHeight(map[iii]) - Blocks.getTextureY(map[iii]), Blocks.getTextureWidth(map[iii]), Blocks.getTextureHeight(map[iii]));
                            if(Blocks.isHelicopter(map[iii]) && map[iii + WIDTH] == '.')
                                map[iii] = Blocks.getC2(map[iii]);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            /*********************************/
            
            iii = cameraStart;
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
                            else if(gameState == 2 && iii == adminPos)
                                batch.draw(pricelTexture, ii * Blocks.defaultW + Blocks.defaultW / 2, 728 - i * Blocks.defaultH - Blocks.defaultH / 2, 12, 8);
                        }
                    } catch(ArrayIndexOutOfBoundsException e) {}
                    iii++;
                }
                iii += WIDTH - 26;
            }
            
            if(ui) {
                batch.draw(pricelTexture, 1200 / 2 + 16, 728 - (700 / 2 - 20), 14, 8);
                batch.draw(vignetteTexture, 0, 0, 1200, 728);
                
                font.getData().setScale(0.5f);
                if(!fill)
                    font.draw(batch, "" + (System.currentTimeMillis() - startTime) / 60000 + " min", 15, 728 - 20);
                else
                    font.draw(batch, "Выберите сторону...", 15, 728 - 20);
                
                if(programmingMode) {
                    font.getData().setScale(0.4f);
                    font.draw(batch, "Режим программирования", 15, 728 - 60);
                    char[] behavior2 = behavior.toCharArray();
                    
                    iii = 0;
                    int i = 0, ii = 0;
                    loop: for(; i < 8; i++) {
                        for(; ii < 128; ii++, iii++) {
                            try {
                                font.draw(batch, "" + behavior2[iii], 15 + ii * 7, 728 - 80 + i * 20);
                            } catch(ArrayIndexOutOfBoundsException e) {
                                break loop;
                            }
                        }
                    }
                    font.draw(batch, "█", 15 + ii * 7, 728 - 80 + i * 20);
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
                        batch.draw(fireBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 1) {
                        batch.draw(fire2BtnTexture, 980, 618, 100, 100);
                        batch.draw(boomBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 2) {
                        batch.draw(delBoomBtnTexture, 980, 618, 100, 100);
                        batch.draw(nightBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 3) {
                        batch.draw(rainBtnTexture, 980, 618, 100, 100);
                        batch.draw(slowBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 4) {
                        batch.draw(uiBtnTexture, 980, 618, 100, 100);
                        batch.draw(bgColorBtnTexture, 875, 618, 100, 100);
                    } else if(etcPage == 5) {
                        batch.draw(programmingModeBtnTexture, 980, 618, 100, 100);
                        batch.draw(phBtnTexture, 875, 618, 100, 100);
                    } else etcPage = 0;
                }
                
                font.getData().setScale(0.5f);
                
                if(gameState == 1) font.draw(batch, message, 15, 728 - 60);
                else if(gameState == 2 && writeMessage) font.draw(batch, text + "█", 15, 728 - 60);
                
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
    
    private void setDone(boolean val) {
        done = val;
    }
    
    private void setRetval(boolean val) {
        retval = val;
    }
    
    private boolean downloadFile(String url, FileHandle fileToSave) {
        retval = false;
        done = false;
        
        new Thread() {
            public void run() {
                try {
                    Net.HttpRequest rq;
                    rq = rqbuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
                    
                    Gdx.net.sendHttpRequest(rq, new Net.HttpResponseListener() {
                        public void cancelled() { setDone(true); }
                        
                        public void failed(Throwable t) { setDone(true); }
                        
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            if(httpResponse.getStatus().getStatusCode() != 404) {
                                fileToSave.writeBytes(httpResponse.getResult(), false);
                                setRetval(true);
                            }
                            
                            setDone(true);
                        }
                    });
                    Thread.sleep(20000);
                    Pools.free(rq);
                } catch(Exception ex) {}
                setDone(true);
            }
        }.start();
        
        while(!done) {
            try { Thread.sleep(100); } catch(Exception ex) {}
        }
        
        return retval;
    }
    
    private void updateRpList() {
        if(!Gdx.files.external(root + "/rp_list.txt").exists()) {
            rpList = null;
            return;
        }
        
        rpList = Gdx.files.external(root + "/rp_list.txt").readString().split("\n");
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
        if(gameState == 1)
            Gdx.files.external(root + "/msg.php").writeString("<?php file_put_contents('msg', $_GET['m'] . ' <' . $_SERVER['REMOTE_ADDR'] . '>'); ?>\n", false);
        
        /* загрузка карты, поведения, мода и текста помощи */
        
        String[] lines;
        
        lines = Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").readString().split("\n");
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
        
        helpMessage = Gdx.files.external(root + "/" + rpList[selectedRp] + "/help").readString().split("\n");
        
        lines = Gdx.files.external(root + "/" + rpList[selectedRp] + "/desc").readString().split("\n");
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
                        texture = new Texture(Gdx.files.external(root + "/" + rpList[selectedRp] + "/images/" + tokens[i].split(":")[1]));
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
        scene = S_GAME;
        
        /* автосохранение карты */
        if(gameState != 2) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(30000);
                            
                            Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").writeString(behavior + "\n", false);
                            Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").writeString(bgColorRed + " " + bgColorGreen + " " + bgColorBlue + "\n", true);
                            int iii = 0;
                            for(int i = 0; i < HEIGHT; i++) {
                                for(int ii = 0; ii < WIDTH; ii++) {
                                    Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").writeString("" + map[iii], true);
                                    iii++;
                                }
                                Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").writeString("\n", true);
                            }
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        }
        
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
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH + 1)
                                    tmp[ii] = true;
                            }
                            
                            for(int i = WIDTH * 2 - 1; i >= WIDTH; i--) {
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH - 1)
                                    tmp[ii] = true;
                            }
                            
                            for(int i = WIDTH; i < WIDTH * 2; i++) {
                                for(int ii = i; ii < map.length && Blocks.isTranslucent(map[ii]); ii += WIDTH)
                                    tmp[ii] = true;
                            }
                        }
                        
                        /* свет от блоков */
                        for(int i = 0; i < lightMap.length; i++) {
                            if(Blocks.getLight(map[i]) > 0) {
                                tmp[i - 1]     = true;
                                tmp[i + 1]     = true;
                                tmp[i - WIDTH] = true;
                                tmp[i + WIDTH] = true;
                                
                                int left = Blocks.getLight(map[i]);
                                for(int ii = i; left > 0 && (Blocks.isTranslucent(map[ii]) || ii == i); ii -= WIDTH, left--) {
                                    for(int iii = ii; (iii - ii) < left && (Blocks.isTranslucent(map[iii]) || iii == i); iii++) {
                                        tmp[iii] = true;
                                    }
                                    for(int iii = ii; -(iii - ii) < left && (Blocks.isTranslucent(map[iii]) || iii == i); iii--) {
                                        tmp[iii] = true;
                                    }
                                }
                                left = Blocks.getLight(map[i]);
                                for(int ii = i; left > 0 && (Blocks.isTranslucent(map[ii]) || ii == i); ii += WIDTH, left--) {
                                    for(int iii = ii; (iii - ii) < left && (Blocks.isTranslucent(map[iii]) || iii == i); iii++) {
                                        tmp[iii] = true;
                                    }
                                    for(int iii = ii; -(iii - ii) < left && (Blocks.isTranslucent(map[iii]) || iii == i); iii--) {
                                        tmp[iii] = true;
                                    }
                                }
                            }
                        }
                        
                        for(int i = 0; i < lightMap.length; i++) {
                            lightMap[i] = tmp[i];
                        }
                        
                        Thread.sleep(1000);
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
        
        /* дождь и пр. */
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        if(selected != -1 && (map[selected] == '.' || Blocks.isWater(map[selected]))) {
                            if(Blocks.isWater(map[selected]) && gameState != 2)
                                noWater = true;
                            selected = -1;
                        }
                        
                        if(selected != -1)
                            cameraStart = selected - (selectedBlockAddr() - cameraStart);
                        
                        while(selected != -1 && !jump && selected > WIDTH && !Blocks.isHelicopter(map[selected]) && !Blocks.isEraser(map[selected]) && map[selected + WIDTH] == '.' && ph) {
                            selected += WIDTH;
                            cameraStart = selected - (selectedBlockAddr() - cameraStart);
                            map[selected] = map[selected - WIDTH];
                            map[selected - WIDTH] = '.';
                            Thread.sleep(30);
                        }
                        
                        if(rain != -1) {
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
        
        /* отправка сообщения */
        if(gameState == 2) {
            new Thread() {
                public void run() {
                    while(true) {
                        try {
                            if(!cmsg.isEmpty()) {
                                setDone(false);
                                Net.HttpRequest rq;
                                
                                String m = "";
                                for(byte c : cmsg.getBytes("UTF-8")) {
                                    if(c == (byte)' ') m += "+";
                                    else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-') m += "" + (char)c;
                                    else {
                                        String result = Integer.toHexString(Byte.toUnsignedInt(c)).toUpperCase();
                                        m += "%" + (result.length() < 2 ? "0" : "") + result;
                                    }
                                }
                                
                                rq = rqbuilder.newRequest().method(Net.HttpMethods.GET).url(connectUrl + "/msg.php").content("m=" + m).build();     
                                Gdx.net.sendHttpRequest(rq, new Net.HttpResponseListener() {
                                    public void cancelled() { setDone(true); }
                                    
                                    public void failed(Throwable t) { setDone(true); }
                                    
                                    public void handleHttpResponse(Net.HttpResponse httpResponse) { setDone(true); }
                                });
                                while(!done)
                                    Thread.sleep(50);
                                Pools.free(rq);
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
                                    } else if(behaviorSplitted[i].equals("wait:light")) {
                                        while(!lightMap[behaviorSelected] &&
                                              !lightMap[behaviorSelected - 1] &&
                                              !lightMap[behaviorSelected + 1] &&
                                              !lightMap[behaviorSelected - WIDTH] &&
                                              !lightMap[behaviorSelected + WIDTH] &&
                                              !behavior.isEmpty())
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
                            //e.printStackTrace();
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
                            Gdx.files.external(root + "/adminMap").writeString(behavior + "\n", false);
                            Gdx.files.external(root + "/adminMap").writeString(bgColorRed + " " + bgColorGreen + " " + bgColorBlue + "\n", true);
                            int iii = 0;
                            for(int i = 0; i < HEIGHT; i++) {
                                for(int ii = 0; ii < WIDTH; ii++) {
                                    Gdx.files.external(root + "/adminMap").writeString("" + map[iii], true);
                                    iii++;
                                }
                                Gdx.files.external(root + "/adminMap").writeString("\n", true);
                            }
                            
                            Gdx.files.external(root + "/adminPos").writeString("" + selectedBlockAddr(), false);
                            
                            try {
                                String[] messages = Gdx.files.external(root + "/msg").readString().split(";;;");
                                
                                for(int msgi = 0; msgi < messages.length; msgi++) {
                                    message = messages[msgi];
                                    /* выполнение команд */
                                    if(message.startsWith("/")) {
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
                                            bgColorRed = Integer.parseInt(command[1]);
                                            bgColorGreen = Integer.parseInt(command[2]);
                                            bgColorBlue = Integer.parseInt(command[3]);
                                        }
                                        
                                        message = "";
                                        
                                        Gdx.files.external(root + "/msg").writeString("", false);
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
                                downloadFile(connectUrl + "/adminMap", Gdx.files.external(root + "/" + rpList[selectedRp] + "/map"));
                                
                                String[] ln;
                                
                                ln = Gdx.files.external(root + "/" + rpList[selectedRp] + "/map").readString().split("\n");
                                if(!programmingMode) behavior = ln[0];
                                bgColorRed = Integer.parseInt(ln[1].split(" ")[0]);
                                bgColorGreen = Integer.parseInt(ln[1].split(" ")[1]);
                                bgColorBlue = Integer.parseInt(ln[1].split(" ")[2]);
                                int ii = 0;
                                for(int j = 2; j < HEIGHT + 2; j++) {
                                    char[] line = ln[j].toCharArray();
                                    for(int i = 0; i < WIDTH; i++)
                                        map[ii++] = line[i];
                                }
                            }
                            
                            try {
                                downloadFile(connectUrl + "/adminPos", Gdx.files.external(root + "/adminPos"));
                                adminPos = Integer.parseInt(Gdx.files.external(root + "/adminPos").readString());
                            } catch(Exception ex) {}
                            /******************/
                            
                            Thread.sleep(450);
                        } catch(Exception ex) {}
                    }
                }
            }.start();
        }
    }
}
