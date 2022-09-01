// Loops3 project main.go
package main

import (
        "fmt"
	"sort"
)

type Vertex struct {
	cod byte
	//0 - ACTION
	//1 - BRANCH
	//2 - JUMP
	flag                               bool
	pathName, index, Tin               int
	parent, sdom, label, ancestor, dom *Vertex
	arrOut, bucket                     []*Vertex
}

type GraphSort struct {
	array   []*Vertex
	arrSort []int
}

func (G GraphSort) Len() int {
	return len(G.arrSort)
}

func (G GraphSort) Swap(i, j int) {
	G.arrSort[i], G.arrSort[j] = G.arrSort[j], G.arrSort[i]
}

func (G GraphSort) Less(i, j int) bool {
	return G.array[G.arrSort[i]].Tin < G.array[G.arrSort[j]].Tin
}

var time int

func CalculateTimes(G [][]*Vertex, V []*Vertex) {
	time = 1
	for _, v := range V {
		v.Tin, v.parent = 0, nil
	}
	VisitVertex(G, V[0])
	V[0].parent = V[0]
}

func VisitVertex(G [][]*Vertex, v *Vertex) {
	v.flag = true
	v.Tin = time
	time++
	for _, u := range G[v.index] {
		if u.Tin == 0 {
			u.parent = v
			VisitVertex(G, u)
		}
	}
}

func FindMin(v *Vertex) *Vertex {
	SearchAndCut(v)
	return v.label
}

func SearchAndCut(v *Vertex) *Vertex {
	var root *Vertex
	if v.ancestor == nil {
		return v
	} else {
		root = SearchAndCut(v.ancestor)
		if v.ancestor.label.sdom.Tin < v.label.sdom.Tin {
			v.label = v.ancestor.label
		}
		v.ancestor = root
	}
	return root
}

func Dominators(G [][]*Vertex, GS GraphSort) {
	var w, u, v *Vertex
	var i int
	n := len(GS.arrSort)
	for _, w = range GS.array {
		w.sdom, w.label = w, w
	}
	for i = n - 1; i > 0; i-- {
		w = GS.array[GS.arrSort[i]]
		for _, v = range w.arrOut {
			if !v.flag {
				continue
			}
			u = FindMin(v)
			if u.sdom.Tin < w.sdom.Tin {
				w.sdom = u.sdom
			}
		}
		w.ancestor = w.parent
		w.sdom.bucket = append(w.sdom.bucket, w)
		for _, v = range w.parent.bucket {
			u = FindMin(v)
			if u.sdom == v.sdom {
				v.dom = v.sdom
			} else {
				v.dom = u
			}
		}
		w.parent.bucket = make([]*Vertex, 0)
	}
	for i = 1; i < n; i++ {
		w = GS.array[GS.arrSort[i]]
		if w.dom != w.sdom {
			w.dom = w.dom.dom
		}
	}
	GS.array[0].dom = nil
}

func main() {
	var (
		n, i, name, nameIn int
		cod                string
	)
	var u, v *Vertex
	fmt.Scan(&n)
	V := make([]*Vertex, n)
	mapName := make(map[int]int) //map[name]index
	for i = 0; i < n; i++ {
		fmt.Scan(&name)
		mapName[name] = i
		fmt.Scan(&cod)
		V[i] = &Vertex{
			cod:      0,
			pathName: -1,
			index:    i,
			Tin:      0,
			parent:   nil,
			sdom:     nil,
			ancestor: nil,
			label:    nil,
			bucket:   make([]*Vertex, 0),
			dom:      nil,
			flag:     false,
			arrOut:   make([]*Vertex, 0),
		}
		switch cod {
		case "ACTION":
			continue
		case "BRANCH":
			fmt.Scan(&nameIn)
			V[i].cod = 1
			V[i].pathName = nameIn
		case "JUMP":
			fmt.Scan(&nameIn)
			V[i].cod = 2
			V[i].pathName = nameIn
		}
	}
	G := make([][]*Vertex, n)
	for i, v = range V {
		switch v.cod {
		case 0:
			if i != n-1 {
				V[i+1].arrOut = append(V[i+1].arrOut, v)
				G[i] = append(G[i], V[i+1])
			}
		case 1:
			if i != n-1 {
				V[i+1].arrOut = append(V[i+1].arrOut, v)
				G[i] = append(G[i], V[i+1])
			}
			u = V[mapName[v.pathName]]
			u.arrOut = append(u.arrOut, v)
			G[i] = append(G[i], u)
		case 2:
			u = V[mapName[v.pathName]]
			u.arrOut = append(u.arrOut, v)
			G[i] = append(G[i], u)
		}
	}
	CalculateTimes(G, V)
	arrSort := make([]int, n)
	for i = 0; i < n; i++ {
		arrSort[i] = i
	}
	GS := GraphSort{
		array:   V,
		arrSort: arrSort,
	}
	sort.Sort(GS)
	for i = 0; i < n && GS.arrSort[i] != 0; i++ {
	}
	GS.arrSort = GS.arrSort[i:]
	//fmt.Println(GS.arrSort)
	Dominators(G, GS)
	count := 0
	for _, i = range GS.arrSort {
		v = V[i]
		for _, u = range v.arrOut {
			if !u.flag {
				continue
			}
			for ; u != nil && u != v; u = u.dom {
			}
			if u == v {
				count++
				break
			}
		}
	}
	fmt.Println(count)
}
