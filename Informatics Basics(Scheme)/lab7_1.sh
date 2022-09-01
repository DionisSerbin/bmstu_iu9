#!/bin/bash

delta=1000

function timeInMillisecond {
        val=$(date +%s:%N);
        l=$(echo $val | cut -f 1 -d':');
        r=$(echo $val | cut -f 2 -d':' | sed "s/^0*//");
        let "res = l * 1000 + r / 1000000";
        echo $res;
}

function period {
        echo $(( ($(timeInMillisecond) - $1) / ($2 * delta) ))
}

function main {
        while true
        do
                begin=$(timeInMillisecond)
                $1
                per_begin=$(period $begin $2)
                while [ $per_begin -eq $(period $begin $2) ]
                do
                        sleep 0.0001
                done
        done
}

name=$(echo $1 | sed "y/.\//__/")
echo $name
main $1 $2 1>"$name.out" 2>"$name.error" &
