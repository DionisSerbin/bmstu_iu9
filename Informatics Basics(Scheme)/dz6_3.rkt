(load "dz6_2.rkt")

(define (tree->scheme xs)
  (if (list? xs)
      (list (if (equal? (cadr xs) '^)
                'expt
                (cadr xs))
            (tree->scheme (car xs))
            (tree->scheme (caddr xs)))
      xs))

(tree->scheme (parse (tokenize "a + b/c^2 - d")))
(tree->scheme (parse (tokenize "x^(a + 1)")))
(eval (tree->scheme (parse (tokenize "2^2^2^2")))
      (interaction-environment))

(define (find? symbol)
  (and (>= (char->integer symbol) 65) (<= (char->integer symbol) 90)))
      
  