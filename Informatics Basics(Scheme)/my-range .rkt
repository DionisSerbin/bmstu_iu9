(define (my-range a b d)

  (define (my-range2 a b d xs)

  (if (not(< a b))
          (reverse  xs)
          (my-range2 (+ a d) b d (cons a xs) ) ) )
  (my-range2 a b d '()  ) )
(my-range 0 11 3)
(my-range 5 100 4)
(my-range 18 200 15)
(my-range 2 30 7)