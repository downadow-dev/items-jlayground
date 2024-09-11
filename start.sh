#!/bin/bash

if [[ "$1" == "--help" ]]; then
    echo 'usage: ./start.sh [--help] [{-c <address>:<port>}|{{-s|-S} <address>:<port>}]'
    exit 1
fi

# одиночная игра
if [[ "$1" == "" ]]; then
    java downadow.items_jlayground.main.Main
    exit
fi
##################

# подключение
if [[ "$1" == '-c' ]]; then
    # проверка curl
    curl --version > /dev/null
    if [[ "$?" != '0' ]]; then
        echo 'Please install curl!'
        exit 1
    fi
    #################
    
    # загрузка ресурспака
    echo -n 'Downloading resourcepack... '
    curl -o current/help "$2/current/help" 2> /dev/null
    for t in $(ls current/res/*); do
        curl -o "$t" "$2/$t" 2> /dev/null
    done
    echo 'DONE'
    ######################
    
    echo -n 'Starting game... '
    java downadow.items_jlayground.main.Main --client &
    echo 'DONE'
    
    echo 'Multiplayer game.'
    
    while true; do
        curl -o current/map "$2/current/map" 2> /dev/null
        curl -o current/adminPos "$2/current/adminPos" 2> /dev/null
        if [[ "$(cat current/msg)" != "" ]]; then
            curl "$2/msg.php?m=$(php -r 'echo urlencode(file_get_contents("current/msg"));')" 2> /dev/null
            : > current/msg
        fi
        sleep 0.5
    done
fi

if [[ "$1" == '-s' || "$1" == '-S' ]]; then
    # проверка php
    php --version > /dev/null
    if [[ "$?" != '0' ]]; then
        echo 'Please install php!'
        exit 1
    fi
    #################
    
    if [[ "$1" == '-S' ]]; then
        java downadow.items_jlayground.main.Main --server &
    else
        java downadow.items_jlayground.main.Main --server-no-creators &
    fi
    
    php -S "$2"
fi

