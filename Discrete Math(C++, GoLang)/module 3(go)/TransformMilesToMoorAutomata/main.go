// Mealy-Mur project main.go
package main

import (
        "fmt"
	"sort"
)

type MealyMachine struct {
	q0 int
	Q  []int
	X  []string
	Y  []string
	δ  [][]int
	ϕ  [][]string
}

func InputMealy() *MealyMachine {
	var kX, kY, n, i, j int
	fmt.Scan(&kX)
	X := make([]string, kX)
	for i = 0; i < kX; i++ {
		fmt.Scan(&X[i])
	}

	fmt.Scan(&kY)
	Y := make([]string, kY)
	for i = 0; i < kY; i++ {
		fmt.Scan(&Y[i])
	}

	fmt.Scan(&n)
	Q := make([]int, n)
	for i = 0; i < n; i++ {
		Q[i] = i
	}

	δ := make([][]int, n)
	for i = 0; i < n; i++ {
		δ[i] = make([]int, kX)
		for j = 0; j < kX; j++ {
			fmt.Scan(&δ[i][j])
		}
	}

	ϕ := make([][]string, n)
	for i = 0; i < n; i++ {
		ϕ[i] = make([]string, kX)
		for j = 0; j < kX; j++ {
			fmt.Scan(&ϕ[i][j])
		}
	}

	return &MealyMachine{
		q0: 0,
		Q:  Q,
		X:  X,
		Y:  Y,
		δ:  δ,
		ϕ:  ϕ,
	}
}

func (MM MealyMachine) ArrayOfFrequency() [][]string {
	var i, j int
	type help struct {
		name int
		out  string
	}
	MyMap := make(map[help]bool)
	newQ := make([][]string, len(MM.Q))
	for i = 0; i < len(MM.Q); i++ {
		newQ[i] = make([]string, 0)
	}
	for i = 0; i < len(MM.Q); i++ {
		for j = 0; j < len(MM.X); j++ {
			if !MyMap[help{MM.δ[i][j], MM.ϕ[i][j]}] {
				newQ[MM.δ[i][j]] = append(newQ[MM.δ[i][j]], MM.ϕ[i][j])
				MyMap[help{MM.δ[i][j], MM.ϕ[i][j]}] = true
			}
		}
	}
	for i = 0; i < len(MM.Q); i++ {
		sort.Strings(newQ[i])
	}
	return newQ
}

func (MM MealyMachine) PrintMur() {
	AOF := MM.ArrayOfFrequency()

	fmt.Printf("digraph {\n")
	fmt.Printf("\trankdir = LR\n")
	for i, arr := range AOF {
		for _, str := range arr {
			fmt.Printf("\t\"(%d,%s)\"\n", i, str)
		}
	}
	for i, arr := range AOF {
		for _, str := range arr {
			for j, num := range MM.δ[i] {
				fmt.Printf("\t\"(%d,%s)\" -> \"(%d,%s)\" [label = \"%s\"]\n", i, str, num, MM.ϕ[i][j], MM.X[j])
			}
		}
	}
	fmt.Printf("}")
}

func main() {
	InputMealy().PrintMur()
}
