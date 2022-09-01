(define (trib n)
  (cond ((not (> n 1)) 0)
        ((= n 2) 1)
        ((> n 2) (+ (trib (- n 1))
                    (trib (- n 2))
                    (trib (- n 3))))))


(trib 3)
(trib 5)
(trib 2)
(trib 0)
(trib 4)
(trib 10)

(define mem-trib
  (let ((memo '()))
    (lambda (n)
      (let ((p (assq n memo)))
        (if p
            (cadr p)
            (cond ((not (> n 1)) 0)
                  ((= n 2) 1)
                  ((> n 2) (let ((tribs (+ (mem-trib (- n 1))
                                           (mem-trib (- n 2))
                                           (mem-trib (- n 3)))))
                             (set! memo (cons (list n tribs) memo))
                             tribs))))))))

(mem-trib 3)
(mem-trib 5)
(mem-trib 2)
(mem-trib 0)
(mem-trib 4)
(mem-trib 10)
(define fact-memoized
  (let ((memo '()))
    (lambda(n)
      (let((p (assq n memo)))
        (if p
            (cadr p)
            (if (zero? n)
                1
                (let ((fact (* n (fact-memoized (- n 1 )))))
                  (set! memo (cons (list n fact) memo))
                  fact)))))))

