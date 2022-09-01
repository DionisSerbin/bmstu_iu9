(define (o . f)
  (cond
    ((null? f)(lambda (x) x))
    ((and
      (null? (car f))
      (null? (cdr f))) x)
    ((null? (cdr f)) (lambda (x) ((car f) x)))
    (else(lambda (x) ((car f) ((apply o (cdr f)) x))))))

(define (f x) (+ x 3)) 
(define (g x) (* x 2)) 
(define (h x) (- x)) 

((o f g f g h h) 1) 
((o f g h) 2) 
((o h) 1) 
((o) 1)