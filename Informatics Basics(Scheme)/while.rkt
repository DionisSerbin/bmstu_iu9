(define-syntax when
  (syntax-rules ()
    ((_ condi expr ...)
     (if condi
         (begin expr ...)))))

(define-syntax while
  (syntax-rules ()
    ((while cond? body ...)
     (let smth ()
       (if cond?
           (begin
             body ...
             (smth)))))))




(define-syntax for
  (syntax-rules (in as)
    ((_ i in lst body ...)
     (let smth ((lst2 (cdr lst)) (i (car lst)))
       (if (null? lst2)
           (begin
             body ...)
           (begin
             body ...
             (smth (cdr lst2) (car lst2))))))
    ((_ lst as i body ...)
     (for i in lst body ...))))

       
 

    
(let ((p 0)
      (q 0))
  (while (< p 3)
         (set! q 0)
         (while (< q 3)
                (display (list p q))
                (newline)
                (set! q (+ q 1)))
         (set! p (+ p 1))))

(for i in '(1 2 3)
  (for j in '(4 5 6)
    (display (list i j))
    (newline)))

(for '(1 2 3) as i
  (for '(4 5 6) as j
    (display (list i j))
    (newline)))