(define (my-flatten xs)
  (if (null? xs)
      '()
      (if (not (list? xs))
          (list xs)
          (append 
           (if (list? (car xs))
               (my-flatten (car xs))
               (list (car xs)))
           (my-flatten (cdr xs)))))) 

(define (any? pred? xs)
  (and (not (null? xs))
       (or (pred? (car xs)) (any? pred? (cdr xs))))) 

(define (const? expr)
  (not (any? (lambda (a) (equal? 'x a)) (my-flatten expr)))) 

(define (func+ expr) `(+ ,@(map derivative (cdr expr))))

(define (func- expr) `(- ,@(map derivative (cdr expr))))

(define (func* expr)
  `(+ (* ,(cadr expr)
         ,(derivative (append `(*) (cddr expr))))
      (* ,(derivative (cadr expr))
         ,@(cddr expr))))

(define (func/ expr)
  `(/ (- (* ,(derivative (cadr expr)) ,@(cddr expr))
         (* ,(cadr expr) ,@(map derivative (cddr expr))))
      (expt (* ,@(cddr expr)) 2)))

(define (funcexpt expr)
  (let ((a (cadr expr)) (b (caddr expr)))
    (cond
      ((const? b) `(* ,(derivative a)
                      ,b
                      (expt ,a (- ,b 1))))
      ((const? a) `(* ,expr (log ,a) ,(derivative b)))
      (else `(* ,expr ,(derivative `(* (log ,a) ,b)))))))

(define (funcexp expr)
  (if (const? (cadr expr))
      expr
      (derivative `(expt (exp 1) ,@(cdr expr)))))

(define (funclog expr)
  `(/ ,(derivative (cadr expr)) ,(cadr expr)))

(define (funcsin expr)
  `(* ,(derivative (cadr expr)) (cos ,(cadr expr))))

(define (funccos expr)
  `(* -1 ,(derivative (cadr expr)) (sin ,(cadr expr))))

(define (derivative expr)
  (cond
    ((const? expr) 0)
    ((or (equal? 'x expr)
         (equal? 'x (car expr))) 1)
    ((equal? '- (car expr)) (func- expr))
    ((equal? '+ (car expr)) (func+ expr))
    ((equal? '* (car expr)) (func* expr))
    ((equal? '/ (car expr)) (func/ expr))
    ((equal? 'expt (car expr)) (funcexpt expr))
    ((equal? 'exp (car expr)) (funcexp expr))
    ((equal? 'log (car expr)) (funclog expr))
    ((equal? 'sin (car expr)) (funcsin expr))
    ((equal? 'cos (car expr)) (funccos expr))))
load"tests.scm"
(run-tests the-tests)  