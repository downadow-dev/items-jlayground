#!/bin/bash

if [[ "$1" == "--help" ]]; then
    echo 'usage: ./start.sh [--help] [-c <address>:<port>]|[-s <port>]'
    exit 1
fi

# одиночная игра
if [[ "$1" == "" ]]; then
    java downadow.items_jlayground.main.Main
    exit
fi
##################

# проверка curl
curl --version > /dev/null
if [[ "$?" != '0' ]]; then
    echo 'Please install curl!'
    exit 1
fi
#################

# подключение
if [[ "$1" == '-c' ]]; then
    # загрузка ресурспака
    echo -n 'Downloading resourcepack... '
    curl --no-progress-meter -o current/help "$2/current/help"
    for t in $(ls current/res/*); do
        curl --no-progress-meter -o "$t" "$2/$t"
    done
    echo 'DONE'
    ######################
    
    echo -n 'Starting game... '
    java downadow.items_jlayground.main.Main --client &
    echo 'DONE'
    
    echo 'Multiplayer game.'
    
    while true; do
        curl --no-progress-meter -o current/map "$2/current/map"
        curl --no-progress-meter -o current/adminPos "$2/current/adminPos"
        sleep 0.5
    done
fi

if [[ "$1" == '-s' ]]; then
    java downadow.items_jlayground.main.Main --server &
    python3 -m http.server "$2"
fi

