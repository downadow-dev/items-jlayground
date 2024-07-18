package downadow.items_jlayground.main;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;

public class Main extends JPanel {
	/* ширина и высота карты в объектах */
	private static final int WIDTH = 100, HEIGHT = 50;
	/* карта */
	private static char[] map = new char[WIDTH * HEIGHT];
	
	private static boolean[] forBoom = new boolean[WIDTH * HEIGHT];
	
	private static boolean ui = true;
	/* блокировка управления */
	private static boolean block = false;
	/* замедленное время */
	private static boolean slow = false;
	
	private static boolean noWater = false;
	
	private static boolean select = false;
	
	private static int cameraStart = 32 + 40 * WIDTH;
	/* выбранный блок */
	private static int selected = -1;
	/* вывести помощь */
	private static boolean help = true;
	
	/* тёмный режим */
	private static boolean darkMode = false;
	
	private static JFrame fr;
	
	/* возвращает адрес блока, на который указывает прицел */
	private static int selectedBlockAddr() {
		return cameraStart + 8 + 5 * WIDTH;
	}
	
	private static long startTime = 0;
	
	private static void fire(int addr) {
		final int saved = addr;
		if(map[saved] == 'B' || map[saved] == 'l' || map[saved] == '|' ||
		   map[saved] == '@' || map[saved] == 'd' || map[saved] == 'w' ||
		   map[saved] == 'c' || map[saved] == 'C' || map[saved] == 'L') {
			map[saved] = 'f';

			new Thread() {
				public void run() {
					try {
						for(int i = 0; i < 6; i++) {
							Thread.sleep(5000);
							if(map[saved] != 'f')
								break;
							if(map[saved - 1] == 'B' || map[saved - 1] == 'l' || map[saved - 1] == '|' ||
							   map[saved - 1] == '@' || map[saved - 1] == 'd' || map[saved - 1] == 'w' ||
							   map[saved - 1] == 'c' || map[saved - 1] == 'C' || map[saved - 1] == 'L')
								fire(saved - 1);
							if(map[saved + 1] == 'B' || map[saved + 1] == 'l' || map[saved + 1] == '|' ||
							   map[saved + 1] == '@' || map[saved + 1] == 'd' || map[saved + 1] == 'w' ||
							   map[saved + 1] == 'c' || map[saved + 1] == 'C' || map[saved + 1] == 'L')
								fire(saved + 1);
							if(map[saved + WIDTH] == 'B' || map[saved + WIDTH] == 'l' || map[saved + WIDTH] == '|' ||
							   map[saved + WIDTH] == '@' || map[saved + WIDTH] == 'd' || map[saved + WIDTH] == 'w' ||
							   map[saved + WIDTH] == 'c' || map[saved + WIDTH] == 'C' || map[saved + WIDTH] == 'L')
								fire(saved + WIDTH);
							if(map[saved - WIDTH] == 'B' || map[saved - WIDTH] == 'l' || map[saved - WIDTH] == '|' ||
							   map[saved - WIDTH] == '@' || map[saved - WIDTH] == 'd' || map[saved - WIDTH] == 'w' ||
							   map[saved - WIDTH] == 'c' || map[saved - WIDTH] == 'C' || map[saved - WIDTH] == 'L')
								fire(saved - WIDTH);
						}
						if(slow)
							Thread.sleep(15000);
						if(map[saved] == 'f');
							map[saved] = 'b';
					} catch(Exception e) {}
				}
			}.start();
		}
	}
	
	private static void boom(int addr) {
		final int saved = addr;
		new Thread() {
			public void run() {
				try {
					
					try {
						if(map[saved] != 'R')         map[saved] = '.';
						if(map[saved + 1] != 'R')     map[saved + 1] = '.';
						if(map[saved - 1] != 'R')     map[saved - 1] = '.';
						if(map[saved + WIDTH] != 'R') map[saved + WIDTH] = '.';
						if(map[saved - WIDTH] != 'R') map[saved - WIDTH] = '.';
						fire(saved - 2);
						fire(saved + 2);
						fire(saved + WIDTH * 2);
					} catch(ArrayIndexOutOfBoundsException e) {}
					for(int i = 0; i < 7; i++) {
						if(map[saved] != 'R')
							map[saved] = (char)((int)'0' + i);
						if(!slow)
							Thread.sleep(30);
						else
							Thread.sleep(110);
					}
					if(map[saved] != 'R')
						map[saved] = '.';
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}
	
	public static void main(String[] args) {
		try {
			/* загрузка карты */
			
			Scanner sc = new Scanner(new File(".map"));
			int ii = 0;
			while(sc.hasNextLine()) {
				char[] line = sc.nextLine().toCharArray();
				for(int i = 0; i < WIDTH; i++)
					map[ii++] = line[i];
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
		fr.setDefaultCloseOperation(3);
		fr.setLayout(null);
		Main p = new Main();
		p.setLayout(null);
		p.setBounds(0, 0, 1024, 728);
		fr.add(p);
		fr.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				try {
					if(select && e.getKeyChar() != 65535) {
						map[selectedBlockAddr()] = e.getKeyChar();
						select = false;
						return;
					}
					
					if(!block) {
						/* перемещение камеры */
						if(e.getKeyCode() == KeyEvent.VK_UP)
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
						else if(e.getKeyCode() == KeyEvent.VK_F4 && map[selectedBlockAddr()] != '.') {
							for(int i = selectedBlockAddr() - 1; map[i] == '.'; i--)
								map[i] = map[selectedBlockAddr()];
						}
						else if(e.getKeyCode() == KeyEvent.VK_F5 && map[selectedBlockAddr()] != '.') {
							for(int i = selectedBlockAddr() + 1; map[i] == '.'; i++)
								map[i] = map[selectedBlockAddr()];
						}
						else if(e.getKeyCode() == KeyEvent.VK_F6 && map[selectedBlockAddr()] != '.') {
							for(int i = selectedBlockAddr() + WIDTH; map[i] == '.'; i += WIDTH)
								map[i] = map[selectedBlockAddr()];
						}
						else if(e.getKeyCode() == KeyEvent.VK_F7 && map[selectedBlockAddr()] != '.') {
							for(int i = selectedBlockAddr() - WIDTH; map[i] == '.'; i -= WIDTH)
								map[i] = map[selectedBlockAddr()];
						}
						
						else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && ui) {
							// сохранение карты
							
							Files.deleteIfExists(Paths.get(".map"));
							FileWriter fw = new FileWriter(".map");
							int iii = 0;
							for(int i = 0; i < HEIGHT; i++) {
								for(int ii = 0; ii < WIDTH; ii++) {
									fw.write(map[iii]);
									iii++;
								}
								fw.write("\n");
							}
							fw.close();
							
							//////////////////////
							
							help = false;
							ui = false;
						} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !ui)
							ui = true;
						/* удаление блока */
						else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
							map[selectedBlockAddr()] = '.';
						else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
							map[selected] = '.';
							selected = -1;
						/* поставить огонь */
						} else if(e.getKeyCode() == KeyEvent.VK_F && map[selectedBlockAddr()] != '.' &&
						         (map[selectedBlockAddr()] == 'B' || map[selectedBlockAddr()] == 'l' || map[selectedBlockAddr()] == '|' ||
						          map[selectedBlockAddr()] == '@' || map[selectedBlockAddr()] == 'd' || map[selectedBlockAddr()] == 'w' ||
						          map[selectedBlockAddr()] == 'c' || map[selectedBlockAddr()] == 'C')) {
							fire(selectedBlockAddr());
						}
						/* поставить воду */
						else if(e.getKeyCode() == KeyEvent.VK_COMMA)
							map[selectedBlockAddr()] = 'W';
						/* поставить слизь */
						else if(e.getKeyCode() == KeyEvent.VK_Z)
							map[selectedBlockAddr()] = 'z';
						/* поставить бомбу */
						else if(e.getKeyChar() == '*')
							map[selectedBlockAddr()] = '"';
						/* поставить res/b.png */
						else if(e.getKeyCode() == KeyEvent.VK_B)
							map[selectedBlockAddr()] = 'B';
						/* поставить коробку */
						else if(e.getKeyCode() == KeyEvent.VK_X)
							map[selectedBlockAddr()] = '@';
						/* поставить чёрную дыру */
						else if(e.getKeyChar() == ';')
							map[selectedBlockAddr()] = 'A';
						/* поставить решётку */
						else if(e.getKeyCode() == KeyEvent.VK_E && selected == -1)
							map[selectedBlockAddr()] = '#';
						/* поставить res/bricks.png */
						else if(e.getKeyChar() == 'r')
							map[selectedBlockAddr()] = 'r';
						/* поставить res/superbricks.png */
						else if(e.getKeyChar() == 'R')
							map[selectedBlockAddr()] = 'R';
						/* поставить лестницу */
						else if(e.getKeyCode() == KeyEvent.VK_L)
							map[selectedBlockAddr()] = 'l';
						/* поставить автомобиль */
						else if(e.getKeyCode() == KeyEvent.VK_C)
							map[selectedBlockAddr()] = 'c';
						/* поставить танк */
						else if(e.getKeyChar() == '=')
							map[selectedBlockAddr()] = '(';
						/* поставить стекло */
						else if(e.getKeyCode() == KeyEvent.VK_G && map[selectedBlockAddr() + WIDTH] != '.')
							map[selectedBlockAddr()] = 'g';
						/* поставить res/stone.png */
						else if(e.getKeyCode() == KeyEvent.VK_T)
							map[selectedBlockAddr()] = 't';
						/* поставить res/W.png */
						else if(e.getKeyCode() == KeyEvent.VK_W && selected == -1)
							map[selectedBlockAddr()] = 'w';
						/* поставить жёлтый блок */
						else if(e.getKeyCode() == KeyEvent.VK_Y)
							map[selectedBlockAddr()] = 'y';
						/* поставить огнестрельное оружие */
						else if(e.getKeyCode() == KeyEvent.VK_U)
							map[selectedBlockAddr()] = '[';
						/* поставить res/sand.png */
						else if(e.getKeyCode() == KeyEvent.VK_S && selected == -1)
							map[selectedBlockAddr()] = 's';
						/* поставить кровать */
						else if(e.getKeyCode() == KeyEvent.VK_N)
							map[selectedBlockAddr()] = 'd';
						/* поставить дверь/палку */
						else if(e.getKeyCode() == KeyEvent.VK_D && selected == -1)
							map[selectedBlockAddr()] = '|';
						/* поставить свет */
						else if(e.getKeyCode() == KeyEvent.VK_O)
							map[selectedBlockAddr()] = '^';
						/* поставить вертолёт */
						else if(e.getKeyCode() == KeyEvent.VK_I)
							map[selectedBlockAddr()] = '/';
						/* поставить "ещё какие-то блоки" */
						else if(e.getKeyCode() == KeyEvent.VK_1)
							map[selectedBlockAddr()] = 'N';
						else if(e.getKeyCode() == KeyEvent.VK_2)
							map[selectedBlockAddr()] = 'J';
						else if(e.getKeyCode() == KeyEvent.VK_3)
							map[selectedBlockAddr()] = 'O';
						else if(e.getKeyCode() == KeyEvent.VK_4)
							map[selectedBlockAddr()] = 'L';
						else if(e.getKeyCode() == KeyEvent.VK_5)
							map[selectedBlockAddr()] = 'K';
						else if(e.getKeyCode() == KeyEvent.VK_6)
							map[selectedBlockAddr()] = 'U';
						else if(e.getKeyCode() == KeyEvent.VK_7)
							map[selectedBlockAddr()] = 'Y';
						else if(e.getKeyCode() == KeyEvent.VK_8)
							map[selectedBlockAddr()] = 'v';
						else if(e.getKeyCode() == KeyEvent.VK_9)
							map[selectedBlockAddr()] = 'V';
						else if(e.getKeyCode() == KeyEvent.VK_0)
							map[selectedBlockAddr()] = ';';
						/* выбрать блок */
						else if(e.getKeyCode() == KeyEvent.VK_SPACE && selected == -1 && map[selectedBlockAddr()] != '.')
							selected = selectedBlockAddr();
						/* убрать выделение */
						else if(e.getKeyCode() == KeyEvent.VK_SPACE && selected != -1)
							selected = -1;
						/* вставить блок */
						else if(e.getKeyChar() == '+') {
							select = true;
						}
						/* отражение объектов */
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == 'c')
							map[selected] = 'C';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == 'C')
							map[selected] = 'c';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == '[')
							map[selected] = ']';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == ']')
							map[selected] = '[';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == '(')
							map[selected] = ')';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == ')')
							map[selected] = '(';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == '/')
							map[selected] = '\\';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == '\\')
							map[selected] = '/';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == ';')
							map[selected] = ':';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == ':')
							map[selected] = ';';
						/* взрыв */
						else if(e.getKeyCode() == KeyEvent.VK_ENTER && selected == -1 && ((int)map[selectedBlockAddr()] > (int)'9' || (int)map[selectedBlockAddr()] < (int)'0')) {
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
						else if(e.getKeyCode() == KeyEvent.VK_ENTER && map[selected] == '[')
							map[selected] = '{';
						else if(e.getKeyCode() == KeyEvent.VK_ENTER && map[selected] == ']')
							map[selected] = '}';
						else if(e.getKeyCode() == KeyEvent.VK_ENTER && map[selected] == '(') {
							map[selected] = '<';
							boom(selected + 8 - WIDTH);
						} else if(e.getKeyCode() == KeyEvent.VK_ENTER && map[selected] == ')') {
							map[selected] = '>';
							boom(selected - 8 - WIDTH);
						}
						/* операции с выделенным блоком */
						else if(e.getKeyCode() == KeyEvent.VK_W && selected != -1 && (map[selected - WIDTH] == '.' || map[selected - WIDTH] == 'g' || map[selected - WIDTH] == 'W') && map[selected] != 'f') {
							if(slow)
								Thread.sleep(50);
							map[selected - WIDTH] = map[selected];
							map[selected] = '.';
							selected -= WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_S && selected != -1 && (map[selected + WIDTH] == '.' || map[selected + WIDTH] == 'g' || map[selected + WIDTH] == 'W') && map[selected] != 'f') {
							if(slow)
								Thread.sleep(50);
							map[selected + WIDTH] = map[selected];
							map[selected] = '.';
							selected += WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_A && selected != -1 && (map[selected - 1] == '.' || map[selected - 1] == 'g' || map[selected - 1] == 'W') && map[selected] != 'f') {
							if(slow)
								Thread.sleep(50);
							map[selected - 1] = map[selected];
							map[selected] = '.';
							selected--;
						} else if(e.getKeyCode() == KeyEvent.VK_D && selected != -1 && (map[selected + 1] == '.' || map[selected + 1] == 'g' || map[selected + 1] == 'W') && map[selected] != 'f') {
							if(slow)
								Thread.sleep(50);
							map[selected + 1] = map[selected];
							map[selected] = '.';
							selected++;
						}
						else if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
							noWater = true;
						}
						
						else if(e.getKeyCode() == KeyEvent.VK_F3 && !darkMode)
							darkMode = true;
						else if(e.getKeyCode() == KeyEvent.VK_F3 && darkMode)
							darkMode = false;
						
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
		
		new Thread() {
			public void run() {
				while(true) {
					try {
						/* "физика" */
						for(int i = 0; i < map.length - WIDTH; i++) {
							if(selected == -1 && (map[i] == '@' ||  map[i] == 's' || map[i] == '|' ||  map[i] == 'd' || map[i] == '#' ||
							    map[i] == 'l' || map[i] == 'c' || map[i] == 'C' || map[i] == '[' || map[i] == ']' || map[i] == '"' || map[i] == '(' || map[i] == ')' || map[i] == ';' || map[i] == ':') && (map[i + WIDTH] == '.' || map[i + WIDTH] == 'W' || map[i + WIDTH] == 'g') && (map[i - 1] != 'z' && map[i + 1] != 'z' && map[i - WIDTH] != 'z')) {
								map[i + WIDTH] = map[i];
								map[i] = '.';
								
								if(!slow)
									Thread.sleep(30);
								else
									Thread.sleep(110);
							} else if(map[i] == 'W' && !noWater) {
								if(map[i - 1] == 'f')
									map[i - 1] = 'b';
								if(map[i + 1] == 'f')
									map[i + 1] = 'b';
								if(map[i + WIDTH] == 'f')
									map[i + WIDTH] = 'b';
								if(map[i - WIDTH] == 'f')
									map[i - WIDTH] = 'b';
								
								if((map[i + WIDTH] != '.' && map[i + WIDTH] != 'W') || i + WIDTH * 2 > map.length) {
									if(map[i - 1] == '.' || map[i - 1] == 'd' || map[i - 1] == '|' || map[i - 1] == '#') {
										if(!slow)
											Thread.sleep(160);
										else
											Thread.sleep(400);
										map[i - 1] = 'W';
									}
									
									if(map[i + 1] == '.' || map[i + 1] == 'd' || map[i + 1] == '|' || map[i + 1] == '#') {
										if(!slow)
											Thread.sleep(160);
										else
											Thread.sleep(400);
										map[i + 1] = 'W';
									}
								}
								
								if(map[i + WIDTH] == '.' || map[i + WIDTH] == 'd' || map[i + WIDTH] == '|' || map[i + WIDTH] == '#')
									map[i + WIDTH] = 'W';
								
							} else if(noWater) {
								for(int ii = 0; ii < map.length; ii++)
									if(map[ii] == 'W')
										map[ii] = '.';
								noWater = false;
							} else if(map[i] == '/' && map[i + WIDTH] == '.')
								map[i] = 'i';
							else if(map[i] == '\\' && map[i + WIDTH] == '.')
								map[i] = 'I';
							else if(map[i] == 'i')
								map[i] = '/';
							else if(map[i] == 'I')
								map[i] = '\\';
							else if(map[i] == 'A') {
								for(int ii = 10; ii > 0; ii--) {
									try {
										if(map[i - ii] != 'A') map[i - ii] = '.';
										if(map[i + ii] != 'A') map[i + ii] = '.';
										if(map[i + WIDTH * ii] != 'A') map[i + WIDTH * ii] = '.';
										if(map[i - WIDTH * ii] != 'A') map[i - WIDTH * ii] = '.';
										if(map[i - WIDTH * ii - ii] != 'A') map[i - WIDTH * ii - ii] = '.';
										if(map[i - WIDTH * ii + ii] != 'A') map[i - WIDTH * ii + ii] = '.';
										if(map[i + WIDTH * ii - ii] != 'A') map[i + WIDTH * ii - ii] = '.';
										if(map[i + WIDTH * ii + ii] != 'A') map[i + WIDTH * ii + ii] = '.';
									} catch(Exception e) {}
									if(!slow)
										Thread.sleep(25);
									else
										Thread.sleep(140);
								}
							} else if(map[i] == ':' && (map[i - 1] == 'z' || map[i + 1] == 'z' || map[i - WIDTH] == 'z') || map[i + WIDTH] == 'z') {
								continue;
							} else if(map[i] == ':' && map[i - 1] == '.') {
								map[i] = '.';
								map[i - 1] = ':';
								Thread.sleep(!slow ? 400 : 1000);
							} else if(map[i] == ':'  && map[i - 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH - 1] == '.') {
								map[i] = '.';
								map[i - WIDTH - 1] = ':';
								Thread.sleep(!slow ? 400 : 1000);
							} else if(map[i] == ';'  && map[i + 1] != '.' && map[i - WIDTH] == '.' && map[i - WIDTH + 1] == '.') {
								map[i] = '.';
								map[i - WIDTH + 1] = ';';
								Thread.sleep(!slow ? 400 : 1000);
							} else if(map[i] == ':') {
								map[i] = ';';
							} else if(map[i] == ';' && map[i + 1] == '.') {
								map[i] = '.';
								map[i + 1] = ';';
								Thread.sleep(!slow ? 400 : 1000);
							} else if(map[i] == ';') {
								map[i] = ':';
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
					try {
						if(selected != -1 && (map[selected] == '.' || map[selected] == 'W' || map[selected] == 'R'))
							selected = -1;
						fr.repaint();
						Thread.sleep(30);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void paint(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, 1024, 728); g.fillRect(0, 0, 1024, 728);
		/* рисование объектов
		   ================== */
		int iii = cameraStart;
		for(int i = 0; i < HEIGHT; i++) {
			for(int ii = 0; ii < WIDTH; ii++) {
				try {
					if(map[iii] == '#') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/reshetka.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'l') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/lestnica.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'g') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/glass.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '|') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/door_or_stick.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'Y') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
					
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
					
						g.drawImage(new ImageIcon("res/door2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'd') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/bed.png").getImage(), ii * 60 - 60, i * 60, 120, 60, null);
					} else if(map[iii] == '^') {
						g.setColor(new Color(255, 255, 255));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'v') {
						g.setColor(new Color(255, 0, 0));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'b') {
						g.setColor(new Color(20, 20, 20));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'B')
						g.drawImage(new ImageIcon("res/B.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 's')
						g.drawImage(new ImageIcon("res/sand.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 't')
						g.drawImage(new ImageIcon("res/stone.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'r')
						g.drawImage(new ImageIcon("res/bricks.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'R')
						g.drawImage(new ImageIcon("res/superbricks.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'w')
						g.drawImage(new ImageIcon("res/W.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'N')
						g.drawImage(new ImageIcon("res/block0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'J') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/block1.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'O')
						g.drawImage(new ImageIcon("res/block2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'L')
						g.drawImage(new ImageIcon("res/block3.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'K') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/block4.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'U') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/pautina.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'W') {
						g.setColor(new Color(10, 10, 255));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'z') {
						g.setColor(new Color(0, 225, 0));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == '@')
						g.drawImage(new ImageIcon("res/box.png").getImage(), ii * 60 - 60, i * 60 - 60, 120, 120, null);
					else if(map[iii] == 'y') {
						g.setColor(new Color(255, 255, 0));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == '[') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/gun0_0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'V') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/B2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '{') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/gun0_1.png").getImage(), ii * 60, i * 60, 60, 60, null);
						map[iii] = '[';
					} else if(map[iii] == ']') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/gun1_0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == ':') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/left.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == ';') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/right.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '}') {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
						
						g.drawImage(new ImageIcon("res/gun1_1.png").getImage(), ii * 60, i * 60, 60, 60, null);
						map[iii] = ']';
					} else {
						if(!darkMode)
							g.setColor(new Color(80, 80, 80));
						else
							g.setColor(new Color(35, 35, 35));
						
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(!darkMode)
							g.setColor(new Color(140, 140, 140));
						else
							g.setColor(new Color(70, 70, 70));
						g.drawRect(ii * 60 , i * 60, 60, 60);
					}
				} catch(ArrayIndexOutOfBoundsException e) {}
				iii++;
			}
		}
		
		iii = cameraStart;
		for(int i = 0; i < HEIGHT; i++) {
			for(int ii = 0; ii < WIDTH; ii++) {
				try {
					if(Main.map[iii] == 'f')
						g.drawImage(new ImageIcon("res/fire.png").getImage(), ii * 60 - 30, i * 60 - 100, 180, 200, null);
					/* подсветка света */
					else if(Main.map[iii] == '^')
						g.drawImage(new ImageIcon("res/white.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					
					else if(Main.map[iii] == 'v')
						g.drawImage(new ImageIcon("res/red_.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					
					else if((int)Main.map[iii] > (int)'0' && (int)Main.map[iii] < (int)'9')
						g.drawImage(new ImageIcon("res/boom" + Main.map[iii] + ".png").getImage(), ii * 60 - 60, i * 60 - 60, 180, 180, null);
					else if(map[iii] == 'c')
						g.drawImage(new ImageIcon("res/car0.png").getImage(), ii * 60 - 60, i * 60 - 20, 180, 80, null);
					else if(map[iii] == 'C')
						g.drawImage(new ImageIcon("res/car1.png").getImage(), ii * 60 - 60, i * 60 - 20, 180, 80, null);
					else if(map[iii] == '/')
						g.drawImage(new ImageIcon("res/helicopter_00.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == '\\')
						g.drawImage(new ImageIcon("res/helicopter_10.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == 'i')
						g.drawImage(new ImageIcon("res/helicopter_01.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == 'I')
						g.drawImage(new ImageIcon("res/helicopter_11.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == '(')
						g.drawImage(new ImageIcon("res/tank0_0.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
					else if(map[iii] == ')')
						g.drawImage(new ImageIcon("res/tank1_0.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
					else if(map[iii] == '<') {
						g.drawImage(new ImageIcon("res/tank0_1.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
						map[iii] = '(';
					} else if(map[iii] == '>') {
						g.drawImage(new ImageIcon("res/tank1_1.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
						map[iii] = ')';
					} else if(map[iii] == '"') {
						g.drawImage(new ImageIcon("res/bomb.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'A') {
						g.drawImage(new ImageIcon("res/dyra.png").getImage(), ii * 60 - 60, i * 60 - 60, 120, 120, null);
					} else if(map[iii] == 'z') {
						g.drawImage(new ImageIcon("res/green.png").getImage(), ii * 60 - 15, i * 60 - 15, 90, 90, null);
					} else if((map[iii] >= 'а' && map[iii] <= 'я') || (map[iii] >= 'А' && map[iii] <= 'Я') || map[iii] == '-' || map[iii] == '!' || map[iii] == '?') {
						g.setColor(new Color(255, 255, 255));
						g.setFont(new Font("Monospaced", Font.PLAIN, 60));
						g.drawString("" + map[iii], ii * 60 + 15, i * 60 + 60);
					}
					
					/* подсветка выбранного блока */
					if(iii == selected && ui) {
						g.setColor(new Color(0, 255, 0));
						g.drawRect(ii * 60, i * 60, 60, 60);
					}
				} catch(ArrayIndexOutOfBoundsException e) {}
				iii++;
			}
		}
		/*********************************/
		
		if(ui) {
			iii = cameraStart;
			for(int i = 0; i < HEIGHT; i++) {
				for(int ii = 0; ii < WIDTH; ii++) {
					try {
						if(forBoom[iii]) {
							g.drawImage(new ImageIcon("res/red.png").getImage(), ii * 60 - 60, i * 60, 180, 60, null);
							g.drawImage(new ImageIcon("res/red.png").getImage(), ii * 60, i * 60 - 60, 60, 180, null);
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
					iii++;
				}
			}
			
			g.drawImage(new ImageIcon("res/pricel.png").getImage(), 1024 / 2 - 7, 700 / 2 - 20, 12, 8, null);
			g.drawImage(new ImageIcon("res/vignette.png").getImage(), 0, 0, 1024, 728, null);
			
			g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
			g.setColor(new Color(255, 255, 0));
			if(!select)
				g.drawString("" + fill((System.currentTimeMillis() - startTime) / 60000, 2) + ":" + fill((System.currentTimeMillis() - startTime) / 1000 % 60, 2), 15, 20);
			else
				g.drawString("Введите символ...", 15, 20);
			
			g.setColor(new Color(250, 250, 250));
			g.setFont(new Font("Monospaced", Font.PLAIN, 15));
			
			if(slow)
				g.drawString("~", 970, 20);
			
			if(help) {
				g.drawImage(new ImageIcon("res/black.png").getImage(), 0, 0, 1024, 728, null);
				
				g.drawString("<стрелки>.......:  перемещение", 20, 20);
				g.drawString("<F1>............:  скрыть/показать эту помощь", 20, 40);
				
				g.drawString("<ESC>...........:  скрыть интерфейс и сохранить карту, либо показать интерфейс", 20, 80);
				g.drawString("<Backspace>.....:  удалить объект под прицелом", 20, 100);
				
				g.drawString("<Delete>........:  удалить выделенный объект", 20, 130);
				g.drawString("<Space>.........:  выделить объект под прицелом, либо убрать выделение", 20, 150);
				g.drawString("WASD............:  перемещение выделенного объекта", 20, 170);
				g.drawString("QE..............:  отразить выделенный объект (не для всех)", 20, 190);
				
				g.drawString("B...............:  поставить коричневый блок", 20, 220);
				g.drawString("X...............:  поставить коробку", 20, 240);
				g.drawString("E...............:  поставить решётку", 20, 260);
				g.drawString("rR..............:  поставить кирпичный блок", 20, 280);
				g.drawString("L...............:  поставить лестницу", 20, 300);
				g.drawString("G...............:  поставить стекло", 20, 320);
				g.drawString("T...............:  поставить камень", 20, 340);
				g.drawString("Y...............:  поставить жёлтый блок", 20, 360);
				g.drawString("S...............:  поставить песок", 20, 380);
				g.drawString("N...............:  поставить кровать", 20, 400);
				g.drawString("D...............:  поставить палку (можете называть и дверью)", 20, 420);
				g.drawString("O...............:  поставить лампу", 20, 440);
				g.drawString("<Enter>.........:  сделать взрыв пом. клавишей <Insert> или активировать выдел. объект, '-' для уд. выд.", 20, 460);
				g.drawString("<F2>............:  включить/выключить замедление времени (оно работает не во всех случаях)", 20, 480);
				g.drawString("W1234567890.....:  (ещё какие-то блоки, включая ковёр и паутину)", 20, 500);
				g.drawString("C...............:  поставить автомобиль, 'i' для вертолёта", 20, 520);
				g.drawString("U...............:  поставить огнестрельное оружие", 20, 540);
				g.drawString("=...............:  поставить танк", 20, 560);
				g.drawString("F...............:  огонь, для установки чёрной дыры, нажмите ';'", 20, 580);
				g.drawString("<запятая>.......:  вода, для удаления всей воды нажмите '.'", 20, 600);
				g.drawString("*...............:  бомба, которая не работает", 20, 620);
				g.drawString("Z...............:  слизь (зелёный блок); попробуйте также клавиши <F4>, <F5>, <F6> и <F7>", 20, 640);
				
				g.drawString("<F3>............:  тёмный/светлый режим; '+' для вставки буквы или других доступных символов;", 20, 670);
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

