
object Hello extends App {

  val partition : (List[Int], Int) => (List[Int], List[Int]) = {
    def union: ((List[Int], List[Int]), (List[Int], List[Int])) => (List[Int], List[Int]) = {
      case ((l1, l2), (l3, l4)) => (l1 ::: l3, l2 ::: l4)
    }
    {
      case (Nil, n) => (Nil, Nil)
      case (x :: xs, n) if x < n => union((List(x), Nil), partition(xs, n))
      case (x :: xs, n) if x >= n => union((Nil, List(x)), partition(xs, n))
    }
  }

  println(partition(List(4, 8, 9, 10), 6))
}
