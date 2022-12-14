(define-syntax lazy-cons
  (syntax-rules ()
    ((_ a b) (cons a (delay b)))))
(define (lazy-car x) (car x))
(define (lazy-cdr x) (force (cdr x)))
(define (lazy-head xs k)
  (if (= k 0)
      '()
      (cons (lazy-car xs) (lazy-head (lazy-cdr xs) (- k 1)))))
(define (lazy-ref xs k)
  (if (= k 1)
      (lazy-car xs)
      (lazy-ref (lazy-cdr xs) (- k 1))))
(define (naturals start)
  (lazy-cons start (naturals (+ start 1))))
(define (lazy-factorial n)
  (define (fact)
    (define (subfact xs n)
      (lazy-cons xs (subfact (* xs n) (+ n 1))))
    (subfact 1 2))
  (lazy-ref (fact) n))


(display (lazy-head (naturals 10) 12))
(begin
  (display (lazy-factorial 10)) (newline)
  (display (lazy-factorial 50)) (newline))
(lazy-head (naturals 10) 12)