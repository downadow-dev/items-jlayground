package downadow.items_jlayground.blocks;

import java.awt.Image;

public class Blocks {
    private static Simple[] blocks = new Simple[512];
    private static Tank[] tanks = new Tank[256];
    private static Helicopter[] helicopters = new Helicopter[256];
    private static Water[] waterTypes = new Water[10];
    private static Arrow[] arrows = new Arrow[10];
    
    private static int blocksLength = 0, tanksLength = 0, helicoptersLength = 0,
        waterTypesLength = 0, arrowsLength = 0;
    
    public static final int defaultX = 0, defaultY = 0, defaultW = 50, defaultH = 50;
    
    /* добавление */
    
    public static void addSimple(char c, char leftC, char rightC, Image texture,
                                 boolean isFallen, boolean isStrong, boolean isSticky,
                                 boolean isFireResistant, boolean isEraser,
                                 int x, int y, int w, int h) {
        blocks[blocksLength] = new Simple();
        blocks[blocksLength].c = c;
        blocks[blocksLength].leftC = leftC;
        blocks[blocksLength].rightC = rightC;
        blocks[blocksLength].texture = texture;
        blocks[blocksLength].isFallen = isFallen;
        blocks[blocksLength].isStrong = isStrong;
        blocks[blocksLength].isSticky = isSticky;
        blocks[blocksLength].isFireResistant = isFireResistant;
        blocks[blocksLength].isEraser = isEraser;
        blocks[blocksLength].x = x;
        blocks[blocksLength].y = y;
        blocks[blocksLength].w = w;
        blocks[blocksLength].h = h;
        
        blocksLength++;
    }
    
    public static void addTank(char c, char leftC, char rightC, Image texture,
                               char c2, boolean isStrong,
                               int x, int y, int w, int h) {
        tanks[tanksLength] = new Tank();
        tanks[tanksLength].c = c;
        tanks[tanksLength].leftC = leftC;
        tanks[tanksLength].rightC = rightC;
        tanks[tanksLength].texture = texture;
        tanks[tanksLength].c2 = c2;
        tanks[tanksLength].isStrong = isStrong;
        tanks[tanksLength].x = x;
        tanks[tanksLength].y = y;
        tanks[tanksLength].w = w;
        tanks[tanksLength].h = h;
        
        tanksLength++;
    }
    
    public static void addHelicopter(char c, char leftC, char rightC, Image texture,
                                     char c2, boolean isStrong,
                                     int x, int y, int w, int h) {
        helicopters[helicoptersLength] = new Helicopter();
        helicopters[helicoptersLength].c = c;
        helicopters[helicoptersLength].leftC = leftC;
        helicopters[helicoptersLength].rightC = rightC;
        helicopters[helicoptersLength].texture = texture;
        helicopters[helicoptersLength].c2 = c2;
        helicopters[helicoptersLength].isStrong = isStrong;
        helicopters[helicoptersLength].x = x;
        helicopters[helicoptersLength].y = y;
        helicopters[helicoptersLength].w = w;
        helicopters[helicoptersLength].h = h;
        
        helicoptersLength++;
    }
    
    public static void addWaterType(char c, Image texture) {
        waterTypes[waterTypesLength] = new Water();
        waterTypes[waterTypesLength].c = c;
        waterTypes[waterTypesLength].texture = texture;
        
        waterTypesLength++;
    }
    
    public static void addArrow(char c, char leftC, char rightC, Image texture,
                                boolean isFallen, boolean isStrong, boolean isFireResistant,
                                int x, int y, int w, int h) {
        arrows[arrowsLength] = new Arrow();
        arrows[arrowsLength].c = c;
        arrows[arrowsLength].leftC = leftC;
        arrows[arrowsLength].rightC = rightC;
        arrows[arrowsLength].texture = texture;
        arrows[arrowsLength].isFallen = isFallen;
        arrows[arrowsLength].isStrong = isStrong;
        arrows[arrowsLength].isFireResistant = isFireResistant;
        arrows[arrowsLength].x = x;
        arrows[arrowsLength].y = y;
        arrows[arrowsLength].w = w;
        arrows[arrowsLength].h = h;
        
        arrowsLength++;
    }
    
    /* определение типа */
    
    public static boolean isSimple(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return true;
        return false;
    }
    
    public static boolean isTank(char c) {
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return true;
        return false;
    }
    
    public static boolean isHelicopter(char c) {
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return true;
        return false;
    }
    
    public static boolean isWater(char c) {
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return true;
        return false;
    }
    
    public static boolean isArrow(char c) {
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return true;
        return false;
    }
    
    /* получение атрибутов */
    
    public static char getLeftC(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].leftC;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].leftC;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].leftC;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].leftC;
        return c;
    }
    
    public static char getRightC(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].rightC;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].rightC;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].rightC;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].rightC;
        return c;
    }
    
    public static Image getTexture(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].texture;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].texture;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].texture;
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return waterTypes[i].texture;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].texture;
        return null;
    }
    
    public static boolean isFallen(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isFallen;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].isFallen;
        if(isTank(c)) return true;
        return false;
    }
    
    public static boolean isStrong(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isStrong;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].isStrong;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].isStrong;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].isStrong;
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return true;
        return false;
    }
    
    public static boolean isSticky(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isSticky;
        return false;
    }
    
    public static boolean isFireResistant(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isFireResistant;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].isFireResistant;
        return true;
    }
    
    public static boolean isEraser(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isEraser;
        return false;
    }
    
    public static int getTextureX(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].x;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].x;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].x;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].x;
        return defaultX;
    }
    
    public static int getTextureY(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].y;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].y;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].y;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].y;
        return defaultY;
    }
    
    public static int getTextureWidth(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].w;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].w;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].w;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].w;
        return defaultW;
    }
    
    public static int getTextureHeight(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].h;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].h;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].h;
        for(int i = 0; i < arrowsLength; i++)
            if(arrows[i].c == c)
                return arrows[i].h;
        return defaultH;
    }
    
    public static char getC2(char c) {
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].c2;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].c2;
        return c;
    }
}

