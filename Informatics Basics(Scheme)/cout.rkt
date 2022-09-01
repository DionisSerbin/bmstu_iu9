(define-syntax cout
  (syntax-rules (endl <<)
    ((_) (begin))
    ((_ << endl exprs ...)
     (let smth ()
       (begin
         (newline)
         (cout exprs ...))))
    ((_ << ex exprs ...)
     (let smth ()
       (begin
         (display ex)
         (cout exprs ...))))))


(cout << "a = " << 1 << endl << "b = " << 2 << endl)