(define-syntax when
  (syntax-rules ()
    ((_ condi expr ...)
     (if condi
         (begin expr ...)))))
(define-syntax unless
  (syntax-rules ()
    ((_ condi expr ...)
     (if (not condi) (begin expr ...)))))
(define x 1)

(when   (> x 0) (display "x > 0")  (newline))
(unless (= x 0) (display "x != 0") (newline))
     