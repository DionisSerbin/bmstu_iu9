(define (my-element? x xs)
 (and (not(null? xs)) (or (equal? x (car xs)) (my-element? x (cdr xs) ) ) ) )
(my-element? 1 '(3 2 1))
(my-element? 4 '(3 2 1))


      