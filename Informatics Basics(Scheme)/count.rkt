(define (count2 n x xs)
  (if (null? xs)
      n
      (if (eq? (car xs) x)
          (count2 (+ n 1) x (cdr xs) )
          (count2 n x (cdr xs) ) ) ) )
(define (count x xs) (count2 0 x xs) )
(count 'a '(a b c a))      
      
      
  