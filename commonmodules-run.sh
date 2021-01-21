#!/bin/sh

APPNAME="UFS Common Modules API"
APPBASE=/opt/ufs-backend
APPCODE=ufs-webportal-api
APPPID=$APPBASE/runs/$APPCODE.pid
APPJAR=/opt/ufs-backend/ufs-1.0.0.jar

case $1 in
    start)
        echo "Starting $APPNAME ..."
        if [ ! -f $APPPID ]; then
            nohup java -jar $APPJAR -Xmx256m &>/dev/null &
	    echo $! > $APPPID
            echo "$APPNAME started!"
        else
            echo "$APPNAME is already running ..."
        fi
    ;;

    stop)
        if [ -f $APPPID ]; then
            PID=$(cat $APPPID);    
            echo "Stopping $APPNAME..."
            kill $PID;
            echo "$APPNAME stopped!"
            rm $APPPID
        else
            echo "$APPNAME is not running ..."                                             
        fi
    ;;

    *)
        echo "Choose an option start/stop for the service"
    ;;
esac

