#!/bin/bash

rm -rf release release.zip
mkdir release
cat empty_map > .map
jar -cvfe items-jlayground.jar downadow.items_jlayground.main.Main downadow/
cp -r empty_map .map res LICENSE release/
mv items-jlayground.jar release/
zip -r1 release.zip release
rm -r release
