(define (my-range a b d)

  (define (my-range2 a b d xs)

    (if (not(< a b))
        (reverse  xs)
        (my-range2 (+ a d) b d (cons a xs) ) ) )
  (my-range2 a b d '()  ) )
(define (my-filter pred? xs)

  (define (my-filter2 pred? xs ys)
    (if (null? xs)
        (reverse ys)
        (if (not (pred? (car xs) ) )
            (my-filter2 pred? (cdr xs) ys)
            (my-filter2 pred? (cdr xs) (cons (car xs) ys) ) ) ) )
  (my-filter2 pred? xs '() ) )
(my-filter odd? (my-range 0 10 1))
(my-filter (lambda (x) (= (remainder x 3) 0)) (my-range 0 13 1))