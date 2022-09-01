(load "Utest.rkt")
(load "my-abs.rkt")

(define examples (list (test (my-abs -2) 2)
                       (test (my-abs 2) 2)
                       (test (my-abs 0) 0)))
(define (my-abs x) (if (< x 0) (- x) x))

(run-tests examples)

(define (signum x)
  (cond
    ((< x 0) -1)
    ((= x 0) 1)
    (else 1)))
(define the-tests
         (list (test (signum -2) -1)
               (test (signum 0) 0)
               (test (signum 2) 1)))
(run-tests the-tests)
      
           