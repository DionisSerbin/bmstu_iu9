(define call/cc call-with-current-continuation)
(define bool #f)
(define (use-assertations) (call/cc (lambda (x) (set! bool x))))
(define-syntax assert
  (syntax-rules ()
    ((_ quest)
    (if (not quest)
        (begin
          (display "Failed: ")
          (display 'quest)
          (newline)
          (bool))))))

(use-assertations)
(define (1/x x)
  (assert (not (zero? x))) ; Утверждение: x ДОЛЖЕН БЫТЬ > 0
  (/ 1 x))

(map 1/x '(1 2 3 4 5)) ; ВЕРНЕТ список значений в программу

(map 1/x '(-2 -1 0 1 2))