//  Nondeterministic finite automaton project main.go
package main

import (
        "fmt"
	"sort"
	"strings"
)

type State struct {
	name  int
	Final bool
	flag  bool
}

func toArrInt(masQ []*State) []int {
	arr := make([]int, len(masQ))
	for i, q := range masQ {
		arr[i] = q.name
	}
	sort.Ints(arr)
	return arr
}

func toDetState(q []*State) *DetState {
	return &DetState{
		name:  toArrInt(q),
		index: -1,
		Final: false,
	}
}

type DetState struct {
	index int
	name  []int
	Final bool
}

func (q DetState) Equal(other DetState) bool {
	if len(q.name) != len(other.name) {
		return false
	}
	for i := 0; i < len(q.name); i++ {
		if q.name[i] != other.name[i] {
			return false
		}
	}
	return true
}

func (q DetState) in(Q []*DetState) int {
	for i, u := range Q {
		if q.Equal(*u) {
			return i
		}
	}
	return -1
}

type DeterministicFiniteAutomaton struct {
	Q     []*DetState
	X     []string
	δ     []map[string]*DetState
	Final []*DetState
	q0    *DetState
}

func (DFA DeterministicFiniteAutomaton) ArrayOfFrequency() [][][]string {
	arr := make([][][]string, len(DFA.Q))
	for i := 0; i < len(DFA.Q); i++ {
		arr[i] = make([][]string, len(DFA.Q))
		for j := 0; j < len(DFA.Q); j++ {
			arr[i][j] = make([]string, 0)
		}
	}
	for _, q := range DFA.Q {
		for _, a := range DFA.X {
			arr[q.index][DFA.δ[q.index][a].index] = append(arr[q.index][DFA.δ[q.index][a].index], a)
		}
	}
	return arr
}

func (DFA DeterministicFiniteAutomaton) PrintAutomaton() {
	arr := DFA.ArrayOfFrequency()
	fmt.Printf("digraph {\n")
	fmt.Printf("\trankdir = LR\n")
	fmt.Printf("\tdummy [label = \"\", shape = none]\n")
	for i, q := range DFA.Q {
		if q.Final {
			fmt.Printf("\t%d [label = \"%v\", shape = doublecircle]\n", i, q.name)
		} else {
			fmt.Printf("\t%d [label = \"%v\", shape = circle]\n", i, q.name)
		}
	}
	fmt.Printf("\tdummy -> 0\n")
	for i, line := range arr {
		for j, str := range line {
			if len(str) != 0 {
				fmt.Printf("\t%d -> %d [label = \"%s\"]\n", i, j, strings.Join(str, ", "))
			}
		}
	}
	fmt.Printf("}")
}

type NondeterministicFiniteAutomaton struct {
	Q     []*State
	X     []string
	δ     []map[string][]*State
	Final []*State
	q0    *State
}

func ReadNFA() *NondeterministicFiniteAutomaton {
	var n, m, i, in, out, ch int
	var str string
	fmt.Scan(&n, &m)

	Q := make([]*State, n)
	δ := make([]map[string][]*State, n)
	for i = 0; i < n; i++ {
		Q[i] = &State{
			name:  i,
			Final: false,
			flag:  false,
		}
		δ[i] = make(map[string][]*State)
	}

	mapX := make(map[string]bool)
	X := make([]string, 0)

	for i = 0; i < m; i++ {
		fmt.Scan(&out, &in, &str)
		if str != "lambda" && !mapX[str] {
			X = append(X, str)
		}
		mapX[str] = true
		if _, ok := δ[out][str]; ok {
			δ[out][str] = append(δ[out][str], Q[in])
		} else {
			δ[out][str] = make([]*State, 0)
			δ[out][str] = append(δ[out][str], Q[in])
		}
	}

	for i = 0; i < n; i++ {
		fmt.Scan(&ch)
		if ch == 1 {
			Q[i].Final = true
		}
	}

	fmt.Scan(&ch)
	q0 := Q[ch]
	return &NondeterministicFiniteAutomaton{
		Q:     Q,
		X:     X,
		δ:     δ,
		Final: nil,
		q0:    q0,
	}
}

func (NFA NondeterministicFiniteAutomaton) Closure(z []*State) []*State {
	C := make([]*State, 0)
	for _, q := range z {
		NFA.Dfs(q, &C)
	}
	for _, q := range C {
		q.flag = false
	}
	return C
}

func (NFA NondeterministicFiniteAutomaton) Dfs(q *State, C *[]*State) {
	if !q.flag {
		q.flag = true
		*C = append(*C, q)
		for _, w := range NFA.δ[q.name]["lambda"] {
			NFA.Dfs(w, C)
		}
	}
}

func (NFA NondeterministicFiniteAutomaton) Det() *DeterministicFiniteAutomaton {
	var (
		u, w  *State
		index int
		a     string
		z     []*State
	)

	help_q0 := make([]*State, 1)
	help_q0[0] = NFA.q0

	z = NFA.Closure(help_q0)

	q0 := &DetState{
		name:  toArrInt(z),
		index: 0,
		Final: false,
	}

	Q := append(make([]*DetState, 0), q0)

	δ := make([]map[string]*DetState, 0)
	δ = append(δ, make(map[string]*DetState))

	Stack := make([][]*State, 0)
	Stack = append(Stack, z)

	StackInd := make([]int, 0)
	StackInd = append(StackInd, 0)

	for len(Stack) != 0 {
		z = Stack[len(Stack)-1]
		Stack = Stack[0 : len(Stack)-1]

		index = StackInd[len(StackInd)-1]
		StackInd = StackInd[0 : len(StackInd)-1]

		q1 := Q[index]

		for _, u = range z {
			if u.Final {
				q1.Final = true
				break
			}
		}

		for _, a = range NFA.X {
			Union := make([]*State, 0)
			for _, u = range z {
				for _, w = range NFA.δ[u.name][a] {
					Union = append(Union, w)
				}
			}

			z_ := NFA.Closure(Union)
			q2 := toDetState(z_)

			index = q2.in(Q)
			if index == -1 {
				index = len(Q)
				q2.index = index

				Q = append(Q, q2)
				δ = append(δ, make(map[string]*DetState))

				Stack = append(Stack, z_)
				StackInd = append(StackInd, index)
			} else {
				q2 = Q[index]
			}
			δ[q1.index][a] = q2
		}
	}
	return &DeterministicFiniteAutomaton{
		Q:     Q,
		X:     NFA.X,
		δ:     δ,
		Final: nil,
		q0:    q0,
	}
}

func main() {
	ReadNFA().Det().PrintAutomaton()
}
