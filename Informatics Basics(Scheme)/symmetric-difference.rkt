(define (find x xs) 
  (and (not(null? xs) )
       (or (= (car xs) x) 
           (find x (cdr xs)) ) ) ) 
(define (symmetric-difference xs ys)
  
  (define (diff2 ys xxs zs)
    (if (null? ys)
        zs
        (if (find (car ys) xxs)
            (diff2 (cdr ys) xxs zs)
            (diff2 (cdr ys) xxs (cons (car ys) zs) ) ) ) )
  
  (define (diff xs ys zs xxs )
    (if (null? xs) 
        (diff2 ys xxs zs)
        (if (find (car xs) ys) 
            (diff (cdr xs) ys zs (cons (car xs)xxs)) 
            (diff (cdr xs) ys (cons (car xs) zs) (cons (car xs)xxs) )) ) )
  
  (diff xs ys '() '()))
(symmetric-difference '(1 2 3 4) '(3 4 5 6))