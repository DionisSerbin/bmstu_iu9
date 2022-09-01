(define-syntax myif
  (syntax-rules ()
    ((_ condi xT xF)
     (let ((t (delay (eval xT (interaction-environment))))  (f (delay (eval xF (interaction-environment)))))
       (or (and (eval condi (interaction-environment)) (force t)) (force f))))))


(myif #t 1 (/ 1 0)) 
(myif #f (/ 1 0) 1)