(define-syntax test
  (syntax-rules ()
    ((_ expression expected-result)
     (list ( quote expression) expected-result))))

(define (run-test the-test)
  (let ((expression (car the-test)))
    (write expression)
    (let* ((result (eval expression
                         (interaction-environment)))
           (status (equal? (cadr the-test) result)))
      (if status
          (display "ok")
          (display "FAIL"))
      (newline)
      (if (not status)
          (begin
          (display "expected:")
          (write (cadr the-test))
          (newline)
          (display "returned:")
          (write result)
          (newline)))
         status)))

(define (and-fold x xs)
  (if (null? xs)
      x
      (and-fold (and x (car xs)) (cdr xs))))

(define (run-tests the-test)
  (and-fold #t (map run-test the-test)))
