abstract class Num[T] {
  def add(a: T, b: T): T
  def sub(a: T, b: T): T
  def mul(a: T, b: T): T
  def div(a: T, b: T): Option[T]
  def sqrt(a: T): Option[T]
  def mulInt(a: Int, b: T): T
  def > (a: T, b: Int) : Boolean
}

object Num {
  implicit object FloatNum extends Num[Float] {
    def add(a: Float, b: Float): Float = a + b
    def sub(a: Float, b: Float): Float = a - b
    def mul(a: Float, b: Float): Float = a * b
    def div(a: Float, b: Float): Option[Float] = Some(a / b)
    def sqrt(a: Float): Option[Float] = a match {
      case a if a >= 0 => Some(Math.sqrt(a).toFloat)
      case _ => None
    }
    def mulInt(a: Int, b: Float): Float = a * b
    def > (a: Float, b: Int): Boolean = a > b
  }

  implicit object DoubleNum extends Num[Double] {
    def add(a: Double, b: Double): Double = a + b
    def sub(a: Double, b: Double): Double = a - b
    def mul(a: Double, b: Double): Double = a * b
    def div(a: Double, b: Double): Option[Double] = Some(a / b)
    def sqrt(a: Double): Option[Double] = a match {
      case a if a >= 0 => Some(Math.sqrt(a))
      case _ => None
    }
    def mulInt(a: Int, b: Double): Double = a * b
    def > (a: Double, b: Int): Boolean = a > b
  }

  implicit object IntNum extends Num[Int] {
    def add(a: Int, b: Int): Int = a + b
    def sub(a: Int, b: Int): Int = a - b
    def mul(a: Int, b: Int): Int = a * b
    def div(a: Int, b: Int): Option[Int] = {
      val divv: Double = a / b
      divv match {
        case divv if divv % 1 == 0 => Some(divv.toInt)
        case _ => None
      }
    }
    def sqrt(a: Int): Option[Int] = a match {
      case a if a >= 0 => {
        val sqr: Double = Math.sqrt(a)
        sqr match {
          case sqr if sqr % 1 == 0 => Some(sqr.toInt)
          case _ => None
        }
      }
      case _ => None
    }
    def mulInt(a: Int, b: Int): Int = a * b
    def > (a: Int, b: Int): Boolean = a > b
  }

  implicit object LongNum extends Num[Long] {
    def add(a: Long, b: Long): Long = a + b
    def sub(a: Long, b: Long): Long = a - b
    def mul(a: Long, b: Long): Long = a * b
    def div(a: Long, b: Long): Option[Long] = {
      val divv: Double = a / b
      divv match {
        case divv if divv % 1 == 0 => Some(divv.toLong)
        case _ => None
      }
    }
    def sqrt(a: Long): Option[Long] = a match {
      case a if a >= 0 => {
        val sqr: Double = Math.sqrt(a)
        sqr match {
          case sqr if sqr % 1 == 0 => Some(sqr.toLong)
          case _ => None
        }
      }
      case _ => None
    }
    def mulInt(a: Int, b: Long): Long = a * b
    def > (a: Long, b: Int): Boolean = a > b
  }

  implicit object ComplexNum extends Num[Complex[Double]] {
    def add (a: Complex[Double], b: Complex[Double]): Complex[Double] = new Complex(a.real + b.real, a.image + b.image)
    def sub (a: Complex[Double], b: Complex[Double]): Complex[Double] = new Complex(a.real - b.real, a.image - b.image)
    def mul (a: Complex[Double], b: Complex[Double]): Complex[Double] = new Complex(a.real*b.real - a.image*b.image, a.image*b.real + a.real*b.image)
    def div (a: Complex[Double], b: Complex[Double]): Option[Complex[Double]] = Some(
      new Complex((a.real*b.real + a.image*b.image)/(b.real*b.real+b.image*b.image),
        (a.image*b.real - a.real*b.image)/(b.real*b.real+b.image*b.image))
    )
    def sqrt(a: Complex[Double]): Option[Complex[Double]] = {
      if (a.real == 0 && a.image == 0) return Some(new Complex(0,0))
      val dx = Math.abs(a.real)
      val dy = Math.abs(a.image)
      val dr = if (dx >= dy) dy/dx else dx/dy
      val dw = if (dx >= dy) Math.sqrt(dx) * Math.sqrt(0.5 * (1.0 + Math.sqrt(1 + dr * dr)))
      else Math.sqrt(dy) * Math.sqrt(0.5 * (dr + Math.sqrt(1 + dr * dr)))
      if (a.real >= 0) {
        Some(new Complex(dw, a.image / (2 * dw)))
      } else {
        val im = if (a.image > 0) dw else -dw
        Some(new Complex(a.image / 2 * im, im))
      }
    }
    def mulInt(a: Int, b: Complex[Double]): Complex[Double] = new Complex(a*b.real, a*b.image)
    def > (a: Complex[Double], b: Int): Boolean = a.real > b
  }
}

class Complex[T](r: T, i: T)(implicit num: Num[T]) {
  val real: T = r
  val image: T = i
  override def toString: String = {
    if (num. > (image, 0)) real + "+" + image + "i"
    else real + "" + image + "i"
  }
}

class QuadraticEquation[T] (val a: T, val b: T, val c: T) {
  def solve(implicit num : Num[T]): List[T] = {
    val d = num.sub(num.mul(b, b), num.mul(num.mulInt(4,a),c))
    val sqr = num.sqrt(d)
    sqr match {
      case None => List()
      case Some(x) => {
        val x1 = num.div(num.add(num.mulInt(-1,b), x), num.mulInt(2,a))
        val x2 = num.div(num.sub(num.mulInt(-1,b), x), num.mulInt(2,a))
        (x1, x2) match {
          case (x1, x2) if x1.isEmpty || x2.isEmpty => List()
          case _ => List(x1.get, x2.get)
        }
      }
    }
  }
}

object Main {
  def main(args: Array[String]) : Unit = {
    val eqFloat = new QuadraticEquation[Float](4, 6, -3)
    val eqDouble = new QuadraticEquation[Double](4, 6, -3)
    val eqInt = new QuadraticEquation[Int](4, 6, -3)
    val eqInt2 = new QuadraticEquation[Int](1, -3, 2)
    val eqLong = new QuadraticEquation[Long](4, 6, -3)
    val eqComplex = new QuadraticEquation[Complex[Double]](new Complex(6,8), new Complex(-4,7), new Complex(6,6))
    println("Float: " + eqFloat.solve)
    println("Double: " + eqDouble.solve)
    println("Int: " + eqInt.solve)
    println("Int: " + eqInt2.solve)
    println("Long: " + eqLong.solve)
    println("Compl[Double]: " + eqComplex.solve)
  }
}