(define call/cc call-with-current-continuation)

(define (parse xs)
  (define place 0)
  (define (articles xs)
    (if (< place (vector-length xs))
        (if (equal? (vector-ref xs place) 'define)
            (cons (article xs) (articles xs))
            '())
        '(#f)))
  (define (article xs)
    (begin
      (set! place (+ place 2))
      (if (>= place (vector-length xs))
          #f
          (let ((x (body xs)))
            (if (>= place (vector-length xs))
                #f
                (if (equal? (vector-ref xs place) 'end)
                    (begin
                      (set! place (+ place 1))
                      (list (vector-ref xs (- place 1)) x))))))))
  (define (body xs)
    (cond
      ((or (>= place (vector-length xs)) (equal? (vector-ref xs place) 'end))  '())
      ((equal? (vector-ref xs place) 'define) '(#f))
      ((equal? (vector-ref xs place) 'if) (begin
                                         (set! place (+ place 1))
                                         (call/cc
                                          (lambda (memo)
                                            (let loop ((cif 1) (plc place))
                                              (cond
                                                ((>= plc (vector-length xs)) (memo '(#f)))
                                                ((equal? (vector-ref xs plc) 'if) (loop (+ cif 1) (+ plc 1)))
                                                ((not (equal? (vector-ref xs plc) 'endif)) (loop cif (+ plc 1)))
                                                
                                                ((> cif 1) (loop (- cif 1) (+ plc 1)))
                                                ((= cif 1) (memo (cons (list 'if (body xs)) (body xs))))
                                                ((< cif 1) (memo '(#f)))))))))
      ((equal? (vector-ref xs place) 'endif) (begin (set! place (+ place 1)) '()))
      ((or (number? (vector-ref xs place))
           (symbol? (vector-ref xs place)))
       (begin
         (set! place (+ place 1))
         (cons (vector-ref xs (- place 1)) (body xs))))))
  (let ((result (list (articles xs) (body xs))))
    (if (not (or (member #f result) (member #f (car result)) (member #f (car (cdr result)))))
        result
        #f)))
(parse #( define -- 1 - end
           define =0? dup 0 = end
           define =1? dup 1 = end
           define factorial
           =0? if drop 1 exit endif
           =1? if drop 1 exit endif
           dup --
           factorial
           *
           end
           0 factorial
           1 factorial
           2 factorial
           3 factorial
           4 factorial ))

(parse #(2 3 define word w1 w2 w3)) ;#f Определение в body
(parse #(define word w1 w2 w3)) ;#f Нет end
(parse #(define word w1 w2 w3 end define)) ;#f Нет end и имени
(parse #(define 12 end  if mod ahah)) ; Нет endif
(parse #(define =0? dup 0 = end ;#f
          define =1? dup 1 = end
          - 3 0 if + 9 0))
(parse #(1 if 8 9 if dup swap 4 endif swap endif 8)) ;#t
(parse #(1 2 +)); ⇒ (() (1 2 +))

(parse #(x dup 0 swap if drop -1 endif))