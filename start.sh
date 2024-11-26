#!/bin/bash

if [[ "$1" == "--help" ]]; then
    echo 'Простой лаунчер для Items Jlayground.
Использование: ./start.sh [один из следующих параметров]

   -s <адрес>:<порт>         запустить сервер
   -S <адрес>:<порт>         запустить сервер и разрешить клиентам
                             запускать команды
   -c <http-адрес>           подключиться к серверу, адрес может
                             быть в формате <адр>:<порт>

Без параметра, игра запускается в одиночном режиме.'
    exit 1
fi

# одиночная игра
if [[ "$1" == "" ]]; then
    java downadow.items_jlayground.main.Main
    exit
fi
##################

# проверка php
php --version > /dev/null
if [[ "$?" != '0' ]]; then
    echo 'Пожалуйста, установите php!'
    exit 1
fi
#################

# подключение
if [[ "$1" == '-c' ]]; then
    # проверка curl
    curl --version > /dev/null
    if [[ "$?" != '0' ]]; then
        echo 'Пожалуйста, установите curl!'
        exit 1
    fi
    #################
    
    # проверка ресурспака
    echo -n 'Проверка ресурспака... '
    curl -o desc "$2/current/desc" 2> /dev/null
    if ! diff desc current/desc > /dev/null; then
        echo 'ОШИБКА'
        echo 'Пожалуйста, загрузите ресурспак сервера.'
        rm -f desc
        exit 1
    fi
    rm -f desc
    echo 'ГОТОВО'
    #####################
    curl -o current/help "$2/current/help" 2> /dev/null
    
    echo -n 'Запускаем Items Jlayground... '
    java downadow.items_jlayground.main.Main --client &
    echo 'ГОТОВО'
    
    curl "$2/msg.php?m=$(php -r 'echo urlencode("[connect]");')" 2> /dev/null
    trap "curl \"$2/msg.php?m=$(php -r 'echo urlencode("[disconnect]");')\" 2> /dev/null" EXIT
    
    echo 'Многопользовательская игра.'
    
    while true; do
        curl -o current/map "$2/current/map" 2> /dev/null
        curl -o current/adminPos "$2/current/adminPos" 2> /dev/null
        if [[ "$(cat current/msg)" != "" ]]; then
            curl "$2/msg.php?m=$(php -r 'echo urlencode(file_get_contents("current/msg"));')" 2> /dev/null
            : > current/msg
        fi
        sleep 0.4
    done
fi

if [[ "$1" == '-s' || "$1" == '-S' ]]; then
    if [[ "$1" == '-S' ]]; then
        java downadow.items_jlayground.main.Main --server &
    else
        java downadow.items_jlayground.main.Main --server-no-creators &
    fi
    
    php -S "$2"
fi

