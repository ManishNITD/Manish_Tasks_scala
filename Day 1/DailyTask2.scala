def get_algorithm(algo:String):(Array[Int],Int)=>Unit={
  algo match {
    case "bubblesort"=> bubbleSort
    case "insertionsort" => insertionSort
    case "quicksort" => quickSort
    case "mergesort" => mergeSort
    case "radixsort" => radixSort
    case "binarysearch" => binarySearch
    case _ => throw new IllegalArgumentException("Please enter correct algorithm")
  }
}
object main extends App{
  var algorithm: String = scala.io.StdIn.readLine("Please enter the algorithm type")
  val unsorted_Array: Array[Int]=scala.io.StdIn.readLine("Please enter the array elements").split(" ").map(_.toInt)
  if (algorithm == "binarysearch") {
    val target: Int = scala.io.StdIn.readLine("Please enter the target element").toInt
    get_algorithm(algorithm)(unsorted_Array, target)
  } else {
    get_algorithm(algorithm)(unsorted_Array, 0)
  }
}

// Bubble Sort
def bubbleSort(arr: Array[Int],dummy:Int): Unit = {
  val n = arr.length
  for (i <- 0 until n - 1) {
    for (j <- 0 until n - i - 1) {
      if (arr(j) > arr(j + 1)) {
        val temp = arr(j)
        arr(j) = arr(j + 1)
        arr(j + 1) = temp
      }
    }
  }
  arr.foreach(println)
}

// Insertion Sort
def insertionSort(arr: Array[Int],dummy:Int): Unit = {
  val n = arr.length
  for (i <- 1 until n) {
    val key = arr(i)
    var j = i - 1
    while (j >= 0 && arr(j) > key) {
      arr(j + 1) = arr(j)
      j -= 1
    }
    arr(j + 1) = key
  }
  arr.foreach(println)
}

// Quick Sort
def quickSort(arr: Array[Int],dummy:Int): Unit = {
  def sort(start: Int, end: Int): Unit = {
    if (start < end) {
      val pivot = arr(end)
      var i = start - 1
      for (j <- start until end) {
        if (arr(j) < pivot) {
          i += 1
          val temp = arr(i)
          arr(i) = arr(j)
          arr(j) = temp
        }
      }
      val temp = arr(i + 1)
      arr(i + 1) = arr(end)
      arr(end) = temp

      val p = i + 1
      sort(start, p - 1)
      sort(p + 1, end)
    }
  }

  sort(0, arr.length - 1)
  arr.foreach(println)
}

// Merge Sort
def mergeSort(arr: Array[Int],dummy:Int): Unit = {
  def merge(start: Int, mid: Int, end: Int): Unit = {
    val left = arr.slice(start, mid + 1)
    val right = arr.slice(mid + 1, end + 1)
    var i = 0
    var j = 0
    var k = start

    while (i < left.length && j < right.length) {
      if (left(i) <= right(j)) {
        arr(k) = left(i)
        i += 1
      } else {
        arr(k) = right(j)
        j += 1
      }
      k += 1
    }

    while (i < left.length) {
      arr(k) = left(i)
      i += 1
      k += 1
    }

    while (j < right.length) {
      arr(k) = right(j)
      j += 1
      k += 1
    }
  }

  def sort(start: Int, end: Int): Unit = {
    if (start < end) {
      val mid = (start + end) / 2
      sort(start, mid)
      sort(mid + 1, end)
      merge(start, mid, end)
    }
  }

  sort(0, arr.length - 1)
  arr.foreach(println)
}

// Radix Sort
def radixSort(arr: Array[Int],dummy:Int): Unit = {
  def countingSort(exp: Int): Unit = {
    val n = arr.length
    val output = Array.fill(n)(0)
    val count = Array.fill(10)(0)

    for (i <- 0 until n) {
      count((arr(i) / exp) % 10) += 1
    }

    for (i <- 1 until 10) {
      count(i) += count(i - 1)
    }

    for (i <- n - 1 to 0 by -1) {
      output(count((arr(i) / exp) % 10) - 1) = arr(i)
      count((arr(i) / exp) % 10) -= 1
    }

    for (i <- 0 until n) {
      arr(i) = output(i)
    }
  }

  val maxVal = arr.max
  var exp = 1
  while (maxVal / exp > 0) {
    countingSort(exp)
    exp *= 10
  }
  arr.foreach(println)
}

// Binary Search
def binarySearch(arr: Array[Int], target: Int): Unit = {
  def search(start: Int, end: Int): Int = {
    if (start <= end) {
      val mid = start + (end - start) / 2
      if (arr(mid) == target) mid
      else if (arr(mid) < target) search(mid + 1, end)
      else search(start, mid - 1)
    } else {
      -1
    }
  }

  val result = search(0, arr.length - 1)
  if (result != -1) {
    println(s"Element found at index $result")
  } else {
    println("Element not found in Array")
  }
}
