;Expression ::= Spaces Tokens Spaces .
;Spaces ::= Space Spaces | .
;Space ::= #\space | #\newline | #\tab .
;Tokens ::= Spaces Token Spaces Tokens | .
;Token ::= Name | Number | Sign | Parenthes .
;Name ::= a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z .
;Number ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 .
;Sign ::= + | - | * | / | ^ .
;Parenthes ::= ( | ) .


(define escape #f)

(define symbols
  (list
   #\a #\b #\c #\d #\e #\f #\g #\h #\i #\j
   #\k #\l #\m #\n #\o #\p #\q #\r #\s #\t
   #\u #\v #\w #\x #\y #\z))

(define nums
  (list #\0 #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9))

(define parentheses (list #\( #\)))

(define signs (list #\+ #\- #\* #\/ #\^))

(define spaces (list #\space #\newline #\tab))
(define call/cc call-with-current-continuation)
(define (tokenize str)
  (and (call/cc
        (lambda (cc) (set! escape cc)))
       (letrec ((name (lambda (xs str)
                        (if (and (not (null? xs)) (member (car xs) symbols))
                            (name (cdr xs) (string-append str (make-string 1 (car xs))))
                            (cons (string->symbol str) xs))))
                (number (lambda (xs val)
                          (if (and (not (null? xs)) (member (car xs) nums))
                              (number (cdr xs) (+ (* val 10) (- (char->integer (car xs)) (char->integer #\0))))
                              (cons val xs))))
                (token (lambda (xs)
                         (cond
                           ((null? xs) '())
                           ((member (car xs) spaces) (token (cdr xs)))
                           ((member (car xs) parentheses) (cons (make-string 1 (car xs)) (token (cdr xs))))
                           ((member (car xs) signs) (cons (string->symbol (make-string 1 (car xs)))
                                                          (token (cdr xs))))
                           ((member (car xs) symbols) (let ((rez (name xs "")))
                                                      (cons (car rez) (token (cdr rez)))))
                           ((member (car xs) nums) (let ((rez (number xs 0)))
                                                     (cons (car rez) (token (cdr rez)))))
                           (else (escape #f))))))
         (and escape
              (token (string->list str))))))

(tokenize "1")
(tokenize "-a")
(tokenize "-a + b * x^2 + dy")
(tokenize "(a - 1)/(b + 1)")