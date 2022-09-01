(define-syntax my-let
  (syntax-rules ()
    ((_ ((will was) ...) body ...)
     ((lambda (will ...) body ...) was ...))))

(define-syntax my-let*
  (syntax-rules ()
    ((_ ((will was)) body ...)
     ((lambda (will) body ...) was))
     ((_ ((will was) . next) body ...)
     ((lambda (will) (my-let* next body ...)) was))))
(my-let ((x 4) (y 5))
         (+ x y))
(my-let* ((x 4) (y (+ x 1)))
         (+ x y))