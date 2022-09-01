(define memoized-factorial
  (let ((memo '()))
    (lambda (n)
      (let((p (assq n memo)))
        (if p
            (cadr p)
            (if (zero? n)
                1
                (let ((fact (* n (memoized-factorial (- n 1 )))))
                  (set! memo (cons (list n fact) memo))
                  fact)))))))


(begin
  (display (memoized-factorial 10)) (newline)
  (display (memoized-factorial 50)) (newline))