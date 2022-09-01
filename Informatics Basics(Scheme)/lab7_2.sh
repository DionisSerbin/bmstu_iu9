#!/bin/bash

find $1 -name '*.c' -type f -print0 -o -name '*.sh' -type f -print0 | xargs -0 cat | grep . | wc -l
