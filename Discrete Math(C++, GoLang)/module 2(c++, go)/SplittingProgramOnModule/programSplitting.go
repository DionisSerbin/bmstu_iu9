// Modules project main.go
package main

import (
        "fmt"
	"io/ioutil"
	"os"
	"strconv"
)

type tocken struct {
	teg byte
	//0 - <number>
	//1 - <ident>
	//2 - <lparen>
	//3 - <rparen>
	//4 - <spec_symbol>
	//5 - <end>
	value interface{}
}

type pair struct {
	head, tail int
}

type elem struct {
	numPer, position int
}

type lexems struct {
	tail        int
	graph       []pair
	numFunc     int
	mapFunc     map[string]elem
	arr         []tocken
	index       int
	currentArgs []string
}

func (lex lexems) check(s string) {
	for i := 0; i < len(lex.currentArgs); i++ {
		if s == lex.currentArgs[i] {
			return
		}
	}
	lex.err()
}

func (lex lexems) err() {
	fmt.Printf("error")
	os.Exit(0)
}
func (lex lexems) Teg() byte {
	return lex.arr[lex.index].teg
}
func (lex lexems) Val() interface{} {
	return lex.arr[lex.index].value
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
func (lex *lexems) Expect2(a byte, v interface{}) {
	if lex.Teg() == a && lex.Val() == v {
		lex.index++
		return
	} else {
		//ОШИБКА
		lex.err()
	}
}
func (lex *lexems) Program() {
	lex.numFunc++
	lex.Function()
	if lex.Teg() == 5 {
		if len(lex.mapFunc) != lex.numFunc {
			lex.err()
		}
		return
	} else {
		lex.Program()
	}
}

func (lex *lexems) Function() {
	lex.Expect(1)
	namefunc := lex.arr[lex.index-1].value.(string)
	lex.Expect(2)
	n, s := lex.Formal_Args_List()
	lex.currentArgs = s
	if num, ok := lex.mapFunc[namefunc]; ok {
		if num.numPer != n {
			//ОШИБКА
			lex.err()
		}
		lex.tail = num.position
	} else {
		lex.tail = len(lex.mapFunc)
		lex.mapFunc[namefunc] = elem{numPer: n, position: lex.tail}
	}
	lex.Expect(3)
	lex.Expect2(4, ":=")
	lex.Expr()
	lex.Expect2(4, ";")
}

func (lex *lexems) Formal_Args_List() (int, []string) {
	s := make([]string, 0)
	if lex.Teg() != 3 {
		lex.Ident_List(&s)
	}
	return len(s), s
}

func (lex *lexems) Ident_List(s *[]string) {
	lex.Expect(1)
	*s = append(*s, lex.arr[lex.index-1].value.(string))
	if lex.Teg() == 4 && lex.Val() == "," {
		lex.index++
		lex.Ident_List(s)
	}
}

func (lex *lexems) Expr() {
	lex.Comparison_Expr()
	if lex.Teg() == 4 && lex.Val() == "?" {
		lex.index++
		lex.Comparison_Expr()
		lex.Expect2(4, ":")
		lex.Expr()
	}
}

func (lex *lexems) Comparison_Expr() {
	lex.Arith_Expr()
	if lex.Comparison_Op() {
		lex.index++
		lex.Arith_Expr()
	}
}

func (lex lexems) Comparison_Op() bool {
	return lex.Teg() == 4 && (lex.Val() == "=" || lex.Val() == "<>" ||
		lex.Val() == "<" || lex.Val() == ">" ||
		lex.Val() == "<=" || lex.Val() == ">=")
}

//<E> ::= <T><E'>.
//<E'> ::= + <T><E'> | - <T><E'> | .
//<T> ::= <F><T'>.
//<T'> ::= * <F><T'> | / <F><T'> | .
//<F> ::= <number> | <var> | ( <E> ) | - <F>.

func (lex *lexems) Arith_Expr() {
	lex.Term()
	lex.Arith_Expr2()
}

func (lex *lexems) Arith_Expr2() {
	if lex.Teg() == 4 && (lex.Val() == "+" || lex.Val() == "-") {
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
	if lex.Teg() == 4 && (lex.Val() == "*" || lex.Val() == "/") {
		lex.index++
		lex.Factor()
		lex.Term2()
	}
}

func (lex *lexems) Factor() {
	switch lex.Teg() {
	case 0:
		lex.index++
	case 1:
		lex.index++
		if lex.Teg() == 2 {
			namefunc := lex.arr[lex.index-1].value.(string)
			lex.index++
			n := lex.Actual_Args_List()
			if num, ok := lex.mapFunc[namefunc]; ok {
				if num.numPer != n {
					//ОШИБКА
					lex.err()
				}
				lex.graph = append(lex.graph, pair{tail: lex.tail, head: num.position})
			} else {
				lex.graph = append(lex.graph, pair{tail: lex.tail, head: len(lex.mapFunc)})
				lex.mapFunc[namefunc] = elem{numPer: n, position: len(lex.mapFunc)}
			}
			lex.Expect(3)
		} else {
			lex.check(lex.arr[lex.index-1].value.(string))
		}
	case 2:
		lex.index++
		lex.Expr()
		lex.Expect(3)
	case 4:
		if lex.Val() == "-" {
			lex.index++
			lex.Factor()
		} else {
			//ОШИБКА
			lex.err()
		}
	default:
		//ОШИБКА
		lex.err()
	}
}

func (lex *lexems) Actual_Args_List() int {
	k := 0
	if lex.Teg() != 3 {
		k++
		lex.Expr_List(&k)
	}
	return k
}

func (lex *lexems) Expr_List(k *int) {
	lex.Expr()
	if lex.Teg() == 4 && lex.Val() == "," {
		lex.index++
		*k++
		lex.Expr_List(k)
	}
}

func Lexer(text string) []tocken {
	arr := make([]tocken, 0)
	var flagnum, flagval bool
	flagnum = false
	flagval = false
	var c byte
	var start, num, i int
	start = 0
	for i = 0; i < len(text); i++ {
		c = text[i]
		if flagnum {
			if c > '9' || c < '0' {
				num, _ = strconv.Atoi(text[start:i])
				arr = append(arr, tocken{teg: 0, value: num})
				flagnum = false
			} else {
				continue
			}
		}
		if flagval {
			if !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
				arr = append(arr, tocken{teg: 1, value: text[start:i]})
				flagval = false
			} else {
				continue
			}
		}
		switch c {
		case '=', ',', '*', '/', '+', '-', '?', ';':
			arr = append(arr, tocken{teg: 4, value: text[i : i+1]})
		case ':', '>', '<':
			if i+1 < len(text) && (text[i+1] == '=' || c == '<' && text[i+1] == '>') {
				arr = append(arr, tocken{teg: 4, value: text[i : i+2]})
				i++
			} else {
				arr = append(arr, tocken{teg: 4, value: text[i : i+1]})
			}
		case '(':
			arr = append(arr, tocken{teg: 2, value: nil})
		case ')':
			arr = append(arr, tocken{teg: 3, value: nil})
		case ' ', '\n', '\t':
			continue
		default:
			if c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' {
				start = i
				flagval = true
			} else if c >= '0' && c <= '9' {
				start = i
				flagnum = true
			} else {
				//ОШИБКА
				fmt.Printf("error")
				os.Exit(0)
			}
		}
	}
	if flagnum {
		num, _ = strconv.Atoi(text[start:i])
		arr = append(arr, tocken{teg: 0, value: num})
		flagnum = false
	}
	if flagval {
		arr = append(arr, tocken{teg: 1, value: text[start:i]})
		flagval = false
	}
	arr = append(arr, tocken{teg: 5, value: nil})
	return arr
}

func Parser(text string) lexems {
	var lex lexems
	lex.graph = make([]pair, 0)
	lex.arr = Lexer(text)
	lex.index = 0
	lex.mapFunc = make(map[string]elem)
	lex.numFunc = 0
	lex.Program()
	return lex
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

type Vertex struct {
	name   int
	timeIn int
	low    int
	comp   int
}

var time int
var count int

func Tarjan(G [][]*Vertex, V []*Vertex) {
	var v *Vertex
	time = 1
	count = 0
	S := New()
	for _, v = range V {
		if v.timeIn == 0 {
			VisitVertex_Tarjan(G, v, S)
		}
	}
}
func VisitVertex_Tarjan(G [][]*Vertex, v *Vertex, S *Stack) {
	var u *Vertex
	v.timeIn, v.low = time, time
	time += 1
	S.Push(v)
	for _, u = range G[v.name] {
		if u.timeIn == 0 {
			VisitVertex_Tarjan(G, u, S)
		}
		if u.comp == -1 && v.low > u.low {
			v.low = u.low
		}
	}
	if v.timeIn == v.low {
		for {
			u = S.Pop()
			u.comp = count
			if u.name == v.name {
				break
			}
		}
		count++
	}
}

func main() {
	buf, _ := ioutil.ReadAll(os.Stdin)
	lex := Parser(string(buf))

	var m, n, a, b, i int
	n = lex.numFunc
	m = len(lex.graph)
	V := make([]*Vertex, n)
	for i = 0; i < n; i++ {
		V[i] = &Vertex{
			name:   i,
			timeIn: 0,
			comp:   -1,
			low:    0,
		}
	}
	G := make([][]*Vertex, n)
	for i = 0; i < m; i++ {
		a = lex.graph[i].tail
		b = lex.graph[i].head
		G[a] = append(G[a], V[b])
	}
	Tarjan(G, V)
	fmt.Printf("%d", count)
}
