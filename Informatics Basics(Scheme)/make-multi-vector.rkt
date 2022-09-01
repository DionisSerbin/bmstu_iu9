(define (vectleng sizes x)
    (if (null? sizes)
        x
        (vectleng (cdr sizes) (* x (car sizes) ) ) ) )

(define (make-multi-vector sizes . fill)

  (define (inpair vector)
    (list sizes vector) )
  (if (null? fill)
      (inpair (make-vector (vectleng sizes 1) ) )
      (inpair (make-vector (vectleng sizes 1) (car fill) ) ) ) )

(define (multi-vector? m)
  (= (vectleng (car m) 1) (length (vector->list (car(cdr m) ) ) ) ) )
  
(define (multi-vector-ref m indices)
  
  (define (vectlengmult xs sizes y)
    (cond ((null? xs) y)
          ((not (null? sizes)) (vectlengmult (cdr xs) (cdr sizes) (+ y (* (car xs) (vectleng sizes 1) ) ) ) )
          (#t (+ y (car xs) ) ) ))
  (vector-ref (car (cdr m) ) (vectlengmult indices (cdr (car m)) 0)))
(define (multi-vector-set! m indices x)
 (define (vectlengmult xs sizes ys)
    (cond ((null? xs) ys)
          ((not (null? sizes)) (vectlengmult (cdr xs) (cdr sizes) (+ ys (* (car xs) (vectleng sizes 1) ) ) ) )
          (#t (+ ys (car xs) ) ) ) )
  (vector-set! (car (cdr m) ) (vectlengmult indices (cdr (car m)) 0)x ))
           

(define a (make-multi-vector '(11 12 9 16)))
(multi-vector? a)
(multi-vector-set! a '(10 7 6 12) 'test)
(multi-vector-ref a '(10 7 6 12))
  