package downadow.items_jlayground.main;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static final int WIDTH = 200, HEIGHT = 50;
	/* карта */
	private static char[] map = new char[WIDTH * HEIGHT];
	
	private static boolean[] forBoom = new boolean[WIDTH * HEIGHT];
	
	private static boolean ph = true, following = false;
	
	private static Color bgColor = new Color(80, 80, 80);
	
	private static int behaviorSelected2 = -1;
	
	private static boolean programmingMode = false;
	
	private static String behavior = "";
	private static int behaviorSelected = 0;
	
	private static boolean ui = true;
	/* блокировка управления */
	private static boolean block = false;
	/* замедленное время */
	private static boolean slow = false;
	
	private static boolean noWater = false;
	
	private static boolean select = false, selectz = false;
	
	private static boolean fill = false;
	
	private static int cameraStart = 32 + 40 * WIDTH;
	/* выбранный блок */
	private static int selected = -1;
	/* вывести помощь */
	private static boolean help = true;
	
	private static int physics = WIDTH;
	
	private static String helpMessage = "";
	
	private static JFrame fr;
	
	/* возвращает адрес блока, на который указывает прицел */
	private static int selectedBlockAddr() {
		return cameraStart + 8 + 5 * WIDTH;
	}
	
	private static long startTime = 0;
	
	private static void fire(int addr) {
		final int saved = addr;
		if(map[saved] == 'B' || map[saved] == 'l' || map[saved] == '|' ||
		   map[saved] == '@' || map[saved] == 'd' || map[saved] == 'w' || map[saved] == 'M' || map[saved] == 'm' ||
		   map[saved] == 'c' || map[saved] == 'C' || map[saved] == 'L' || map[saved] == 'X') {
			map[saved] = 'f';

			new Thread() {
				public void run() {
					try {
						for(int i = 0; i < 6; i++) {
							Thread.sleep(5000);
							if(map[saved] != 'f')
								return;
							if(map[saved - 1] == 'B' || map[saved - 1] == 'l' || map[saved - 1] == '|' ||
							   map[saved - 1] == '@' || map[saved - 1] == 'd' || map[saved - 1] == 'w' || map[saved - 1] == 'M' || map[saved - 1] == 'm' ||
							   map[saved - 1] == 'c' || map[saved - 1] == 'C' || map[saved - 1] == 'L' || map[saved - 1] == 'X')
								fire(saved - 1);
							if(map[saved + 1] == 'B' || map[saved + 1] == 'l' || map[saved + 1] == '|' ||
							   map[saved + 1] == '@' || map[saved + 1] == 'd' || map[saved + 1] == 'w' || map[saved + 1] == 'M' || map[saved + 1] == 'm' ||
							   map[saved + 1] == 'c' || map[saved + 1] == 'C' || map[saved + 1] == 'L' || map[saved + 1] == 'X')
								fire(saved + 1);
							if(map[saved + WIDTH] == 'B' || map[saved + WIDTH] == 'l' || map[saved + WIDTH] == '|' ||
							   map[saved + WIDTH] == '@' || map[saved + WIDTH] == 'd' || map[saved + WIDTH] == 'w' || map[saved + WIDTH] == 'm' || map[saved + WIDTH] == 'M' ||
							   map[saved + WIDTH] == 'c' || map[saved + WIDTH] == 'C' || map[saved + WIDTH] == 'L' || map[saved + WIDTH] == 'X')
								fire(saved + WIDTH);
							if(map[saved - WIDTH] == 'B' || map[saved - WIDTH] == 'l' || map[saved - WIDTH] == '|' ||
							   map[saved - WIDTH] == '@' || map[saved - WIDTH] == 'd' || map[saved - WIDTH] == 'w' || map[saved - WIDTH] == 'm' || map[saved - WIDTH] == 'M' ||
							   map[saved - WIDTH] == 'c' || map[saved - WIDTH] == 'C' || map[saved - WIDTH] == 'L' || map[saved - WIDTH] == 'X')
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
	
	private static void fire2(int addr) {
		final int saved = addr;
		map[saved] = 'F';

		new Thread() {
			public void run() {
				try {
					Thread.sleep(!slow ? 500 : 1200);
					if(map[saved - 1] != '.' && map[saved - 1] != 'F' && map[saved - 1] != 'W')
						fire2(saved - 1);
					if(map[saved + 1] != '.' && map[saved + 1] != 'F' && map[saved + 1] != 'W')
						fire2(saved + 1);
					if(map[saved + WIDTH] != '.' && map[saved + WIDTH] != 'F' && map[saved + WIDTH] != 'W')
						fire2(saved + WIDTH);
					if(map[saved - WIDTH] != '.' && map[saved - WIDTH] != 'F' && map[saved - WIDTH] != 'W')
						fire2(saved - WIDTH);
					if(map[saved - WIDTH - 1] != '.' && map[saved - WIDTH - 1] != 'F' && map[saved - WIDTH - 1] != 'W')
						fire2(saved - WIDTH - 1);
					if(map[saved - WIDTH + 1] != '.' && map[saved - WIDTH + 1] != 'F' && map[saved - WIDTH + 1] != 'W')
						fire2(saved - WIDTH + 1);
					if(map[saved + WIDTH - 1] != '.' && map[saved + WIDTH - 1] != 'F' && map[saved + WIDTH - 1] != 'W')
						fire2(saved + WIDTH - 1);
					if(map[saved + WIDTH + 1] != '.' && map[saved + WIDTH + 1] != 'F' && map[saved + WIDTH + 1] != 'W')
						fire2(saved + WIDTH + 1);
					
					Thread.sleep(!slow ? 7000 : 15000);
					map[saved] = '.';
				} catch(Exception e) {}
			}
		}.start();
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
						
						if(!(map[saved - 1 - WIDTH] >= '0' && map[saved - 1 - WIDTH] <= '9') && map[saved - 1 - WIDTH] != 'R' && map[saved - 1 - WIDTH] != '.') {
							map[saved - 2 - WIDTH * 2] = map[saved - 1 - WIDTH];
							map[saved - 1 - WIDTH] = '.';
						}
						
						if(!(map[saved + 1 - WIDTH] >= '0' && map[saved + 1 - WIDTH] <= '9') && map[saved + 1 - WIDTH] != 'R' && map[saved + 1 - WIDTH] != '.') {
							map[saved + 2 - WIDTH * 2] = map[saved + 1 - WIDTH];
							map[saved + 1 - WIDTH] = '.';
						}
						
						if(!(map[saved + 1 + WIDTH] >= '0' && map[saved + 1 + WIDTH] <= '9') && map[saved + 1 + WIDTH] != 'R' && map[saved + 1 + WIDTH] != '.') {
							map[saved + 2 + WIDTH * 2] = map[saved + 1 + WIDTH];
							map[saved + 1 + WIDTH] = '.';
						}
						
						if(!(map[saved - 1 + WIDTH] >= '0' && map[saved - 1 + WIDTH] <= '9') && map[saved - 1 + WIDTH] != 'R' && map[saved - 1 + WIDTH] != '.') {
							map[saved - 2 + WIDTH * 2] = map[saved - 1 + WIDTH];
							map[saved - 1 + WIDTH] = '.';
						}
						
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
	
	private static void shootRight(int addr) {
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
	                if(map[i + 1] != 'f' && map[i + 1] != 'F' && map[i + 1] != 'R')
	                    map[i + 1] = '.';
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}
	private static void shootLeft(int addr) {
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
	                map[i] = '.';
	                if(map[i - 1] != 'f' && map[i - 1] != 'F' && map[i - 1] != 'R')
	                    map[i - 1] = '.';
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}
	
	public static void main(String[] args) {
		try {
			/* загрузка карты, поведения и текста помощи */
			
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
					/* режим программирования поведения */
					if(programmingMode) {
						if(select && e.getKeyChar() != ' ' && e.getKeyChar() != ':' && e.getKeyChar() != (char)65535) {
							behavior += "set:" + e.getKeyChar() + " ";
							select = false;
							return;
						} else if(selectz && e.getKeyCode() == KeyEvent.VK_W) {
							behavior += "wait:up ";
							selectz = false;
							return;
						} else if(selectz && e.getKeyCode() == KeyEvent.VK_S) {
							behavior += "wait:down ";
							selectz = false;
							return;
						} else if(selectz && e.getKeyCode() == KeyEvent.VK_A) {
							behavior += "wait:left ";
							selectz = false;
							return;
						} else if(selectz && e.getKeyCode() == KeyEvent.VK_D) {
							behavior += "wait:right ";
							selectz = false;
							return;
						}
						
						if(e.getKeyCode() == KeyEvent.VK_HOME) {
							behavior = "";
							behaviorSelected2 = -1;
						} else if(e.getKeyChar() == 'W') {
							behavior += "up:copy ";
							behaviorSelected2 -= WIDTH;
						} else if(e.getKeyChar() == 'S') {
							behavior += "down:copy ";
							behaviorSelected2 += WIDTH;
						} else if(e.getKeyChar() == 'A') {
							behavior += "left:copy ";
							behaviorSelected2--;
						} else if(e.getKeyChar() == 'D') {
							behavior += "right:copy ";
							behaviorSelected2++;
						} else if(e.getKeyCode() == KeyEvent.VK_W) {
							behavior += "up ";
							behaviorSelected2 -= WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_S) {
							behavior += "down ";
							behaviorSelected2 += WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_A) {
							behavior += "left ";
							behaviorSelected2--;
						} else if(e.getKeyCode() == KeyEvent.VK_D) {
							behavior += "right ";
							behaviorSelected2++;
						} else if(e.getKeyCode() == KeyEvent.VK_I) {
							behavior += "up:lift ";
							behaviorSelected2 -= WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_K) {
							behavior += "down:lift ";
							behaviorSelected2 += WIDTH;
						} else if(e.getKeyCode() == KeyEvent.VK_J) {
							behavior += "left:lift ";
							behaviorSelected2--;
						} else if(e.getKeyCode() == KeyEvent.VK_L) {
							behavior += "right:lift ";
							behaviorSelected2++;
						} else if(e.getKeyChar() == 'f') {
							behavior += "fire ";
						} else if(e.getKeyCode() == KeyEvent.VK_F) {
							behavior += "fire2 ";
						} else if(e.getKeyCode() == KeyEvent.VK_T) {
							behavior += "tp:" + selectedBlockAddr() + " ";
							behaviorSelected2 = selectedBlockAddr();
						} else if(e.getKeyCode() == KeyEvent.VK_B) {
							behavior += "boom ";
						} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
							behavior += "sel:" + selectedBlockAddr() + " ";
							behaviorSelected2 = selectedBlockAddr();
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
						
						else if(e.getKeyCode() == KeyEvent.VK_0)
							behavior += "~50 ";
						else if(e.getKeyCode() == KeyEvent.VK_1)
							behavior += "~100 ";
						else if(e.getKeyCode() == KeyEvent.VK_2)
							behavior += "~200 ";
						else if(e.getKeyCode() == KeyEvent.VK_3)
							behavior += "~300 ";
						else if(e.getKeyCode() == KeyEvent.VK_4)
							behavior += "~400 ";
						else if(e.getKeyCode() == KeyEvent.VK_5)
							behavior += "~500 ";
						else if(e.getKeyCode() == KeyEvent.VK_6)
							behavior += "~600 ";
						else if(e.getKeyCode() == KeyEvent.VK_7)
							behavior += "~700 ";
						else if(e.getKeyCode() == KeyEvent.VK_8)
							behavior += "~800 ";
						else if(e.getKeyCode() == KeyEvent.VK_9)
							behavior += "~1000 ";
						else if(e.getKeyCode() == KeyEvent.VK_MINUS)
							behavior += "~5000 ";
						else if(e.getKeyChar() == 'Z')
							behavior += "wait ";
						else if(e.getKeyChar() == 'z') {
							selectz = true;
						} else if(e.getKeyChar() == (char)24 /* Ctrl+X */)
							behavior += "no_sel ";
						else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
							String[] behavior2 = behavior.split(" ");
							behavior = "";
							for(int i = 0; i < behavior2.length - 1; i++)
								behavior += behavior2[i] + " ";
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
										sb_fr.setVisible(false);
									}
								}
								public void keyTyped(KeyEvent e) {}
								public void keyReleased(KeyEvent e) {}
							});
		
							sb_b.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									behavior = sb_tf.getText();
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
					if(select && e.getKeyChar() != 65535) {
						map[selectedBlockAddr()] = e.getKeyChar();
						select = false;
						return;
					}
					
					else if(e.getKeyCode() == KeyEvent.VK_LEFT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - 1] == '.') {
						for(int i = selectedBlockAddr() - 1; map[i] == '.'; i--)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + 1] == '.') {
						for(int i = selectedBlockAddr() + 1; map[i] == '.'; i++)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_DOWN && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() + WIDTH] == '.') {
						for(int i = selectedBlockAddr() + WIDTH; map[i] == '.'; i += WIDTH)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP && fill && map[selectedBlockAddr()] != '.' && map[selectedBlockAddr() - WIDTH] == '.') {
						for(int i = selectedBlockAddr() - WIDTH; map[i] == '.'; i -= WIDTH)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					
					else if(e.getKeyCode() == KeyEvent.VK_LEFT && fill && map[selectedBlockAddr()] != '.') {
						for(int i = selectedBlockAddr() - 1; map[i] != '.'; i--)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT && fill && map[selectedBlockAddr()] != '.') {
						for(int i = selectedBlockAddr() + 1; map[i] != '.'; i++)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_DOWN && fill && map[selectedBlockAddr()] != '.') {
						for(int i = selectedBlockAddr() + WIDTH; map[i] != '.'; i += WIDTH)
							map[i] = map[selectedBlockAddr()];
						fill = false;
						return;
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP && fill && map[selectedBlockAddr()] != '.') {
						for(int i = selectedBlockAddr() - WIDTH; map[i] != '.'; i -= WIDTH)
							map[i] = map[selectedBlockAddr()];
						fill = false;
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
					else if(e.getKeyCode() == KeyEvent.VK_F6 && !ph) {
						ph = true;
						return;
					} else if(e.getKeyCode() == KeyEvent.VK_F6 && ph) {
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
						else if(e.getKeyCode() == KeyEvent.VK_F8) {
							for(int i = 0; i < map.length; i++)
								if(map[i] != '.' && map[i] != 's')
									map[i] = 's';
						}
						else if(e.getKeyCode() == KeyEvent.VK_F9) {
							for(int i = map.length - WIDTH - 1; i >= 0; i--) {
								map[i + WIDTH] = map[i];
								map[i] = '.';
							}
						}
						else if(e.getKeyCode() == KeyEvent.VK_F10 && physics == WIDTH)
							physics = -WIDTH;
						else if(e.getKeyCode() == KeyEvent.VK_F10 && physics == -WIDTH)
							physics = WIDTH;
						/*******************************/
						
						else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && ui) {
							// сохранение карты
							
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
						} else if(e.getKeyChar() == 'f' && map[selectedBlockAddr()] != '.' &&
						         (map[selectedBlockAddr()] == 'B' || map[selectedBlockAddr()] == 'l' || map[selectedBlockAddr()] == '|' ||
						          map[selectedBlockAddr()] == '@' || map[selectedBlockAddr()] == 'd' || map[selectedBlockAddr()] == 'w' ||
						          map[selectedBlockAddr()] == 'c' || map[selectedBlockAddr()] == 'C')) {
							fire(selectedBlockAddr());
						} else if(e.getKeyChar() == 'F' && map[selectedBlockAddr()] != '.')
							fire2(selectedBlockAddr());
						/* поставить воду */
						else if(e.getKeyCode() == KeyEvent.VK_COMMA)
							map[selectedBlockAddr()] = 'W';
						/* поставить стул */
						else if(e.getKeyCode() == KeyEvent.VK_M)
							map[selectedBlockAddr()] = 'M';
						/* поставить слизь */
						else if(e.getKeyCode() == KeyEvent.VK_Z)
							map[selectedBlockAddr()] = 'z';
						/* поставить "бомбу" */
						else if(e.getKeyChar() == '*')
							map[selectedBlockAddr()] = '"';
						/* поставить current/res/b.png */
						else if(e.getKeyCode() == KeyEvent.VK_B)
							map[selectedBlockAddr()] = 'B';
						/* поставить коробку */
						else if(e.getKeyChar() == 'x') {
							map[selectedBlockAddr() - 1] = '|';
							map[selectedBlockAddr() - 1 - WIDTH] = '|';
							map[selectedBlockAddr() - WIDTH] = '|';
							map[selectedBlockAddr()] = '@';
						}
						
						else if(e.getKeyChar() == 'X')
							map[selectedBlockAddr()] = 'X';
						/* поставить блок ускорения */
						else if(e.getKeyChar() == '~')
							map[selectedBlockAddr()] = '~';
						/* поставить '???' */
						else if(e.getKeyChar() == ';')
							map[selectedBlockAddr()] = 'A';
						/* поставить блок-батут */
						else if(e.getKeyChar() == '%')
							map[selectedBlockAddr()] = '%';
						/* поставить синий блок */
						else if(e.getKeyChar() == 'E' && selected == -1)
							map[selectedBlockAddr()] = 'E';
						/* поставить решётку */
						else if(e.getKeyCode() == KeyEvent.VK_E && selected == -1)
							map[selectedBlockAddr()] = '#';
						/* поставить компьютер */
						else if(e.getKeyCode() == KeyEvent.VK_H)
							map[selectedBlockAddr()] = 'h';
						/* поставить машину */
						else if(e.getKeyCode() == KeyEvent.VK_Q && selected == -1)
							map[selectedBlockAddr()] = 'q';
						/* поставить current/res/bricks.png */
						else if(e.getKeyChar() == 'r')
							map[selectedBlockAddr()] = 'r';
						/* поставить портал */
						else if(e.getKeyChar() == 'p')
							map[selectedBlockAddr()] = 'p';
						else if(e.getKeyChar() == 'P')
							map[selectedBlockAddr()] = 'P';
						/* поставить current/res/superbricks.png */
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
						/* поставить зелёный блок */
						else if(e.getKeyChar() == 'G')
							map[selectedBlockAddr()] = 'G';
						/* поставить стекло */
						else if(e.getKeyCode() == KeyEvent.VK_G && map[selectedBlockAddr() + WIDTH] != '.')
							map[selectedBlockAddr()] = 'g';
						/* поставить current/res/stone.png */
						else if(e.getKeyCode() == KeyEvent.VK_T)
							map[selectedBlockAddr()] = 't';
						/* поставить current/res/W.png */
						else if(e.getKeyCode() == KeyEvent.VK_W && selected == -1)
							map[selectedBlockAddr()] = 'w';
						/* поставить жёлтый блок */
						else if(e.getKeyCode() == KeyEvent.VK_Y)
							map[selectedBlockAddr()] = 'y';
						/* поставить радужный блок */
						else if(e.getKeyChar() == '$')
							map[selectedBlockAddr()] = '$';
						/* поставить огнестрельное оружие */
						else if(e.getKeyCode() == KeyEvent.VK_U)
							map[selectedBlockAddr()] = '[';
						/* поставить current/res/sand.png */
						else if(e.getKeyCode() == KeyEvent.VK_S && selected == -1)
							map[selectedBlockAddr()] = 's';
						/* поставить кровать */
						else if(e.getKeyCode() == KeyEvent.VK_N)
							map[selectedBlockAddr()] = 'd';
						/* поставить дверь/палку */
						else if(e.getKeyChar() == 'd' && selected == -1)
							map[selectedBlockAddr()] = '|';
						else if(e.getKeyChar() == 'D' && selected == -1)
							map[selectedBlockAddr()] = '&';
						/* поставить свет */
						else if(e.getKeyCode() == KeyEvent.VK_O)
							map[selectedBlockAddr()] = '^';
						/* поставить вертолёт */
						else if(e.getKeyCode() == KeyEvent.VK_I)
							map[selectedBlockAddr()] = '/';
						/* поставить минералку */
						else if(e.getKeyChar() == '`')
							map[selectedBlockAddr()] = '`';
						/* поставить полублок */
						else if(e.getKeyCode() == KeyEvent.VK_V)
							map[selectedBlockAddr()] = '№';
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
						/* бросать блок */
						else if(e.getKeyCode() == KeyEvent.VK_F11 && selected == -1)
						    shootLeft(selectedBlockAddr());
						else if(e.getKeyCode() == KeyEvent.VK_F12 && selected == -1)
						    shootRight(selectedBlockAddr());
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
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == '~')
							map[selected] = ',';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == ',')
							map[selected] = '~';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == 'M')
							map[selected] = 'm';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == 'm')
							map[selected] = 'M';
						else if(e.getKeyCode() == KeyEvent.VK_Q && map[selected] == 'q')
							map[selected] = 'Q';
						else if(e.getKeyCode() == KeyEvent.VK_E && map[selected] == 'Q')
							map[selected] = 'q';
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
							for(int i = 1; i < 16; i++) {
								if((map[selected + i - WIDTH] != '.' && !(map[selected + i - WIDTH] >= '0' && map[selected + i - WIDTH] <= '9')) || (map[selected + i] != '.' && !(map[selected + i] >= '0' && map[selected + i] <= '9'))) {
							        map[selected] = '<';
									boom(selected + i - WIDTH);
									break;
								}
							}
						} else if(e.getKeyCode() == KeyEvent.VK_ENTER && map[selected] == ')') {
							for(int i = 1; i < 16; i++) {
								if((map[selected - i - WIDTH] != '.' && !(map[selected - i - WIDTH] >= '0' && map[selected - i - WIDTH] <= '9')) || (map[selected - i] != '.' && !(map[selected - i] >= '0' && map[selected - i] <= '9'))) {
							        map[selected] = '>';
									boom(selected - i - WIDTH);
									break;
								}
							}
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
										bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
										setbg_fr.setVisible(false);
									}
								}
								public void keyTyped(KeyEvent e) {}
								public void keyReleased(KeyEvent e) {}
							});
							setbg_tfG.addKeyListener(new KeyListener() {
								public void keyPressed(KeyEvent e) {
									if(e.getKeyCode() == KeyEvent.VK_ENTER) {
										bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
										setbg_fr.setVisible(false);
									}
								}
								public void keyTyped(KeyEvent e) {}
								public void keyReleased(KeyEvent e) {}
							});
							setbg_tfB.addKeyListener(new KeyListener() {
								public void keyPressed(KeyEvent e) {
									if(e.getKeyCode() == KeyEvent.VK_ENTER) {
										bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
										setbg_fr.setVisible(false);
									}
								}
								public void keyTyped(KeyEvent e) {}
								public void keyReleased(KeyEvent e) {}
							});
							setbg_b.addActionListener(new java.awt.event.ActionListener() {
								public void actionPerformed(java.awt.event.ActionEvent e) {
									bgColor = new Color(Integer.parseInt(setbg_tfR.getText()), Integer.parseInt(setbg_tfG.getText()), Integer.parseInt(setbg_tfB.getText()));
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
		new Thread() {
			public void run() {
				while(true) {
					try {
					    if(physics == -WIDTH) {
						    for(int i = WIDTH; i < map.length - WIDTH; i++) {
							    if(ph) {
								    if(selected == -1 && (map[i] == '@' ||  map[i] == 's' || map[i] == '|' ||  map[i] == 'd' || map[i] == '#' ||
									    map[i] == 'l' || map[i] == 'c' || map[i] == 'M' || map[i] == 'm' || map[i] == 'C' || map[i] == '[' || map[i] == ']' || map[i] == '"' || map[i] == '(' || map[i] == ')' || map[i] == ';' || map[i] == ':' || map[i] == '`' || map[i] == 'q' || map[i] == 'Q') && (map[i + physics] == '.' || map[i + physics] == 'W' || map[i + physics] == 'b' || map[i + physics] == 'g') && (map[i - 1] != 'z' && map[i + 1] != 'z' && map[i - physics] != 'z')) {
									    map[i + physics] = map[i];
									    map[i] = '.';
								    }
							    }
						    }
						} else {
						    for(int i = map.length - WIDTH; i > WIDTH; i--) {
							    if(ph) {
								    if(selected == -1 && (map[i] == '@' ||  map[i] == 's' || map[i] == '|' ||  map[i] == 'd' || map[i] == '#' ||
									    map[i] == 'l' || map[i] == 'c' || map[i] == 'M' || map[i] == 'm' || map[i] == 'C' || map[i] == '[' || map[i] == ']' || map[i] == '"' || map[i] == '(' || map[i] == ')' || map[i] == ';' || map[i] == ':' || map[i] == '`' || map[i] == 'q' || map[i] == 'Q') && (map[i + physics] == '.' || map[i + physics] == 'W' || map[i + physics] == 'b' || map[i + physics] == 'g') && (map[i - 1] != 'z' && map[i + 1] != 'z' && map[i - physics] != 'z')) {
									    map[i + physics] = map[i];
									    map[i] = '.';
								    }
							    }
						    }
						}
						
						if(!slow)
							Thread.sleep(30);
						else
							Thread.sleep(110);
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
								if(map[i] == 'W' && !noWater) {
									if(map[i - 1] == 'f')
										map[i - 1] = 'b';
									if(map[i + 1] == 'f')
										map[i + 1] = 'b';
									if(map[i + physics] == 'f')
										map[i + physics] = 'b';
									if(map[i - physics] == 'f')
										map[i - physics] = 'b';
							
									if((map[i + physics] != '.' && map[i + physics] != 'W') || i + physics * 2 > map.length) {
										if(map[i - 1] == '.' || map[i - 1] == 'd' || map[i - 1] == '|' || map[i - 1] == '#' || map[i - 1] == 'l') {
											if(!slow)
												Thread.sleep(160);
											else
												Thread.sleep(400);
											map[i - 1] = 'W';
										}
								
										if(map[i + 1] == '.' || map[i + 1] == 'd' || map[i + 1] == '|' || map[i + 1] == '#' || map[i + 1] == 'l') {
											if(!slow)
												Thread.sleep(160);
											else
												Thread.sleep(400);
											map[i + 1] = 'W';
										}
									}
							
									if(map[i + physics] == '.' || map[i + physics] == 'd' || map[i + physics] == '|' || map[i + physics] == '#' || map[i + physics] == 'l')
										map[i + physics] = 'W';
							
								} else if(noWater) {
									for(int ii = 0; ii < map.length; ii++)
										if(map[ii] == 'W')
											map[ii] = '.';
									noWater = false;
								} else if(map[i] == '/' && map[i + physics] == '.')
									map[i] = 'i';
								else if(map[i] == '\\' && map[i + physics] == '.')
									map[i] = 'I';
								else if(map[i] == 'i')
									map[i] = '/';
								else if(map[i] == 'I')
									map[i] = '\\';
								else if(map[i] == 'A') {
									if(map[i - 1] == '.' || map[i - 1] == 'f' || map[i - 1] == 'W')
										map[i - 1] = 'A';
									if(map[i + 1] == '.' || map[i + 1] == 'f' || map[i + 1] == 'W')
										map[i + 1] = 'A';
									if(map[i + WIDTH] == '.' || map[i + WIDTH] == 'f' || map[i + WIDTH] == 'W')
										map[i + WIDTH] = 'A';
									if(map[i - WIDTH] == '.' || map[i - WIDTH] == 'f' || map[i - WIDTH] == 'W')
										map[i - WIDTH] = 'A';
							
									Thread.sleep(!slow ? 50 : 500);
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
								if(map[i] == '%' && (map[i - physics] != '.' && map[i - physics * 2] == '.' && map[i - physics * 3] == '.' && map[i - physics * 4] == '.')) {
									map[i - physics * 4] = map[i - physics];
									map[i - physics] = '.';
								}
								/* работа порталов */
								else if(map[i] == 'p') {
									for(int ii = 0; ii < map.length - 1; ii++) {
										if(map[ii] == 'P') {
											if(map[i + 1] != '.')
												map[ii - 1] = map[i + 1];
											if(map[i - 1] != '.')
												map[ii + 1] = map[i - 1];
											if(map[i - physics] != '.')
												map[ii + physics] = map[i - physics];
								
											map[i - 1] = '.';
											map[i + 1] = '.';
											map[i - physics] = '.';
											break;
										}
									}
								}
								/* работа ускорения */
								else if(map[i] == '~' && map[i - physics] != '.' && map[i - physics] != 'W' && map[i - physics] != '~' && map[i - physics + 1] == '.') {
									map[i - physics + 1] = map[i - physics];
									map[i - physics] = '.';
									Thread.sleep(!slow ? 200 : 500);
								} else if(map[i] == ',' && map[i - physics] != '.' && map[i - physics] != 'W' && map[i - physics] != '~' && map[i - physics - 1] == '.') {
									map[i - physics - 1] = map[i - physics];
									map[i - physics] = '.';
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
		
		/* обновление экрана и пр. */
		new Thread() {
			public void run() {
				while(true) {
					try {
						if(selected != -1 && (map[selected] == '.' || map[selected] == 'W' || map[selected] == 'R'))
							selected = -1;
						
						if(following && selected != -1)
							cameraStart = selected - 8 - 5 * WIDTH;
						else if(following && selected == -1)
							following = false;
						
						fr.repaint();
						Thread.sleep(20);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		/* логика стрелок */
		new Thread() {
			public void run() {
				while(true) {
						for(int i = 0; i < map.length; i++) {
							try {
								if(map[i] == ':' && (map[i - 1] == 'z' || map[i + 1] == 'z' || map[i - physics] == 'z') || map[i + physics] == 'z') {
									continue;
								} else if(map[i] == ':' && map[i - 1] == '.') {
									map[i] = '.';
									map[i - 1] = ':';
								} else if(map[i] == ':' && map[i - 1] != '.' && map[i - physics] == '.' && map[i - physics - 1] == '.') {
									map[i] = '.';
									map[i - physics - 1] = ':';
								} else if(map[i] == ';' && map[i + 1] != '.' && map[i - physics] == '.' && map[i - physics + 1] == '.') {
									map[i] = '.';
									map[i - physics + 1] = ';';
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
								if(behaviorSplitted[i].isEmpty())
									continue;
								else if(behaviorSplitted[i].split(":")[0].equals("sel") && behaviorSelected == 0) {
									behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
								} else if(behaviorSplitted[i].split(":")[0].equals("no_sel")) {
									behaviorSelected = 0;
								} else if(behaviorSplitted[i].equals("up")) {
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
								} else if(behaviorSplitted[i].split(":")[0].equals("fire")) {
									fire(behaviorSelected);
								} else if(behaviorSplitted[i].split(":")[0].equals("fire2")) {
									fire2(behaviorSelected);
								} else if(behaviorSplitted[i].split(":")[0].equals("boom")) {
									boom(behaviorSelected);
								} else if(behaviorSplitted[i].split(":")[0].equals("set")) {
									map[behaviorSelected] = behaviorSplitted[i].split(":")[1].toCharArray()[0];
								} else if(behaviorSplitted[i].startsWith("~")) {
									Thread.sleep(!slow ? Integer.parseInt(behaviorSplitted[i].replace("~", "")) : Integer.parseInt(behaviorSplitted[i].replace("~", "")) * 2);
								} else if(behaviorSplitted[i].equals("wait:up")) {
									while(map[behaviorSelected - WIDTH] == '.')
										Thread.sleep(40);
								} else if(behaviorSplitted[i].equals("wait:down")) {
									while(map[behaviorSelected + WIDTH] == '.')
										Thread.sleep(40);
								} else if(behaviorSplitted[i].equals("wait:right")) {
									while(map[behaviorSelected + 1] == '.')
										Thread.sleep(40);
								} else if(behaviorSplitted[i].equals("wait:left")) {
									while(map[behaviorSelected - 1] == '.')
										Thread.sleep(40);
								} else if(behaviorSplitted[i].equals("wait")) {
									while(map[behaviorSelected - 1] == '.'     &&
									      map[behaviorSelected + 1] == '.'     &&
									      map[behaviorSelected + WIDTH] == '.' &&
									      map[behaviorSelected - WIDTH] == '.')
										Thread.sleep(40);
								} else if(behaviorSplitted[i].startsWith("tp:")) {
									map[Integer.parseInt(behaviorSplitted[i].split(":")[1])] = map[behaviorSelected];
									map[behaviorSelected] = '.';
									behaviorSelected = Integer.parseInt(behaviorSplitted[i].split(":")[1]);
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
	
	public void paint(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, 1024, 728); g.fillRect(0, 0, 1024, 728);
		/* рисование объектов
		   ================== */
		int iii = cameraStart;
		for(int i = 0; i < HEIGHT; i++) {
			for(int ii = 0; ii < WIDTH; ii++) {
				try {
					if(iii < map.length && iii >= 0) {
						g.setColor(bgColor);
						g.fillRect(ii * 60, i * 60, 60, 60);
						if(ui) {
							try {g.setColor(bgColor.getRed() < 195 && bgColor.getGreen() < 195 && bgColor.getBlue() < 195 ? new Color(bgColor.getRed() + 60, bgColor.getGreen() + 60, bgColor.getBlue() + 60) : new Color(bgColor.getRed() - 60, bgColor.getGreen() - 60, bgColor.getBlue() - 60));} catch(IllegalArgumentException e) {}
							g.drawRect(ii * 60 , i * 60, 60, 60);
						}
					}
					
					if(map[iii] == '#') {
						g.drawImage(new ImageIcon("current/res/reshetka.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'l') {
						g.drawImage(new ImageIcon("current/res/lestnica.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'g') {
						g.drawImage(new ImageIcon("current/res/glass.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '|') {
						g.drawImage(new ImageIcon("current/res/door_or_stick.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '&') {
						g.drawImage(new ImageIcon("current/res/stick2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'Y') {
						g.drawImage(new ImageIcon("current/res/door2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'd') {
						g.drawImage(new ImageIcon("current/res/bed.png").getImage(), ii * 60 - 60, i * 60, 120, 60, null);
					} else if(map[iii] == '^') {
						g.setColor(new Color(255, 255, 255));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'v') {
						g.setColor(new Color(255, 0, 0));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'G') {
						g.setColor(new Color(0, 255, 0));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'b') {
						g.setColor(new Color(20, 20, 20));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'B')
						g.drawImage(new ImageIcon("current/res/B.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 's')
						g.drawImage(new ImageIcon("current/res/sand.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 't')
						g.drawImage(new ImageIcon("current/res/stone.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'r')
						g.drawImage(new ImageIcon("current/res/bricks.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'R')
						g.drawImage(new ImageIcon("current/res/superbricks.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'w')
						g.drawImage(new ImageIcon("current/res/W.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'N')
						g.drawImage(new ImageIcon("current/res/block0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'J') {
						g.setColor(new Color(0, 0, 0));
						g.fillRect(ii * 60, i * 60, 60, 60); g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'O')
						g.drawImage(new ImageIcon("current/res/block2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'L')
						g.drawImage(new ImageIcon("current/res/block3.png").getImage(), ii * 60, i * 60, 60, 60, null);
					else if(map[iii] == 'K') {
						g.drawImage(new ImageIcon("current/res/block4.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'U') {
						g.drawImage(new ImageIcon("current/res/pautina.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'W') {
						g.setColor(new Color(10, 10, 255));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'E') {
						g.setColor(new Color(0, 0, 255));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == 'z') {
						g.setColor(new Color(0, 225, 0));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == '@')
						g.drawImage(new ImageIcon("current/res/box.png").getImage(), ii * 60 - 60, i * 60 - 60, 120, 120, null);
					else if(map[iii] == 'y') {
						g.setColor(new Color(255, 255, 0));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.setColor(new Color(180, 180, 0));
						g.drawRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 + 1, i * 60 + 1, 58, 58);
					} else if(map[iii] == '[') {
						g.drawImage(new ImageIcon("current/res/gun0_0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'V') {
						g.drawImage(new ImageIcon("current/res/B2.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '{') {
						g.drawImage(new ImageIcon("current/res/gun0_1.png").getImage(), ii * 60, i * 60, 60, 60, null);
						map[iii] = '[';
					} else if(map[iii] == ']') {
						g.drawImage(new ImageIcon("current/res/gun1_0.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == ':') {
						g.drawImage(new ImageIcon("current/res/left.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == ';') {
						g.drawImage(new ImageIcon("current/res/right.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'A') {
						g.setColor(new Color(0, 0, 0));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == '~' || map[iii] == ',') {
						g.setColor(new Color(5, 5, 5));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
						g.setColor(new Color(60, 60, 60));
						g.fillRect(ii * 60 + 20, i * 60 + 20, 20, 20);
						g.drawRect(ii * 60 + 20, i * 60 + 20, 20, 20);
						
						if(ui)
							g.drawImage(new ImageIcon("current/res/" + (map[iii] == '~' ? "right" : "left") + ".png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '%') {
						g.setColor(new Color(255, 150, 0));
						g.fillRect(ii * 60, i * 60, 60, 60);
						g.drawRect(ii * 60 , i * 60, 60, 60);
					} else if(map[iii] == '}') {
						g.drawImage(new ImageIcon("current/res/gun1_1.png").getImage(), ii * 60, i * 60, 60, 60, null);
						map[iii] = ']';
					} else if(map[iii] == '`') {
						g.drawImage(new ImageIcon("current/res/voda.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == '$') {
						g.drawImage(new ImageIcon("current/res/rainbow.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} else if(map[iii] == 'h') {
						g.drawImage(new ImageIcon("current/res/computer.png").getImage(), ii * 60, i * 60, 60, 60, null);
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
						g.drawImage(new ImageIcon("current/res/fire.png").getImage(), ii * 60 - 30, i * 60 - 100, 180, 200, null);
					else if(Main.map[iii] == 'F')
						g.drawImage(new ImageIcon("current/res/fire2.png").getImage(), ii * 60 - 30, i * 60 - 100, 180, 200, null);
					/* подсветка света */
					else if(Main.map[iii] == '^')
						g.drawImage(new ImageIcon("current/res/white.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					
					else if(Main.map[iii] == 'v')
						g.drawImage(new ImageIcon("current/res/red_.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					else if(Main.map[iii] == 'G')
						g.drawImage(new ImageIcon("current/res/green_.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					else if(Main.map[iii] == 'J')
						g.drawImage(new ImageIcon("current/res/black_.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					else if(Main.map[iii] == 'E')
						g.drawImage(new ImageIcon("current/res/blue.png").getImage(), ii * 60 - 10, i * 60 - 10, 80, 80, null);
					
					else if((int)Main.map[iii] >= (int)'0' && (int)Main.map[iii] <= (int)'9')
						g.drawImage(new ImageIcon("current/res/boom" + Main.map[iii] + ".png").getImage(), ii * 60 - 60, i * 60 - 60, 180, 180, null);
					else if(map[iii] == 'c')
						g.drawImage(new ImageIcon("current/res/car0.png").getImage(), ii * 60 - 60, i * 60 - 20, 180, 80, null);
					else if(map[iii] == 'C')
						g.drawImage(new ImageIcon("current/res/car1.png").getImage(), ii * 60 - 60, i * 60 - 20, 180, 80, null);
					else if(map[iii] == 'q')
						g.drawImage(new ImageIcon("current/res/car2_0.png").getImage(), ii * 60 - 75, i * 60 - 30, 200, 90, null);
					else if(map[iii] == 'Q')
						g.drawImage(new ImageIcon("current/res/car2_1.png").getImage(), ii * 60 - 75, i * 60 - 30, 200, 90, null);
					else if(map[iii] == '/')
						g.drawImage(new ImageIcon("current/res/helicopter_00.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == '\\')
						g.drawImage(new ImageIcon("current/res/helicopter_10.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == 'i')
						g.drawImage(new ImageIcon("current/res/helicopter_01.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == 'I')
						g.drawImage(new ImageIcon("current/res/helicopter_11.png").getImage(), ii * 60 - 80, i * 60 - 30, 200, 90, null);
					else if(map[iii] == '(')
						g.drawImage(new ImageIcon("current/res/tank0_0.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
					else if(map[iii] == ')')
						g.drawImage(new ImageIcon("current/res/tank1_0.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
					else if(map[iii] == 'X')
						g.drawImage(new ImageIcon("current/res/box2.png").getImage(), ii * 60 - 60, i * 60 + 60, 120, 60, null);
					else if(map[iii] == '<') {
						g.drawImage(new ImageIcon("current/res/tank0_1.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
						map[iii] = '(';
					} else if(map[iii] == '>') {
						g.drawImage(new ImageIcon("current/res/tank1_1.png").getImage(), ii * 60 - 60, i * 60 - 20, 200, 80, null);
						map[iii] = ')';
					} else if(map[iii] == '"') {
						g.drawImage(new ImageIcon("current/res/bomb.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'p') {
						g.drawImage(new ImageIcon("current/res/portal0.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'P') {
						g.drawImage(new ImageIcon("current/res/portal1.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'M') {
						g.drawImage(new ImageIcon("current/res/l_stul.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'm') {
						g.drawImage(new ImageIcon("current/res/r_stul.png").getImage(), ii * 60, i * 60 - 60, 60, 120, null);
					} else if(map[iii] == 'z') {
						g.drawImage(new ImageIcon("current/res/green.png").getImage(), ii * 60 - 15, i * 60 - 15, 90, 90, null);
					} else if((map[iii] >= 'а' && map[iii] <= 'я') || (map[iii] >= 'А' && map[iii] <= 'Я') || map[iii] == '-' || map[iii] == '!' || map[iii] == '?' || map[iii] == 'ё' || map[iii] == 'Ё') {
						g.setColor(new Color(255, 255, 255));
						g.setFont(new Font("Monospaced", Font.PLAIN, 60));
						g.drawString("" + map[iii], ii * 60 + 15, i * 60 + 60);
					} else if(map[iii] == '№')
						g.drawImage(new ImageIcon("current/res/B2.png").getImage(), ii * 60, i * 60 - 33, 60, 60, null);
					
					/* подсветка выбранного блока */
					if(iii == selected && ui && !following) {
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
						if(forBoom[iii] && !programmingMode) {
							g.drawImage(new ImageIcon("current/res/red.png").getImage(), ii * 60 - 60, i * 60, 180, 60, null);
							g.drawImage(new ImageIcon("current/res/red.png").getImage(), ii * 60, i * 60 - 60, 60, 180, null);
						} else if(behaviorSelected2 != -1 && iii == behaviorSelected2 && programmingMode)
							g.drawImage(new ImageIcon("current/res/red.png").getImage(), ii * 60, i * 60, 60, 60, null);
					} catch(ArrayIndexOutOfBoundsException e) {}
					iii++;
				}
			}
			
			g.drawImage(new ImageIcon("current/res/pricel.png").getImage(), 1024 / 2 - 7, 700 / 2 - 20, 12, 8, null);
			g.drawImage(new ImageIcon("current/res/vignette.png").getImage(), 0, 0, 1024, 728, null);
			
			g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
			g.setColor(new Color(255, 255, 0));
			if(!select && !fill)
				g.drawString("" + fill((System.currentTimeMillis() - startTime) / 60000, 2) + ":" + fill((System.currentTimeMillis() - startTime) / 1000 % 60, 2), 15, 20);
			else if(select && !fill)
				g.drawString("Введите символ...", 15, 20);
			else if(!select && fill)
				g.drawString("Выберите сторону...", 15, 20);
			
			
			if(programmingMode) {
				g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 11));
				g.drawString("Режим программирования", 15, 60);
				char[] behavior2 = behavior.toCharArray();
				
				iii = 0;
				loop:
				for(int i = 0; i < 8; i++) {
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
			try {g.drawString("" + map[selectedBlockAddr()], 65, 40);} catch(ArrayIndexOutOfBoundsException e) {}
			
			g.setColor(new Color(250, 250, 250));
			g.setFont(new Font("Monospaced", Font.BOLD, 20));
			
			if(slow)
				g.drawString("~", 970, 20);
			
			g.setFont(new Font("Monospaced", Font.PLAIN, 14));
			
			if(help && !programmingMode) {
				g.drawImage(new ImageIcon("current/res/black.png").getImage(), 0, 0, 1024, 728, null);
				
				String[] helpMessageSplitted = helpMessage.split("\n");
				for(int i = 0; i < helpMessageSplitted.length; i++)
					g.drawString(helpMessageSplitted[i], 15, (i + 1) * 18);
			} else if(help && programmingMode) {
				g.drawImage(new ImageIcon("current/res/black.png").getImage(), 0, 0, 1024, 728, null);
				
				g.setFont(new Font("Monospaced", Font.PLAIN, 15));
				
				g.drawString("<стрелки>.......:  перемещение", 20, 20);
				g.drawString("wasd............:  up, left, down, right", 20, 40);
				g.drawString("WASD............:  up:copy, left:copy, down:copy, right:copy", 20, 60);
				g.drawString("ijkl............:  up:lift, left:lift, down:lift, right:lift", 20, 80);
				g.drawString("fF..............:  fire, fire2", 20, 100);
				g.drawString("b...............:  boom", 20, 120);
				g.drawString("<Home>..........:  стереть весь код", 20, 140);
				g.drawString("<Backspace>.....:  стереть последний блок кода", 20, 160);
				g.drawString("<пробел>........:  sel:<...> (выбрать блок под прицелом)", 20, 180);
				g.drawString("<Ctrl+X>........:  no_sel (убрать выделение)", 20, 200);
				g.drawString("<Enter>.........:  set:<след. клавиша>", 20, 220);
				g.drawString("<Insert>........:  изменение кода в текстовом режиме", 20, 240);
				g.drawString("0123456789-.....:  ~<...> (приостановить выполнение на 50/100/200/.../1000/5000 миллисекунд", 20, 260);
				g.drawString("Z...............:  wait (ждать любого столкновения)", 20, 280);
				g.drawString("   'z', а после 'w'/'a'/'s'/'d' --- wait:up/left/down/right (ждать столкновения в опр. стороне)", 20, 300);
				g.drawString("<F5>............:  выход из режима программирования", 20, 320);
				g.drawString("<F1>............:  скрыть/показать эту помощь", 20, 340);
				g.drawString("T...............:  tp:<адрес блока под прицелом>", 20, 360);
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

