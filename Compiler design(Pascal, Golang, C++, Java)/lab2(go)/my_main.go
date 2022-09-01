package main

import (
	"fmt"
	"go/ast"
	"go/format"
	"go/parser"
	"go/token"
	"log"
	"os"
)

func deleteFUNCNAME(file *ast.File) {
	ast.Inspect(file, func(n ast.Node) bool {
		for fileIter, x := range file.Decls {
			if genDecl, ok := x.(*ast.GenDecl); ok {
				if genDecl.Tok == token.VAR {
					for _, spec := range genDecl.Specs {
						valueSpec := spec.(*ast.ValueSpec)
						for i := range valueSpec.Values {
							if _, err := valueSpec.Values[i].(*ast.Ident); err {
								rightValue := valueSpec.Values[i].(*ast.Ident)
								if rightValue.Name == "FUNCNAME" {
									changeValue := []ast.Expr{
										&ast.BasicLit{
											ValuePos: rightValue.NamePos,
											Kind:     token.STRING,
											Value:    "\"(global)\"",
										},
									}
									valueSpec.Values = changeValue
								}
							}
						}

					}
				}
			}
			if funcDecl, ok := x.(*ast.FuncDecl); ok {
				body := funcDecl.Body
				for _, bodyList := range body.List {
					if exprStmt, err := bodyList.(*ast.ExprStmt); err {
						for i := range exprStmt.X.(*ast.CallExpr).Args {
							if _, err := exprStmt.X.(*ast.CallExpr).Args[i].(*ast.Ident); err {
								argsValue := exprStmt.X.(*ast.CallExpr).Args[i].(*ast.Ident)
								if argsValue.Name == "FUNCNAME" {
									changeValue := []ast.Expr{
										&ast.BasicLit{
											ValuePos: argsValue.NamePos,
											Kind:     token.STRING,
											Value:    "\"" +funcDecl.Name.Name + "\"",
										},
									}
									exprStmt.X.(*ast.CallExpr).Args = changeValue
								}
							}
						}
					}
					if decl, err := bodyList.(*ast.DeclStmt); err {
						declStmt := decl.Decl.(*ast.GenDecl)
						for _, spec := range declStmt.Specs {
							valueSpec := spec.(*ast.ValueSpec)
							for  _, exprValue := range valueSpec.Values { //
								binaryVlue := exprValue.(*ast.BinaryExpr)
								if _, err := binaryVlue.X.(*ast.Ident); err {
									xValue := binaryVlue.X.(*ast.Ident)
									if xValue.Name == "FUNCNAME" {
										binaryVlue.X = nil
										changeValue := &ast.BasicLit{
												ValuePos: xValue.NamePos,
												Kind:     token.STRING,
												Value:    "\"" + funcDecl.Name.Name + "\"",
										}
										binaryVlue.X = changeValue
									}
								}
								if _, err := binaryVlue.Y.(*ast.Ident); err {
									yValue := binaryVlue.Y.(*ast.Ident)
									if yValue.Name == "FUNCNAME" {
										binaryVlue.Y = nil
										changeValue := &ast.BasicLit{
											ValuePos: yValue.NamePos,
											Kind:     token.STRING,
											Value:    "\"" + funcDecl.Name.Name + "\"",
										}
										binaryVlue.Y = changeValue
									}
								}
							}
						}
					}
				}
			}
			if genDecl, ok := x.(*ast.GenDecl); ok {
				if genDecl.Tok == token.VAR {
					for _, spec := range genDecl.Specs {
						valueSpec := spec.(*ast.ValueSpec)
						if valueSpec.Names[0].Name == "FUNCNAME" {
							if _, ok := valueSpec.Type.(*ast.Ident); ok {
								identValue := valueSpec.Type.(*ast.Ident)
								if identValue.Name != "string" {
									fmt.Println("Error: FUNCNAME is not STRING")
									return false
								}
								copy(file.Decls[fileIter:], file.Decls[fileIter+1:])
								file.Decls[len(file.Decls)-1] = nil
								file.Decls = file.Decls[:len(file.Decls)-1]
							}
						}
					}
				}
			}
		}
		return true
	})
}

func main() {
	fset := token.NewFileSet()

	file, err := parser.ParseFile(fset, os.Args[1], nil, parser.ParseComments)
	if err != nil {
		log.Fatal(err.Error())
	}
	deleteFUNCNAME(file)
	err = format.Node(os.Stdout, fset, file)
	if err != nil {
		log.Fatal(err.Error())
	}
}
