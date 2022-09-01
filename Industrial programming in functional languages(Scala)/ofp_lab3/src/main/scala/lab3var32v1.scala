class EquationSystem[T](a: ((T, T), (T, T)), b: (T, T)) {
  val ((a11, a12), (a21, a22)) = a
  val (b1, b2) = b

  def solve[S]()(implicit ops: EquationSystemOps[T, S]): Option[(S, S)] = {
    val d = ops.sub(ops.mul(a11, a22), ops.mul(a21, a12))
    if (ops.is_zero(d)) {
      None
    } else {
      val d1 = ops.sub(ops.mul(b1, a22), ops.mul(a12, b2))
      val d2 = ops.sub(ops.mul(a11, b2), ops.mul(b1, a21))
      Some((ops.div(d1, d), ops.div(d2, d)))
    }
  }
}

abstract class EquationSystemOps[T, S] {
  def mul(a: T, b: T): T
  def sub(a: T, b: T): T
  def div(a: T, b: T): S
  def is_zero(a: T): Boolean
}

class Ratio(val num: Int, val denom: Int) {
  override def toString: String =
    num.toString + "/" + denom.toString
}

object EquationSystemOps {
  implicit object float extends EquationSystemOps[Float, Float] {
    override def mul(a: Float, b: Float): Float = a * b
    override def sub(a: Float, b: Float): Float = a - b
    override def div(a: Float, b: Float): Float = a / b
    override def is_zero(a: Float): Boolean = a == 0
  }

  implicit object int extends EquationSystemOps[Int, Ratio] {
    override def mul(a: Int, b: Int): Int = a * b
    override def sub(a: Int, b: Int): Int = a - b
    override def div(a: Int, b: Int): Ratio = new Ratio(a, b)
    override def is_zero(a: Int): Boolean = a == 0
  }
}


//object Main extends App {
//  val s1 = new EquationSystem(((2, 1), (3, 2)), (5, 3))
//  val s2 = new EquationSystem(((3, 4), (2, 5)), (18, 19))
//  val s3 = new EquationSystem[Float](((2, 1), (3, 2)), (5, 3))
//  val s4 = new EquationSystem(((2, 1), (4, 2)), (5, 3))
//  println(s1.solve())
//  println(s2.solve())
//  println(s3.solve())
//  println(s4.solve())
//}
