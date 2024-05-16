def hall_status(matrix: Array[Array[String]]): Unit = {
  for (i <- 0 until matrix.length) {
    for (j <- 0 until matrix(i).length) {
      print(matrix(i)(j) + "\t")
    }
    println()
  }
}

def book(seat: Int, matrix: Array[Array[String]]): Unit = {
  def seat_booking(matrix: Array[Array[String]], seat: Int): Unit = {
    val row = seat / 10
    val column = seat % 10 - 1
    if (matrix(row)(column) == "X") {
      println("Seat Already Occupied")
    } else {
      matrix(row)(column) = "X"
    }
  }
  seat_booking(matrix, seat)
  hall_status(matrix)
}

@main def booking(): Unit = {
  var matrix: Array[Array[String]] = Array.ofDim[String](10, 10)

  def initialize_matrix(matrix: Array[Array[String]]): Unit = {
    for (i <- 0 until matrix.length) {
      for (j <- 0 until matrix(i).length) {
        matrix(i)(j) = (10 * i + j + 1).toString()
      }
    }
  }

  initialize_matrix(matrix)

  var counter = "Y"
  while (counter == "Y") {
    println(
      "Hello " + System.getProperty(
        "user.name"
      ) + "\n" + "1. Do you want to book a seat\n" + "2. See the Hall Status"
    )
    var input = scala.io.StdIn.readLine().toInt
    if (input == 1) {
      var seat = scala.io.StdIn.readLine("Enter Seat Number: ").toInt
      book(seat, matrix)
    } else if (input == 2) {
      hall_status(matrix)
    } else {
      println("Invalid Input")
    }
    counter = scala.io.StdIn.readLine("Enter Your choice(Y/N): ")
  }
}
