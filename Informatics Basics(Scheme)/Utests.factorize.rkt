(load "Utest.rkt")
(load "factorize.rkt")





(define the-tests-factorize
  (list
   (test (factorize '(- (expt x 2) (expt y 2))) '(* (- x y) (+ x y)))
   (test (factorize '(- (expt (+ first 1) 2) (expt (- second 1) 2))) '(* (- (+ first 1) (- second 1))
                                                                         (+ (+ first 1) (- second 1))))
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 2) (expt y 2))))
                     1 2)
               (interaction-environment)) '-3)
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 2) (expt y 2))))
                     3 2)
               (interaction-environment)) '5)
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 2) (expt y 2))))
                     10 10)
               (interaction-environment)) '0)
   (test (factorize '(- (expt x 3) (expt y 3))) '(* (- x y) (+ (expt x 2) (expt y 2) (* x y))))
   (test (factorize '(- (expt (+ first 1) 3) (expt (- second 1) 3))) '(* (- (+ first 1) (- second 1) )
                                                                         (+ (expt (+ first 1) 2)
                                                                            (expt (- second 1) 2)
                                                                            (* (+ first 1) (- second 1)) )))
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 3) (expt y 3))))
                     3 2)
               (interaction-environment)) '19)
 
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 3) (expt y 3))))
                     2 3)
               (interaction-environment)) '-19)
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(- (expt x 3) (expt y 3))))
                     3 3)
               (interaction-environment)) '0)
   (test (factorize '(+ (expt x 3) (expt y 3))) '(* (+ x y) (- (+ (expt x 2) (expt y 2)) (* x y))))
   (test (factorize '(+ (expt (+ first 1) 3) (expt (- second 1) 3))) '(* (+ (+ first 1) (- second 1) )
                                                                         (- (+ (expt (+ first 1) 2)
                                                                               (expt (- second 1) 2))
                                                                            (* (+ first 1) (- second 1)) )))
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(+ (expt x 3) (expt y 3))))
                     3 2)
               (interaction-environment)) '35)
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(+ (expt x 3) (expt y 3))))
                     2 3)
               (interaction-environment)) '35)
   (test (eval (list (list 'lambda 
                           '(x y) 
                           (factorize '(+ (expt x 3) (expt y 3))))
                     0 0)
               (interaction-environment)) '0)
   ))



(run-tests the-tests-factorize)