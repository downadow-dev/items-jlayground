# ![box](https://github.com/downadow-dev/items-jlayground/raw/main/game/images/box.png) Items Jlayground

**Items Jlayground** — это свободная игра-песочница, в которой игроку
предоставляется свобода _строить, разрушать, программировать, взрывать,
летать на вертолёте, стрелять из танка_ и другое. Игра написана на Java
без использования сторонних библиотек и имеет низкие системные
требования. Items Jlayground поддерживает многопользовательскую игру.
Автор игры — downadow.

Мир Items Jlayground представляет собой одномерный массив с заданными
шириной (250) и высотой (60). Ячейка может определяться как либо отсутствие
блока (.), либо танк, либо вертолёт, либо вода, либо примитив. Примитивы
могут иметь множество атрибутов (_падающий_, _крепкий_, _липкий_ и т. д.).

## Сборка и запуск

Вы должны иметь установленный Java Development Kit в системе, прежде чем приступить к компиляции.
Для Windows, рекомендуется загрузить OpenJDK >=21 с [adoptium.net](https://adoptium.net/).

### Windows

Скопируйте каталог `game` сюда снова, но под именем `current`. Введите в командной строке:
```
javac */*/*/*.java
java downadow.items_jlayground.main.Main
```

### GNU/Linux

```bash
./build.sh
./start.sh
```

