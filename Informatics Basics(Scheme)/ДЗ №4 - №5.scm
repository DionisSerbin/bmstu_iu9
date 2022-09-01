(define-syntax define-data
  (syntax-rules ()
    ((_ name ()) (begin))
    ((_ name ((type arg ...) ...))
     (let ((sn (string->symbol (string-append (symbol->string 'name) "?"))) (dict '()))
       (for xs in '((type arg ...) ...)
         (eval `(define ,xs (list ,(symbol->string (car xs)) ,@(cdr xs))) (interaction-environment))
         (set! dict (cons (list (symbol->string (car xs)) (length xs)) dict)))
       (eval `(define (,sn p)
                   (and
                    (list? p)
                    (not (null? p))
                    (not (not (member (list (car p) (length p)) ',dict))))) (interaction-environment))))))

(define-syntax match
  (syntax-rules ()
    ((_ x) #f)
    ((_ x ((type arg ...) fun) other ...)
     (if (and (list? x)
              (not (null? x))
              (equal? (symbol->string 'type) (car x))
              (= (length x) (length '(type arg ...))))
         (eval `((lambda ,'(arg ...) ,'fun) ,@(cdr x)) (interaction-environment))
         (match x other ...)))))

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


(define-data figure ((square a)
                     (rectangle a b)
                     (triangle a b c)
                     (circle r)))

; Определяем значения типа
;
(define s (square 10))
(define r (rectangle 10 20))
(define t (triangle 10 20 30))
(define c (circle 10))
;Пусть определение алгебраического типа вводит не только конструкторы,
;но и предикат этого типа:

(and (figure? s)
     (figure? r)
     (figure? t)
     (figure? c));⇒ #t

(define pi (acos -1)) ; Для окружности
  
(define (perim f)
  (match f 
    ((square a)       (* 4 a))
    ((rectangle a b)  (* 2 (+ a b)))
    ((triangle a b c) (+ a b c))
    ((circle r)       (* 2 pi r))))
  
(perim s);⇒ 40
(perim r);⇒ 60
(perim t);⇒ 60