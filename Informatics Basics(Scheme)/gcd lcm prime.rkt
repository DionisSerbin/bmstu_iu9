(define (my-gcd a b)
  (cond
    ((or (= a 0) (= b 0)) (abs (- a b)))
    ((or (> a b) (= a b)) (my-gcd b (- a b)))
    ((< a b) (my-gcd a (- b a)))))
(define (my-lcm a b) (/ (abs(* a b)) ( my-gcd a b)))
(define (prime? n)
  (define (iter i)
    (if (=(remainder n i)0)
        #f
        (if (or (> i (sqrt n)) (= i (sqrt n)))
            #t
            (iter (+ i 1)))))
  (if (= n 2)
      #t
      (iter 2)))