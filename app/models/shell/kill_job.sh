#!/bin/bash

arg1=$1
echo "will kill the job with id =$arg1"
exec yarn application -kill $arg1



