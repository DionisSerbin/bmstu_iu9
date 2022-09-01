(define (delete pred? xs)
  (define (delete2 pred? xs ys)
  (if (null? xs)
      (reverse ys)
      (if (pred? (car xs) )
          (delete2 pred? (cdr xs) ys)
          (delete2 pred? (cdr xs) (cons (car xs) ys) ) ) ) ) (delete2 pred? xs '() ) )
(delete odd? '(1 2 3 4 5 6 7) )














































(define (delet pred? list)
  (define (d2 pred? list ans)
    (if (null? list)
        (reverse ans)
        (if (pred? (car list))
            (d2 pred? (cdr list) (cons (car list) ans))
            (d2 pred? (cdr list) ans))))
  (d2 pred? list '()))