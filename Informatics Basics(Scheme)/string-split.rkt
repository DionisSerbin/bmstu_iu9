(define (seperate lstr lsep lacc lres)
  (cond ((null? lstr) (append lres (list (list->string lacc))))
        ( (member (car lstr) lsep)
          (if (null? lacc) 
              (seperate (cdr lstr) lsep '() lres)
              (seperate (cdr lstr) lsep '() (append lres  (list (list->string lacc))))))
        (#t (seperate (cdr lstr) lsep (append lacc (list (car lstr))) lres))))
       
(define (string-split str sep)
  (let ((lstr (string->list str))
        (lsep  (string->list sep)))
    (seperate lstr lsep '() '())))
(string-split "x;y;z" ";")
(string-split "x-->y-->z" "-->")

(define (drop list n)
  (if (= n 0)
      list
      (drop (cdr list) (- n 1))))
(define (dek list list2)
  (define (subdek dig list ans)
    (if (null? list)
        (reverse ans)
        (subdek dig (cdr list) (cons (cons dig (car list)) ans))))
  (define (dek2 list list2 ans)
    (if (null? list)
        (reverse ans)
        (dek2 (cdr list) list2 (cons (subdek (car list) list2 '()) ans))))
  (dek2 list list2 '()))
(define (mapp op listt)
  (define (mp op xs listt)
    (if (null? listt)
        (reverse xs)
        (mp op (cons (op (car listt)) xs) (cdr listt))))
  (mp op '() listt))
(define (my-map function list1 . more-lists)
  (define (some? function list)
    ;; returns #f if (function x) returns #t for 
    ;; some x in the list
    (and (pair? list)
         (or (function (car list))
             (some? function (cdr list)))))
  (define (map1 function list)
    ;; non-variadic map.  Returns a list whose elements are
    ;; the result of calling function with corresponding
    ;; elements of list
    (if (null? list)
        '()
        (cons (function (car list))
              (map1 function (cdr list)))))
  ;; Variadic map implementation terminates
  ;; when any of the argument lists is empty
  (let ((lists (cons list1 more-lists)))
    (if (some? null? lists)
        '()
        (cons (apply function (map1 car lists))
              (apply my-map function (map1 cdr lists))))))

