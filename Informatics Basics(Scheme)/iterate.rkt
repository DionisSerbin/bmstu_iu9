(define (iterate f x n)
  (define (iterate2 f x n xs)
    (if (= n 0)
         (reverse xs)
         (iterate2 f (f x) (- n 1)  (cons  x xs))))
  (iterate2 f x  n '() ) )
       
(iterate (lambda (x) (* 2 x) ) 1 6)
(iterate (lambda (x) (* 2 x) ) 1 1)