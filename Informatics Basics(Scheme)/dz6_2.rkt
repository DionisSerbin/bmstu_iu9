;Expr    ::= Term Expr~ .
;Expr~   ::= AddOp Term Expr~ | .
;Term    ::= Factor Term~ .
;Term~   ::= MulOp Factor Term~ | .
;Factor  ::= Power Factor~ .
;Factor~ ::= PowOp Power Factor~ | .
;Power   ::= value | "(" Expr ")" | unaryMinus Power .

(load "dz6_1.rkt")

(define escape #f)

(define tok '(+ - * / ^ "(" ")"))

(define (right-assoc xs)
  (if (= (length xs) 1)
      (car xs)
      (list (car xs) (cadr xs) (right-assoc (cddr xs)))))

(define (left-assoc xs)
  (if (= (length xs) 1)
      (car xs)
      (left-assoc (cons (list (car xs) (cadr xs) (caddr xs)) (cdddr xs)))))

(define call/cc call-with-current-continuation)

(define (parse xs)
  (define (parsing xs args)
    (and
     (call/cc
      (lambda (cc) (set! escape cc)))
     (letrec ((curVal (lambda () (car xs)))
              (next (lambda () (set! xs (cdr xs))))
              (term (lambda (arg) (and (or (and (not (null? xs))
                                                (equal? (curVal) arg))
                                           (escape #f))
                                       (next)
                                       arg)))
              (term? (lambda (arg)
                       (and (not (null? xs))
                            (equal? (curVal) arg)
                            (next)
                            arg)))
              (check (lambda (arg) (or ((cdr (assoc arg dict)))
                                       (escape #f))))
              (val (lambda () (and (equal? (member (curVal) tok) #f)
                                   (let ((cur (curVal)))
                                     (next)
                                     cur))))
              (dict (list
                     (cons 'expr
                           (lambda ()
                             (left-assoc (cons (check 'term) (check 'expr~)))))
                     (cons 'expr~
                           (lambda ()
                             (let ((n (or (term? '+) (term? '-))))
                               (if n
                                   (cons n (cons (check 'term) (check 'expr~)))
                                   '()))))
                     (cons 'term
                           (lambda ()
                             (left-assoc (cons (check 'factor) (check 'term~)))))
                     (cons 'term~
                           (lambda ()
                             (let ((n (or (term? '*) (term? '/))))
                               (if n
                                   (cons n (cons (check 'factor) (check 'term~)))
                                   '()))))
                     (cons 'factor
                           (lambda ()
                             (right-assoc (cons (check 'power) (check 'factor~)))))
                     (cons 'factor~
                           (lambda ()
                             (let ((n (term? '^)))
                               (if n
                                   (cons n (cons (check 'power) (check 'factor~)))
                                   '()))))
                     (cons 'power
                           (lambda ()
                             (cond
                               ((term? '-) (list '- (check 'power)))
                               ((term? "(") ((lambda ()
                                              (let ((n (check 'expr)))
                                              (term ")")
                                              n))))
                             ((val))
                             (else #f)))))))
     (let ((ans (check args)))
       (and (null? xs) ans)))))
(parsing xs 'expr))

(parse (tokenize "a/b/c/d"))
(parse (tokenize "a^b^c^d"))
(parse (tokenize "a/(b/c)"))
(parse (tokenize "a + b/c^2 - d"))