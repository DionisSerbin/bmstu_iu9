(load "Utest.rkt")
(load "dz3.scm")

(define the-tests
  (list (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(5)))
                          2)
                    (interaction-environment))
              '0)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(x)))
                          2)
                    (interaction-environment))
              1)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(- x)))
                          2)
                    (interaction-environment))
              -1)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 1 x)))
                          2)
                    (interaction-environment))
              1)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* -1 x)))
                          2)
                    (interaction-environment))
              -1)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* -4 x)))
                          2)
                    (interaction-environment))
              -4)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 10 x)))
                          2)
                    (interaction-environment))
              10)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(- (* 2 x) 3)))
                          2)
                    (interaction-environment))
              2)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(expt x 10)))
                          2)
                    (interaction-environment))
              5120)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 2 (expt x 5))))
                          3)
                    (interaction-environment))
              810)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(expt x -2) ))
                          3)
                    (interaction-environment))
              (- (/ 2 27)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(expt x -2) ))
                          3)
                    (interaction-environment))
              (- (/ 2 27)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(expt 5 x)))
                          2)
                    (interaction-environment))
              (* 25 (log 5)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(cos x)))
                          1)
                    (interaction-environment))
              (- (sin 1)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(sin x)))
                          5)
                    (interaction-environment))
              (cos 5))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(expt (exp 1) x)))
                          25)
                    (interaction-environment))
              (expt (exp 1) 25))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 2 (expt (exp 1) x))))
                          3)
                    (interaction-environment))
              (* 2 (expt (exp 1) 3)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 2 (expt (exp 1) (* 2 x)))))
                          2)
                    (interaction-environment))
              (* 4 (expt (exp 1) (* 4))))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(log x)))
                          123)
                    (interaction-environment))
              (/ 1 123))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 3 (log x))))
                          123)
                    (interaction-environment))
              (/ 3 123))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(+ (expt x 3) (expt x 2))))
                          3)
                    (interaction-environment))
              33)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(- (* 2 (expt x 3)) (* 2 (expt x 2)))))
                          4)
                    (interaction-environment))
              80)
        
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 2 (sin x) (cos x))))
                          0)
                    (interaction-environment))
              2)
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* 2 (expt (exp 1) x) (sin x) (cos x))))
                          5)
                    (interaction-environment))
              (* 2 (+ (* (expt (exp 1) 5) (sin 5) (cos 5)) (* (expt (exp 1) 5) (- (expt (cos 5) 2) (expt (sin 5) 2))))))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(sin (* 2 x))))
                          15)
                    (interaction-environment))
              (* (cos (* 2 15)) 2))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(cos (* 2 (expt x 2)))))
                          321)
                    (interaction-environment))
              (* (- (sin (* 2 (expt 321 2)))) (* 4 321)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(sin (log (expt x 2)))))
                          3)
                    (interaction-environment))
              (* (cos (log 9)) (/ 6 9)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(+ (sin (* 2 x)) (cos (* 2 (expt x 2))))))
                          4)
                    (interaction-environment))
              (+ (* 2 (cos 8)) (* (- (sin 32)) 16)))
        (test (eval (list (list 'lambda 
                                '(x) 
                                (derivative '(* (sin (* 2 x)) (cos (* 2 (expt x 2))))))
                          3)
                    (interaction-environment))
              (+ (* (* 2 (cos 6)) (cos 18)) (* (sin 6) (* (- (sin 18)) 12))))
        ))

(run-tests the-tests)