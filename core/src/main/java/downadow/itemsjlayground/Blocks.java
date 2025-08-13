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

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Blocks {
    private static Simple[] blocks = new Simple[512];
    private static Tank[] tanks = new Tank[256];
    private static Helicopter[] helicopters = new Helicopter[256];
    private static Water[] waterTypes = new Water[16];
    
    private static int blocksLength = 0, tanksLength = 0, helicoptersLength = 0, waterTypesLength = 0;
    
    public static final int defaultX = 0, defaultY = 0, defaultW = 50, defaultH = 50;
    
    private static Texture voidTexture;
    
    public static void init() {
        voidTexture = new Texture("void.png");
    }
    
    /* добавление */
    
    public static void addSimple(char c, char leftC, char rightC, Texture texture,
                                 boolean isFallen, boolean isStrong, boolean isSticky,
                                 boolean isFireResistant, boolean isEraser,
                                 boolean isTranslucent, int light,
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
        blocks[blocksLength].isTranslucent = isTranslucent;
        blocks[blocksLength].light = light;
        blocks[blocksLength].x = x;
        blocks[blocksLength].y = y;
        blocks[blocksLength].w = w;
        blocks[blocksLength].h = h;
        
        blocksLength++;
    }
    
    public static void addTank(char c, char leftC, char rightC, Texture texture,
                               Texture texture2, boolean isStrong,
                               int x, int y, int w, int h) {
        tanks[tanksLength] = new Tank();
        tanks[tanksLength].c = c;
        tanks[tanksLength].leftC = leftC;
        tanks[tanksLength].rightC = rightC;
        tanks[tanksLength].texture = texture;
        tanks[tanksLength].texture2 = texture2;
        tanks[tanksLength].isStrong = isStrong;
        tanks[tanksLength].x = x;
        tanks[tanksLength].y = y;
        tanks[tanksLength].w = w;
        tanks[tanksLength].h = h;
        
        tanksLength++;
    }
    
    public static void addHelicopter(char c, char leftC, char rightC, Texture texture,
                                     Texture texture2, boolean isStrong,
                                     int x, int y, int w, int h) {
        helicopters[helicoptersLength] = new Helicopter();
        helicopters[helicoptersLength].c = c;
        helicopters[helicoptersLength].leftC = leftC;
        helicopters[helicoptersLength].rightC = rightC;
        helicopters[helicoptersLength].texture = texture;
        helicopters[helicoptersLength].texture2 = texture2;
        helicopters[helicoptersLength].isStrong = isStrong;
        helicopters[helicoptersLength].x = x;
        helicopters[helicoptersLength].y = y;
        helicopters[helicoptersLength].w = w;
        helicopters[helicoptersLength].h = h;
        
        helicoptersLength++;
    }
    
    public static void addWaterType(char c, Texture texture, int x, int y, int w, int h) {
        waterTypes[waterTypesLength] = new Water();
        waterTypes[waterTypesLength].c = c;
        waterTypes[waterTypesLength].texture = texture;
        waterTypes[waterTypesLength].x = x;
        waterTypes[waterTypesLength].y = y;
        waterTypes[waterTypesLength].w = w;
        waterTypes[waterTypesLength].h = h;
        
        waterTypesLength++;
    }
    
    /* определение типа */
    
    public static boolean isUnknown(char c) {
        if(c == '~' || c == ',' || c == ':' || c == ';' || c == 'b' ||
           c == 'f' || c == 'F' || c == 'p' || c == 'P' || c == '.')
            return false;
        
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return false;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return false;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return false;
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return false;
        return true;
    }
    
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
        return c;
    }
    
    public static Texture getTexture(char c) {
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
        return voidTexture;
    }
    
    public static boolean isFallen(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isFallen;
        if(isTank(c) || c == ':' || c == ';') return true;
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
        return true;
    }
    
    public static boolean isEraser(char c) {
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isEraser;
        return false;
    }
    
    public static boolean isTranslucent(char c) {
        if(c == '.' || c == ';' || c == ':' || c == 'p' ||
           c == 'P' || c == 'f' || c == 'F' || isUnknown(c)) return true;
        
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return true;
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return true;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return true;
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].isTranslucent;
        
        return false;
    }
    
    public static int getLight(char c) {
        if(c == 'f' || c == 'F') return 5;
        else if(c == 'p' || c == 'P') return 3;
        
        for(int i = 0; i < blocksLength; i++)
            if(blocks[i].c == c)
                return blocks[i].light;
        return 0;
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
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return waterTypes[i].x;
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
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return waterTypes[i].y;
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
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return waterTypes[i].w;
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
        for(int i = 0; i < waterTypesLength; i++)
            if(waterTypes[i].c == c)
                return waterTypes[i].h;
        return defaultH;
    }
    
    public static Texture getTexture2(char c) {
        for(int i = 0; i < tanksLength; i++)
            if(tanks[i].c == c)
                return tanks[i].texture2;
        for(int i = 0; i < helicoptersLength; i++)
            if(helicopters[i].c == c)
                return helicopters[i].texture2;
        return voidTexture;
    }
}

