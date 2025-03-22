# ![superbricks](https://github.com/downadow-dev/items-jlayground/raw/main/assets/game/images/superbricks.png) Items Jlayground

**Items Jlayground** — это свободная игра-песочница, в которой игроку
предоставляется свобода _строить, разрушать, программировать, взрывать,
летать на вертолёте, стрелять из танка_ и другое. Игра написана на Java
с использованием LibGDX. Items Jlayground поддерживает многопользовательскую игру.
Автор игры — downadow.

Мир Items Jlayground представляет собой одномерный массив с заданными
шириной (250) и высотой (60). Ячейка может определяться как отсутствие
блока (.), либо танк, либо вертолёт, либо вода, либо примитив. Примитивы
могут иметь множество атрибутов (_падающий_, _крепкий_, _липкий_ и т. д.).

Данный проект является результатом переноса Items Jlayground с Java Swing
на LibGDX.

## Запуск

При запуске появляется начальный экран с информацией об игре. Закройте его,
нажав на любую область экрана. Теперь вы в главном меню. Здесь можно загрузить
пакет ресурсов, выбрать ресурспак из списка и начать игру.
Ресурспаки — неотъемлемая часть Items Jlayground. Они содержат карту, помощь и мод,
который, как правило, содержит определения блоков,
их текстуры (в каталоге `images/` пакета ресурсов) и атрибуты.
Выберите предустановленный пакет ресурсов "game", нажмите "Одиночная игра".

### Android

Вас встречает текст помощи. Обычно, в него включается информация о доступных
блоках, которую пользователь может прочитать в любой момент игры. Текст
помощи — часть ресурспака (в данном случае "game"). Вы можете прокручивать
помощь, используя стрелки. Кнопка в верхнем правом углу открывает или закрывает
текст помощи.

Стрелки перемещают камеру. Если вы нажмёте под стрелками "влево" и "вправо",
вы сможете передвигаться быстрее. Кнопка между стрелками позволяет
выделить блок и "играть" за него, Выделенный блок можно
поворачивать и активировать (для танка и вертолёта); для прыжка,
воспользуйтесь стрелкой "вверх". Нажатие на верхнюю область экрана сохраняет
карту.

Поставьте первый блок. Нажмите на область экрана в верхнем левом углу, откроется
клавиатура. Введите символ (идентификатор) блока. Почти полный список блоков
можно посмотреть в тексте помощи. Например, для ресурспака "game",
'r' — это кирпичи, 'w' — доски и т. д. Вы также можете ввести позицию
символа в Юникоде (например, 33 — '!'). Блок '.' обозначает отсутствие блока,
используйте его для удаления поставленных вами блоков.

Теперь если вы выбрали блок, он должен отобразиться в верхнем левом углу экрана,
внутри '\[' и '\]'. Нижняя правая область экрана — взаимодействие. Нажав на неё,
вы поставите выбранный блок, активируете выделенный блок или сделаете взрыв
помеченных областей.

Панель в верхнем правом углу экрана предоставляет доступ ко многим возможностям
игры. В частности, для того, чтобы пометить область для взрыва, включить режим
программирования, включить ночь или сжечь блок, вам придётся обращаться к данной
панели. Кнопка '...' прокручивает панель.

## Режим программирования

Режим программирования нужен для редактирования т. н. "поведения". Код поведения
выполняется каждые полсекунды. Он представляет собой слова, разделённые пробелом,
приказывающие сделать те или иные действия. Специальное слово `selected` во время
исполнения заменяется на адрес блока под прицелом, либо на адрес выделенного
пользователем блока.

`no_sel` — снять выделение; `sel:<адрес>` — выделить блок по адресу
(если нет выделения); `up`, `down`, `left`, `right`, `up:lift`, `down:lift`,
`left:lift`, `right:lift`, `up:copy`, `down:copy`, `left:copy`,
`right:copy`, — перемещение выделенного блока; `tp:<адрес>` — телепортация
выделенного блока; `~<время>` — приостановить выполнение на заданное количество
миллисекунд; `step:<адрес>:<время>` перемещать блок на заданный адрес с перерывами
в заданное количество миллисекунд. `boom`, `fire`, `fire2` — взрыв, огонь; `wait`,
`wait:up`, `wait:down`, `wait:left`, `wait:right`, — ждать столкновения
с выделенным блоком на определённой стороне. `set:<символ>` — изменить
выделенный блок на другой.

Примеры кода:
```
no_sel sel:selected boom
```
```
no_sel sel:14127 set:J step:13127:60 step:13131:50 step:14131:40 step:14127:35 ~500
```

## Сетевая игра: создание сервера

Сервер не может быть создан на Android. Для того, чтобы создать сетевую игру,
перейдите в каталог данных игры (для GNU/Linux — `~/.items-jlayground.data`),
введите в командной строке:
```
php -S 0.0.0.0:8000
```
Запустите игру, выберите ресурспак и нажмите _Server job_. Теперь другой
человек сможет подключиться к вам (он должен иметь тот же мод, что у вас).
Узнать свой IP-адрес в сети можно, запустив следующее:
```
ip addr
```

## Информация

Данный проект сгенерирован с помощью [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) версии `1.13.1.0`.
Лицензия Items Jlayground (включая часть данных шаблона и значки) и gdx-liftoff — Apache License 2.0.

### gdx-liftoff: Credits

The project was forked from the [`czyzby/gdx-setup`](https://github.com/czyzby/gdx-setup) repository.
[@czyzby](https://github.com/czyzby) and [@kotcrab](https://github.com/kotcrab) created the original application,
as well as a set of libraries that it depends on (`gdx-lml` and `VisUI` respectively). Since then, the project is
maintained by [@tommyettinger](https://github.com/tommyettinger). Graciously, czyzby came back and made a wide variety of improvements, so big
thanks there! Thanks also to [@metaphore](https://github.com/metaphore), who now maintains gdx-lml (which this used and may still use).

[@raeleus](https://github.com/raeleus) created the
[Particle Park skin for scene2d.ui](https://ray3k.wordpress.com/particle-park-ui-skin-for-scene2d-ui/),
which was adapted to be the default skin added to new projects (if the _"Generate UI Assets"_ option is selected).
"Accademia di Belle Arti di Urbino and students of MA course of Visual design" has created the _Titillium Web_
font that the skin uses (under SIL OFL license). Oh yeah, and he did some other stuff starting in version 1.12.1.10,
such as *almost the entire release*. Huge thanks to raeleus for the complete overhaul of the user experience!

Other project contributors include [@Mr00Anderson](https://github.com/Mr00Anderson), [@lyze237](https://github.com/lyze237),
[@metaphore](https://github.com/metaphore) (again!), and [@payne911](https://github.com/payne911).
People who haven't directly contributed code have still helped a lot by spending their time to test on platforms
like macOS and iOS; [@JojoIce](https://github.com/JojoIce) is one of several people who made a difference regarding iOS. And of course,
many thanks go to all the early adopters for putting up with any partially-working releases early on!

The randomized icons chopped up and used for Android projects come from the [OpenMoji](https://openmoji.org) project.
If you want to use these icons in a less-mangled format, there's
[openmoji-atlas](https://github.com/tommyettinger/openmoji-atlas) to access these emoji from libGDX conveniently.

Thanks also to everyone who has made the various libraries and tools Liftoff depends upon. From the huge team
responsible for Graal Native Image, to [Construo](https://github.com/fourlastor-alexandria/construo) by pretty much a
[team of one](https://github.com/fourlastor), some of the best features of Liftoff aren't in Liftoff code at all.
