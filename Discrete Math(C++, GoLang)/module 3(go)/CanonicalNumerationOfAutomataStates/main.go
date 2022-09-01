// MealyKanon project main.go
package main

import (
        "fmt"
)

type Vertex struct {
	oldName, name int
}

var time int

func DFS(G [][]*Vertex, v *Vertex) []int {
	time = 0
	order := make([]int, len(G))
	VisitVertex(G, v, order)
	order = order[0:time]
	return order
}

func VisitVertex(G [][]*Vertex, v *Vertex, order []int) {
	v.name = time
	order[time] = v.oldName
	time++
	for _, u := range G[v.oldName] {
		if u.name == -1 {
			VisitVertex(G, u, order)
		}
	}
}

func PrintMealy(m int, δ [][]int, ϕ [][]string, V []*Vertex, order []int) {
	fmt.Printf("%d\n%d\n0\n", time, m)
	var i, j int
	for _, i = range order {
		if V[i].name != -1 {
			for j = 0; j < m; j++ {
				fmt.Printf("%d ", V[δ[i][j]].name)
			}
			fmt.Printf("\n")
		}
	}
	for _, i = range order {
		if V[i].name != -1 {
			for j = 0; j < m; j++ {
				fmt.Printf("%s ", ϕ[i][j])
			}
			fmt.Printf("\n")
		}
	}
}

func main() {
	var n, m, q0, i, j, a int
	fmt.Scan(&n, &m, &q0)
	V := make([]*Vertex, n)
	G := make([][]*Vertex, n)
	δ := make([][]int, n)
	ϕ := make([][]string, n)
	for i = 0; i < n; i++ {
		V[i] = &Vertex{
			oldName: i,
			name:    -1,
		}
	}
	for i = 0; i < n; i++ {
		δ[i] = make([]int, m)
		for j = 0; j < m; j++ {
			fmt.Scan(&a)
			δ[i][j] = a
			G[i] = append(G[i], V[a])
		}
	}
	for i = 0; i < n; i++ {
		ϕ[i] = make([]string, m)
		for j = 0; j < m; j++ {
			fmt.Scan(&ϕ[i][j])
		}
	}
	order := DFS(G, V[q0])
	PrintMealy(m, δ, ϕ, V, order)
}
