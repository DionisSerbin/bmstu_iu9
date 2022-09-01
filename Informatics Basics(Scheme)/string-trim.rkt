(define (trim? xs)
  (cond
    ( (null? xs) '() )
    ((or (equal? (car xs) '#\tab) (equal? (car xs) '#\newline) (equal? (car xs) '#\space) ) (trim? (cdr xs) ) )
    (#t xs) ) )
(define (string-trim-left xs)
  (list->string (trim? (string->list xs) ) ) )

(define (string-trim-right xs)
       (list->string (reverse (trim? (reverse (string->list xs) ) ) ) ) )
(define (string-trim xs)
    (string-trim-right (string-trim-left xs) ) )
(string-trim-left "\t\tabc def")
(string-trim-right "abc def\t")
(string-trim "\t\tabc def\n")

