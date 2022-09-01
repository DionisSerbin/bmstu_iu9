// MinimizationMealy project main.go
package main

import (
        "fmt"
)

type state struct {
	name   int
	parent *state
	depth  int
	flag   bool
}

func (q *state) Find() *state {
	if q == q.parent {
		return q
	}
	return q.parent.Find()
}

func (q *state) Union(x *state) {
	if q.depth > x.depth {
		x.parent = q
	} else {
		q.parent = x
		if q.depth == x.depth {
			x.depth++
		}
	}
}

type MealyMachine struct {
	n, m, q0 int
	Q        []*state
	δ        [][]int
	ϕ        [][]string
}

func InputMM() *MealyMachine {
	var n, m, q0, i, j int
	fmt.Scan(&n, &m, &q0)
	Q := make([]*state, n)
	δ := make([][]int, n)
	ϕ := make([][]string, n)
	for i = 0; i < n; i++ {
		Q[i] = &state{
			name:   i,
			parent: Q[i],
			depth:  0,
			flag:   true,
		}
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
	MM := MealyMachine{
		n:  n,
		m:  m,
		q0: q0,
		Q:  Q,
		δ:  δ,
		ϕ:  ϕ,
	}
	return &MM
}

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

func (MM *MealyMachine) Canon() *MealyMachine {
	var i, j int
	V := make([]*Vertex, MM.n)
	G := make([][]*Vertex, MM.n)
	for i = 0; i < MM.n; i++ {
		V[i] = &Vertex{
			oldName: i,
			name:    -1,
		}
	}
	for i = 0; i < MM.n; i++ {
		for j = 0; j < MM.m; j++ {
			G[i] = append(G[i], V[MM.δ[i][j]])
		}
	}
	order := DFS(G, V[MM.q0])
	nNew := time
	mNew := MM.m
	QNew := make([]*state, nNew)
	δNew := make([][]int, nNew)
	ϕNew := make([][]string, nNew)
	index := 0
	for _, i = range order {
		if V[i].name != -1 {
			δNew[index] = make([]int, mNew)
			ϕNew[index] = make([]string, mNew)
			QNew[index] = &state{
				name:   index,
				parent: QNew[index],
				depth:  0,
				flag:   true,
			}
			for j = 0; j < MM.m; j++ {
				δNew[index][j] = V[MM.δ[i][j]].name
				ϕNew[index][j] = MM.ϕ[i][j]
			}
			index++
		}
	}
	MM.n = nNew
	MM.m = mNew
	MM.q0 = 0
	MM.Q = QNew
	MM.δ = δNew
	MM.ϕ = ϕNew
	return MM
}

func (MM *MealyMachine) Split1() (m int, π []*state) {
	m = MM.n
	for _, q := range MM.Q {
		q.parent = q
		q.depth = 0
	}
	var x int
	for _, q1 := range MM.Q {
		for _, q2 := range MM.Q {
			if q1.Find() != q2.Find() {
				for x = 0; x < MM.m; x++ {
					if MM.ϕ[q1.name][x] != MM.ϕ[q2.name][x] {
						break
					}
				}
				if x == MM.m {
					q1.Union(q2)
					m--
				}
			}
		}
	}
	π = make([]*state, MM.n)
	for _, q := range MM.Q {
		π[q.name] = q.Find()
	}
	return
}
func (MM *MealyMachine) Split(π []*state) int {
	m := MM.n
	for _, q := range MM.Q {
		q.parent = q
		q.depth = 0
	}
	var x, w1, w2 int
	for _, q1 := range MM.Q {
		for _, q2 := range MM.Q {
			if π[q1.name] == π[q2.name] && q1.Find() != q2.Find() {
				for x = 0; x < MM.m; x++ {
					w1 = MM.δ[q1.name][x]
					w2 = MM.δ[q2.name][x]
					if π[w1] != π[w2] {
						break
					}
				}
				if x == MM.m {
					q1.Union(q2)
					m--
				}
			}
		}
	}
	for _, q := range MM.Q {
		π[q.name] = q.Find()
	}
	return m
}

func (MM *MealyMachine) AufenkampHohn() *MealyMachine {
	m, π := MM.Split1()
	var (
		x, mNew, q0New, nNew int
		qNew                 *state
	)
	for {
		mNew = MM.Split(π)
		if m == mNew {
			break
		}
		m = mNew
	}
	QNew := make([]*state, MM.n)
	δNew := make([][]int, 0, MM.n)
	ϕNew := make([][]string, 0, MM.n)
	nNew = 0
	balanse := make([]int, MM.n)
	for _, q := range MM.Q {
		qNew = π[q.name]
		if qNew.flag {
			balanse[qNew.name] = nNew
			qNew.flag = false
			QNew[nNew] = &state{
				name:   nNew,
				parent: QNew[nNew],
				depth:  0,
				flag:   true,
			}
			δNew = append(δNew, make([]int, MM.m))
			ϕNew = append(ϕNew, make([]string, MM.m))
			for x = 0; x < MM.m; x++ {
				δNew[QNew[nNew].name][x] = π[MM.δ[q.name][x]].name
				ϕNew[QNew[nNew].name][x] = MM.ϕ[q.name][x]
			}
			nNew++
		}
	}
	q0New = balanse[π[MM.q0].name]
	for i := 0; i < nNew; i++ {
		for x = 0; x < MM.m; x++ {
			δNew[i][x] = balanse[δNew[i][x]]
		}
	}
	return &MealyMachine{
		n:  nNew,
		m:  MM.m,
		q0: q0New,
		Q:  QNew[0:nNew],
		δ:  δNew,
		ϕ:  ϕNew,
	}
}

func (MM MealyMachine) Print() {
	var i, j int
	fmt.Printf("digraph {\n")
	fmt.Printf("\trankdir = LR\n")
	fmt.Printf("\tdummy [label = \"\", shape = none]\n")
	for i = 0; i < MM.n; i++ {
		fmt.Printf("\t%d [shape = circle]\n", i)
	}
	fmt.Printf("\tdummy -> %d\n", MM.q0)
	for i = 0; i < MM.n; i++ {
		for j = 0; j < MM.m; j++ {
			fmt.Printf("\t%d -> %d [label = \"%c(%s)\"]\n", i, MM.δ[i][j], 'a'+j, MM.ϕ[i][j])
		}
	}
	fmt.Printf("}")
}

func main() {
	InputMM().AufenkampHohn().Canon().Print()
}
