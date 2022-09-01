(define-syntax define-struct
  (syntax-rules ()
    ((_ name (type ...))
     (let ((sn (symbol->string 'name)))
       (eval `(define (,(string->symbol (string-append "make-" sn)) type ...) (list ,sn type ...)) (interaction-environment))
       (eval `(define (,(string->symbol (string-append sn "?")) x) (and (list? x) (= (length x) ,(+ (length '(type ...)) 1)) (equal? (car x) ,sn))) (interaction-environment))
       (let ((ind 0))
         (for itype in '(type ...)
           (set! ind (+ ind 1))
           (eval `(define (,(string->symbol (string-append sn "-" (symbol->string itype))) xs) (list-ref xs ,ind)) (interaction-environment))
           (eval `(define-syntax ,(string->symbol (string-append "set-" sn "-" (symbol->string itype) "!"))
                         (syntax-rules ()
                           ((_ xs a)
                            (set! xs (set-list-ref xs ,ind a))))) (interaction-environment))))))))

(define-syntax for
  (syntax-rules (in as)
    ((_ x in xs exprs ...)
     (let loop((lxs (cdr xs)) (x (car xs))) 
       (if (null? lxs)
           (begin
             exprs ...)
           (begin
             exprs ...
             (loop (cdr lxs) (car lxs))))))
    ((_ xs as x exprs ...)
     (for x in xs exprs ...))))

(define (set-list-ref xs k val)
  (if (= k 0)
      (cons val (cdr xs))
      (cons (car xs) (set-list-ref (cdr xs) (- k 1) val))))



(define-struct pos (row col))
(define p (make-pos 1 2))    

(pos? p)   

(pos-row p)
(pos-col p)

(set-pos-row! p 3) 
(set-pos-col! p 4) 

(pos-row p)
(pos-col p)

(define f (make-pos 5 6))

(pos-row f)
(pos-col f)

(set-pos-row! f 9)
(set-pos-col! f 10) 

(pos-row f)
(pos-col f)