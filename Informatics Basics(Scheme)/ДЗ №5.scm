(load "smart.scm")

(define (stack-uno fun) (lambda (xs) (cons (fun (car xs)) (cdr xs))))

(define (stack-bin fun) (lambda (xs) (cons (fun (cadr xs) (car xs)) (cddr xs))))

(define built-in_operations
  (list (cons '+ (stack-bin +))
        (cons '- (stack-bin -))
        (cons '* (stack-bin *))
        (cons '/ (stack-bin div))
        (cons 'mod (stack-bin mod))
        (cons 'neg (stack-uno -))
        (cons '= (stack-bin (lambda (x y) (boolean->integer (= x y)))))
        (cons '> (stack-bin (lambda (x y) (boolean->integer (> x y)))))
        (cons '< (stack-bin (lambda (x y) (boolean->integer (< x y)))))
        (cons 'not (stack-uno (lambda (x) (boolean->integer (not (integer->boolean x))))))
        (cons 'and (stack-bin (lambda (y x) (boolean->integer (and (integer->boolean x) (integer->boolean y))))))
        (cons 'or (stack-bin (lambda (y x) (boolean->integer (or (integer->boolean x) (integer->boolean y))))))
        (cons 'drop cdr)
        (cons 'swap (lambda (xs) (my-fold-right cons (list (cadr xs) (car xs) (cddr xs)))))
        (cons 'dup (lambda (xs) (cons (car xs) xs)))
        (cons 'over (lambda (xs) (cons (cadr xs) xs)))
        (cons 'rot (lambda (xs) (my-fold-right cons (list (caddr xs) (cadr xs) (car xs) (cdddr xs)))))
        (cons 'depth (lambda (xs) (cons (length xs) xs)))
        ))

(define (main xs wc ws rs as)
  (if
   (>= wc (vector-length ws))
   xs
   (let ((n (vector-ref ws wc)))
     ;(all-display n " => " xs " "  ln)
     (cond
       ((number? n)
        (main (cons n xs) (++ wc) ws rs as))
       ((equal? 'define n)
        (let
            ((word (vector-ref ws (+ wc 1)))
             (ind (+ wc 2)))
          (while (not (equal? 'end (vector-ref ws wc))) (set! wc (++ wc)))
          (main xs (++ wc) ws rs (cons (cons word ind) as))))
       ((or (equal? 'end n) (equal? 'exit n))
        (main xs (car rs) ws (cdr rs) as))
       ((equal? 'while n)
        (begin
          (when (not (integer->boolean (car xs)))
            (let ((p 1))
              (while (> p 0)
                     (set! wc (++ wc))
                     (when (equal? 'while (vector-ref ws wc)) (set! p (++ p)))
                     (when (equal? 'endwhile (vector-ref ws wc)) (set! p (-- p))))))
          (main (cdr xs) (++ wc) ws rs as)))
       ((equal? 'endwhile n)
        (begin
          (let ((p -1))
            (while (< p 0)
                   (set! wc (-- wc))
                   (when (equal? 'while (vector-ref ws wc)) (set! p (++ p)))
                   (when (equal? 'endwhile (vector-ref ws wc)) (set! p (-- p)))))
          (main xs wc ws rs as)))
       ((equal? 'if n)
        (begin
          (when (not (integer->boolean (car xs)))
            (let ((p 1))
              (while (> p 0)
                     (set! wc (++ wc))
                     (when (equal? 'if (vector-ref ws wc)) (set! p (++ p)))
                     (when (equal? 'endif (vector-ref ws wc)) (set! p (-- p)))
                     (when (and (= p 1) (equal? 'else (vector-ref ws wc))) (set! p (-- p))))))
          (main (cdr xs) (++ wc) ws rs as)))
       ((equal? 'else n)
        (begin
          (let ((p 1))
            (while (> p 0)
                   (set! wc (++ wc))
                   (when (equal? 'if (vector-ref ws wc)) (set! p (++ p)))
                   (when (equal? 'endif (vector-ref ws wc)) (set! p (-- p)))))
          (main xs (++ wc) ws rs as)))
       ((equal? 'endif n)
        (main xs (++ wc) ws rs as))
       ((assoc n built-in_operations)
        (main ((cdr (assoc n built-in_operations)) xs) (++ wc) ws rs as))
       ((assoc n as)
        (main xs (cdr (assoc n as)) ws (cons (++ wc) rs) as))
       ))))

(define (interpret v xs)
  (main xs 0 v (list (vector-length v)) '()))

;==============================================================

(define p1 #(     define =? over = end
                   define >? over < end
                   define <? over > end
                   define range
                   0 =? if exit endif
                   0 >? if dup 1 - range
                   else dup 1 + range endif
                   end
                   range))

(interpret p1 '(10))
(interpret p1 '(-10))

(define p2 #(     define bit-number
                   dup 1 > while
                   dup 2 mod swap 2 /
                   dup 1 > endwhile end
                   bit-number))

(interpret p2 '(15))
(interpret p2 '(8))
(interpret p2 '(1024))

(define p3 #(     define >? over < end
                   0 swap 1 >? while
                   2 / swap 1 +
                   swap 1 >? endwhile
                   drop))

(interpret p3 '(1))
(interpret p3 '(2))
(interpret p3 '(1023))
(interpret p3 '(1024))

(define p4 #(     dup 0 > if
                      dup 2 +
                      dup 10 > if
                      dup 4 + else
                      dup 4 - endif else
                      dup 2 + endif))

(interpret p4 '(5))
(interpret p4 '(-5))
(interpret p4 '(-1))

(define p5 #(     define f
                   dup 1 + g
                   end
                   define g
                   dup 0 < if
                   exit
                   else
                   dup 5 - f
                   end
                   f))

(interpret p5 '(20))