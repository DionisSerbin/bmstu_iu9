// PrintMealy project main.go
package main

import (
        "fmt"
)

func PrintMealy(n, m, q0 int, δ [][]int, ϕ [][]string) {
	var i, j int
	fmt.Printf("digraph {\n")
	fmt.Printf("\trankdir = LR\n")
	fmt.Printf("\tdummy [label = \"\", shape = none]\n")
	for i = 0; i < n; i++ {
		fmt.Printf("\t%d [shape = circle]\n", i)
	}
	fmt.Printf("\tdummy -> %d\n", q0)
	for i = 0; i < n; i++ {
		for j = 0; j < m; j++ {
			fmt.Printf("\t%d -> %d [label = \"%c(%s)\"]\n", i, δ[i][j], 'a'+j, ϕ[i][j])
		}
	}
	fmt.Printf("}")
}

func main() {
	var n, m, q0, i, j int
	fmt.Scan(&n, &m, &q0)
	δ := make([][]int, n)
	ϕ := make([][]string, n)
	for i = 0; i < n; i++ {
		δ[i] = make([]int, m)
		for j = 0; j < m; j++ {
			fmt.Scan(&δ[i][j])
		}
	}
	for i = 0; i < n; i++ {
		ϕ[i] = make([]string, m)
		for j = 0; j < m; j++ {
			fmt.Scan(&ϕ[i][j])
		}
	}
	PrintMealy(n, m, q0, δ, ϕ)
}
