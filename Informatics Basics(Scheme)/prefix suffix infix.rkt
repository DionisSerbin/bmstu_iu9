(define (prefix xs ys) 
(or (null? xs) (and (equal? (car xs) (car ys)) (prefix (cdr xs) (cdr ys)))))
         
(define (suffix a b) (prefix (reverse a) (reverse b)))
 
(define (infix a b)
    (cond ((null? b) #f)
          ((prefix a b) #t)
          (#t (infix a (cdr b)))))
         
(define (string-prefix? sa sb) (prefix (string->list sa) (string->list sb)))
 
(define (string-suffix? sa sb) (suffix (string->list sa) (string->list sb)))
 
(define (string-infix? sa sb) (infix (string->list sa) (string->list sb)))
  
(string-prefix? "abc" "abcdef")
(string-prefix? "bcd" "abcdef")

(string-suffix? "def" "abcdef")
(string-suffix? "bcd" "abcdef")

(string-infix? "def" "abcdefgh")
(string-infix? "abc" "abcdefgh")
(string-infix? "fgh" "abcdefgh")
(string-infix? "igk" "abcdefgh")
