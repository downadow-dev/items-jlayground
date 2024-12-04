#!/bin/bash
# автор     downadow (Sviatoslav)
rm -f ./downadow/items_jlayground/*/*.class
javac ./downadow/items_jlayground/*/*.java
rm -rf current
rm -f res/adminPos
echo > res/msg
cp -r game current
