#!/bin/sh

APPNAME="TMS Web UI"
APPBASE=/opt/ufs-tms
APPCODE=ufs-tms
APPPID=$APPBASE/runs/$APPCODE.pid
APPJAR=/opt/ufs-tms/ufs-tms-1.0.7-SNAPSHOT.jar

case $1 in
    start)
        echo "Starting $APPNAME ..."
        if [ ! -f $APPPID ]; then
            # shellcheck disable=SC2039
            nohup java -jar $APPJAR -Xmx1024m &>/dev/null &
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

