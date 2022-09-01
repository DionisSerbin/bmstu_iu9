// FormulaOrder project main.go
package main

import (
        "fmt"
        "io/ioutil"
	"os"
	"sort"
	"strconv"
)

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
	return G.array[G.arrSort[i]].timeOut < G.array[G.arrSort[j]].timeOut
}

type Tocken struct {
	teg byte
	//0 - <number>
	//1 - <ident>
	//2 - <spec_symbol>
	//3 - <endl>
	//4 - <endf>
	value interface{}
}

type Vertex struct {
	title   string
	name    []string
	value   []string
	arrIn   []*Vertex
	arrOut  []*Vertex
	index   int
	timeIn  int
	timeOut int
	low     int
	comp    int
}

type lexems struct {
	index int
	num   int
	array      []Tocken
	nameFormul []string
	arrUsed       []string
	mapDeclareted map[string]*Vertex
	currentVertex *Vertex
	V             []*Vertex
}

func (lex lexems) err() {
	fmt.Printf("syntax error")
	os.Exit(0)
}

func (lex *lexems) lexer(text string) {
	flagNum := false
	flagVal := false
	var c byte
	start := 0
	startFormul := 0
	for i := 0; i < len(text); i++ {
		c = text[i]
		if flagNum {
			if !(c >= '0' && c <= '9') {
				num, _ := strconv.Atoi(text[start:i])
				lex.array = append(lex.array, Tocken{teg: 0, value: num})
				flagNum = false
			} else {
				continue
			}
		}
		if flagVal {
			if !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
				lex.array = append(lex.array, Tocken{teg: 1, value: text[start:i]})
				flagVal = false
			} else {
				continue
			}
		}
		switch c {
		case '=', '+', '-', '/', '*', '(', ')', ',':
			lex.array = append(lex.array, Tocken{teg: 2, value: c})
		case '\n':
			lex.array = append(lex.array, Tocken{teg: 3, value: nil})
			lex.nameFormul = append(lex.nameFormul, text[startFormul:i])
			startFormul = i + 1
		case ' ', '\t':
			continue
		default:
			if c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' {
				flagVal = true
				start = i
			} else if c >= '0' && c <= '9' {
				flagNum = true
				start = i
			} else {
				//ERROR
				lex.err()
			}
		}
	}
	if flagNum {
		num, _ := strconv.Atoi(text[start:len(text)])
		lex.array = append(lex.array, Tocken{teg: 0, value: num})
		flagNum = false
	}
	if flagVal {
		lex.array = append(lex.array, Tocken{teg: 1, value: text[start:len(text)]})
		flagVal = false
	}
	if startFormul < len(text) {
		lex.nameFormul = append(lex.nameFormul, text[startFormul:len(text)])
	}
	//lex.array = append(lex.array, Tocken{teg: 3, value: nil})
	lex.array = append(lex.array, Tocken{teg: 4, value: nil})
}

func (lex *lexems) Parser() {
	lex.Program()
}

func (lex lexems) Teg() byte {
	return lex.array[lex.index].teg
}

func (lex lexems) Val() interface{} {
	return lex.array[lex.index].value
}

func (lex *lexems) Expect(a byte) {
	if lex.Teg() == a {
		lex.index++
		return
	} else {
		//ОШИБКА
		lex.err()
	}
}

func (lex *lexems) Expect2(a byte, v byte) {
	if lex.Teg() == a && lex.Val().(byte) == v {
		lex.index++
		return
	} else {
		//ОШИБКА
		lex.err()
	}
}

func (lex *lexems) Program() {
	lex.currentVertex = &Vertex{
		title:   lex.nameFormul[lex.num],
		name:    make([]string, 0),
		value:   make([]string, 0),
		arrIn:   make([]*Vertex, 0),
		arrOut:  make([]*Vertex, 0),
		index:   lex.num,
		timeIn:  0,
		timeOut: 0,
		low:     0,
		comp:    -1,
	}
	lex.num++
	lex.Function()
	lex.V = append(lex.V, lex.currentVertex)
	lex.Expect(3)
	if !(lex.Teg() == 4) {
		lex.Program()
	}
}

func (lex *lexems) Function() {
	lex.Ident_List()
	lex.Expect2(2, '=')
	for _, s := range lex.currentVertex.name {
		if _, ok := lex.mapDeclareted[s]; ok {
			lex.err()
		} else {
			lex.mapDeclareted[s] = lex.currentVertex //???
		}
	}
	num := lex.Expr_List(0) + 1
	if num != len(lex.currentVertex.name) {
		//ОШИБКА
		lex.err()
	}
}

func (lex *lexems) Ident_List() {
	lex.Expect(1)
	lex.currentVertex.name = append(lex.currentVertex.name, lex.array[lex.index-1].value.(string))
	if lex.Teg() == 2 && lex.Val().(byte) == ',' {
		lex.index++
		lex.Ident_List()
	}
}

func (lex *lexems) Expr_List(num int) int {
	lex.Arith_Expr()
	if lex.Teg() == 2 && lex.Val().(byte) == ',' {
		lex.index++
		num = lex.Expr_List(num + 1)
	}
	return num
}

func (lex *lexems) Arith_Expr() {
	lex.Term()
	lex.Arith_Expr2()
}

func (lex *lexems) Arith_Expr2() {
	if lex.Teg() == 2 && (lex.Val().(byte) == '+' || lex.Val().(byte) == '-') {
		lex.index++
		lex.Term()
		lex.Arith_Expr2()
	}
}

func (lex *lexems) Term() {
	lex.Factor()
	lex.Term2()
}

func (lex *lexems) Term2() {
	if lex.Teg() == 2 && (lex.Val().(byte) == '*' || lex.Val().(byte) == '/') {
		lex.index++
		lex.Factor()
		lex.Term2()
	}
}

func (lex *lexems) Factor() {
	switch {
	case lex.Teg() == 0:
		lex.index++
	case lex.Teg() == 1:
		lex.currentVertex.value = append(lex.currentVertex.value, lex.Val().(string))
		lex.arrUsed = append(lex.arrUsed, lex.Val().(string))
		lex.index++
	case lex.Teg() == 2 && lex.Val().(byte) == '(':
		lex.index++
		lex.Arith_Expr()
		lex.Expect2(2, ')')
	case lex.Teg() == 2 && lex.Val().(byte) == '-':
		lex.index++
		lex.Factor()
	case lex.Teg() == 2 && lex.Val().(byte) == ',':
		return
	default:
		//ОШИБКА
		lex.err()
	}
}

func (lex *lexems) makeGraph() {
	for _, v := range lex.V {
		for _, s := range v.value {
			if u, ok := lex.mapDeclareted[s]; ok {
                                if v == u {
        				fmt.Println("cycle")
					os.Exit(0)
				}
				v.arrIn = append(v.arrIn, u)
				u.arrOut = append(u.arrOut, v)
			} else {
				lex.err()
			}
		}
	}
}

//Stack

type (
	Stack struct {
		top    *node
		length int
	}
	node struct {
		value *Vertex
		prev  *node
	}
)

func New() *Stack {
	return &Stack{nil, 0}
}

func (this *Stack) Pop() *Vertex {
	n := this.top
	this.top = n.prev
	this.length--
	return n.value
}

func (this *Stack) Push(value *Vertex) {
	n := &node{value, this.top}
	this.top = n
	this.length++
}

//Stack end

var timeIn int
var timeOut int
var count int

func (lex *lexems) Tarjan() {
	var v *Vertex
	timeIn = 1
	timeOut = 1
	count = 0
	S := New()
	for _, v = range lex.V {
		if v.timeIn == 0 {
			lex.VisitVertex_Tarjan(v, S)
		}
	}
	if count != lex.num {
		fmt.Printf("cycle")
		os.Exit(0)
	}
}

func (lex *lexems) VisitVertex_Tarjan(v *Vertex, S *Stack) {
	var u *Vertex
	v.timeIn, v.low = timeIn, timeIn
	timeIn++
	S.Push(v)
	for _, u = range v.arrIn {
		if u.timeIn == 0 {
			lex.VisitVertex_Tarjan(u, S)
		}
		if u.comp == -1 && v.low > u.low {
			v.low = u.low
		}
	}
	if v.timeIn == v.low {
		for {
			u = S.Pop()
			u.comp = count
			if u == v {
				break
			}
		}
		count++
	}
	v.timeOut = timeOut
	timeOut++
}

func main() {
	buf, err := ioutil.ReadAll(os.Stdin)
        if err != nil {
                fmt.Println("error")
                return
        }
        text := string(buf)
        if len(text) == 0 {
                fmt.Println("null line")
                return
        }
	lex := lexems{
		index:         0,
		num:           0,
		array:         make([]Tocken, 0),
		nameFormul:    make([]string, 0),
		arrUsed:       make([]string, 0),
		mapDeclareted: make(map[string]*Vertex, 0),
		currentVertex: nil,
		V:             make([]*Vertex, 0),
	}
	lex.lexer(text)
	lex.Parser()
	lex.makeGraph()
	lex.Tarjan()
	arrSort := make([]int, lex.num)
	for i := 0; i < len(arrSort); i++ {
		arrSort[i] = i
	}
	GS := GraphSort{array: lex.V, arrSort: arrSort}
	sort.Sort(GS)
	for i := 0; i < len(GS.arrSort); i++ {
		fmt.Println(GS.array[GS.arrSort[i]].title)
	}
}
