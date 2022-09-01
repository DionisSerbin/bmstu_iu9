(define (save-data data name)
  (with-output-to-file ;Перенаправление вывода в файл  
      name (lambda () (write data))))
(define (load-data name)
  (define (subdata in)
    (let ((char (read-char in)))
      (if (eof-object? char)
          '()
          (cons char (subdata in)))))
  (string->symbol (list->string(subdata (open-input-file name)))))
(save-data "Hello World" "testing.rkt")
(load-data "testing.rkt")


