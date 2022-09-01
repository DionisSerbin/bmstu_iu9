;many-fracs =empty | space frac {many-fracs}
;frac = sign num '/' num
;sign = + | -
;num = num-char{num_char}
;num-char= 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0
;space = empty | space-char{space};
;space-char= #\space | #\tab |#\newline

(define (checking xs bigarg)
  (call/cc
   (lambda (exit)
     (and exit (letrec ((val (lambda () (car xs)))
                        (next (lambda () (set! xs (cdr xs))))
                        (left? (lambda () (not (null? xs))))
                        (token! (lambda (arg) (and (if (not (and (left?) (equal? (val) arg))) (exit #f)) (next) arg)))
                        (token? (lambda (arg) (and (left?) (equal? (val) arg) (next) arg)))
                        (inst (lambda (arg) (or ((cdr (assoc arg dict))) (exit #f))))
                        (dict (list (cons 'fracs
                                          (lambda () (inst 'setspace) (if (left?)
                                                                          (cons (inst 'frac) (inst 'fracs)) '())))
                                    (cons 'setspace
                                          (lambda ()
                                            (define a (and (left?) (member (val) '(#\tab #\space #\newline))))
                                            (if a
                                                (and (next) (inst 'setspace)))))
                                    (cons 'frac 
                                          (lambda ()
                                            (define a (inst 'int))
                                            (token! #\/)
                                            (define b (inst 'uint)) (/ a b)))
                                    (cons 'int 
                                          (lambda ()
                                            (cond ((token? #\-) (- (inst 'uint)))
                                                  ((token? #\+) (+ (inst 'uint)))
                                                  (else (inst 'uint)))))
                                    (cons 'uint 
                                          (lambda ()
                                            (define (ch->int a) (- (char->integer a) (char->integer #\0)))
                                            (define (ans xs val)
                                              (if (null? xs)
                                                  val
                                                  (ans (cdr xs) (+ (* 10 val) (ch->int (car xs))))))
                                            (ans (cons (inst 'num) (inst 'setnum)) 0)))
                                    (cons 'setnum 
                                          (lambda ()
                                            (define a (and (left?)
                                                           (member(val) '(#\0 #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9))))
                                            (if a
                                                (and (next) (cons (car a) (inst 'setnum)))
                                                '())))
                                    (cons 'num
                                          (lambda () (or (token? #\0) (token? #\1) (token? #\2) (token? #\3) (token? #\4) (token? #\5)
                                                         (token? #\6) (token? #\7) (token? #\8) (token? #\9)))))))
                 (let ((ans (inst bigarg)))
                   (and (not (left?)) ans)))))))


(define call/cc call-with-current-continuation)
(define (check-frac str)
  (not (not (checking (string->list str) 'frac))))

(define (check-many-fracs str)
  (not (not (checking (string->list str) 'fracs))))

(define (scan-frac str)
  (checking (string->list str) 'frac))

(define (scan-many-fracs str)
  (checking (string->list str) 'fracs))




(check-frac "110/111")
(check-frac "-4/3")  
(check-frac "+5/10")  
(check-frac "5.0/10") 
(check-frac "FF/10")

(scan-frac "110/111")
(scan-frac "-4/3")  
(scan-frac "+5/10")  
(scan-frac "5.0/10") 
(scan-frac "FF/10") 

(scan-many-fracs "\t1/2 1/3\n\n10/8 ")
(scan-many-fracs "\t1/2 1/3\n\n2/-5")