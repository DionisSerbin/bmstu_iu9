(define (subref anthg key ans)
  (if (vector? anthg)
      (subref (vector->list anthg) key ans)
      (if (string? anthg) 
          (subref (string->list anthg) key ans)
          (if (= key 0)
              (reverse ans)
              (subref (cdr anthg) (- key 1) (cons (car anthg) ans))))))

 


(define (ref2 smthg key)
  (cond
    ((vector? smthg) (and (not (< (- (vector-length smthg) 1) key)) (vector-ref smthg key)))
    ((list? smthg) (and (not(< (- (length smthg) 1) key)) (vector-ref (list->vector smthg) key)))
    ((string? smthg) (and (not(< (- (string-length smthg) 1) key)) (vector-ref (list->vector (string->list smthg)) key)))  ))

(define (refsum smthg key )
  (if (null? (subref smthg key '()))
      smthg
      (refsum (cdr smthg) (- key 1))))


(define (ref smthg key . dop)
  (if (null? dop)
      (ref2 smthg key)
      (if (vector? smthg)
          (and (not (< (vector-length smthg) key))
               (if (= (vector-length smthg) key)
                   (list->vector (append (subref (vector->list smthg) key '()) dop))   
                   (list->vector (append (subref (vector->list smthg) key '()) (append dop (refsum (vector->list smthg) key) )))))
          (if (string? smthg)
              (and (not (< (string-length smthg) key))
                   (if (= (string-length smthg) key)
                       (list->string (append (subref (string->list smthg) key '()) dop))   
                       (and (char? (car dop)) 
                            (list->string (append (subref (string->list smthg) key '()) (append dop (refsum (string->list smthg) key) ) ) )) ))
              (if (list? smthg)
                  (and (not (< (length smthg) key))
                       (if (= (length smthg) key)
                           (append (subref (list smthg) key '()) dop) 
                           (append (subref  smthg key '()) (append dop (refsum smthg key ) )))))))))

(ref '(1 2 3) 1) 
(ref #(1 2 3) 1) 
(ref "123" 1)    
(ref "123" 3)    



(ref '(1 2 3) 1 0)   
(ref #(1 2 3) 1 0)  
(ref #(1 2 3) 1 #\0)
(ref "123" 1 #\0)    
(ref "123" 1 0)
(ref "123" 3 #\4)
(ref "123" 5 #\4)






