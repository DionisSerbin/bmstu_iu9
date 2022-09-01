(define-syntax trace-ex
  (syntax-rules ()
          ((_  expr)
           (begin
             (let ((x expr))
                    (display 'expr)
                    (display "=>")
                    (write x)
                    (newline)
                    x)))))


(define (zip . xss)
  (if (or (null? xss)
          (null? (trace-ex (car xss))))
      '()
      (cons (map car xss)
            (apply zip (map cdr (trace-ex xss))))))

(define count
  (let ((u 0))
    (lambda ()
      (set! u (+ 1 u))
      u)))