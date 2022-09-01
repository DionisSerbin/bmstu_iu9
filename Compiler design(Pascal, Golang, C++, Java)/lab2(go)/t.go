package main

import "fmt"

var FUNCNAME string

var y string = FUNCNAME

func main() {
	fmt.Println(FUNCNAME)
	var x = FUNCNAME + FUNCNAME
	fmt.Println(x)
}
