class Vector(var x: Float, var y: Float, var z: Float){

  def ** (vect: Vector) = new Vector(y * vect.z - (z * vect.y), z * vect.x - (x * vect.z), x * vect.y - (y * vect.x))

  def * (n: Int) = new Vector(x * n, y * n, z * n)

  def * (vect: Vector): Float = x * vect.x + y * vect.y + z * vect.z

  def - (vect: Vector) = new Vector(x - vect.x, y - vect.y, z - vect.z)

  def + (vect: Vector) = new Vector(x + vect.x, y + vect.y, z + vect.z)

  override def toString: String = s"x = $x; y = $y; z = $z"
}

class NumbMultVector(val x: Int){
  def * (vect: Vector): Vector = vect * x
}

object main extends App {

  val a = new Vector(10, 10, 5)
  val b = new Vector(8, 9, 3)
  val i = 4

  implicit def intToNumbMult(i: Int) = new NumbMultVector(i)
  
  println(a + b)
  println(a - b)
  println(a * 4)
  println(a * b)
  println(a ** b)
  println(i * a)
}
