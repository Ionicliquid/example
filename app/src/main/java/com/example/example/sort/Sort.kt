package com.example.example.sort


class Sort {

    companion object {
        // 冒泡排序
        fun bubbleSort(array: Array<Int>): Array<Int> {

            if (array.isEmpty()) {
                return array
            }

            for (i in array.indices) {
                for (j in 0 until array.size - 1 - i) {
                    if (array[j + 1] < array[j]) {
                        val temp = array[j + 1]
                        array[j + 1] = array[j]
                        array[j] = temp
                    }
                }
            }
            return array
        }

        // 选择排序
        fun selectionSort(array: Array<Int>): Array<Int> {
            if (array.isEmpty()) {
                return array
            }
            for (i in array.indices) {
                var index = i
                for (j in i until array.size) {
                    if (array[j] < array[index]) {
                        index = j
                    }
                }
                var temp = array[index]
                array[index] = array[i]
                array[i] = temp
            }
            return array

        }

        // 插入排序

        fun insertionSort(array: Array<Int>): Array<Int> {
            if (array.isEmpty()) {
                return array
            }
            var current: Int
            for (i in array.indices) {
                current = array[i]
                var preIndex = i - 1
                while (preIndex >= 0 && current < array[preIndex]) {
                    array[preIndex + 1] = array[preIndex]
                    preIndex--
                }
                array[preIndex + 1] = current
            }
            return array
        }

        // 希尔排序

        fun shellSort(array: Array<Int>): Array<Int> {
            var len = array.size
            var temp: Int
            var gap = len / 2
            while (gap > 0) {
                for (i in gap until len) {
                    temp = array[i]
                    var preIndex = i - gap
                    while (preIndex >= 0 && array[preIndex] > temp) {
                        array[preIndex + gap] = array[preIndex]
                        preIndex -= gap
                    }
                    array[preIndex + gap] = temp
                }

                gap /= 2
            }
            return array


        }

        // 归并排序
        fun mergeSort(array: Array<Int>): Array<Int> {
            if (array.size < 2) {
                return array
            }
            var mid = array.size / 2
            var left = mergeSort(array.copyOfRange(0, mid))
            var right = mergeSort(array.copyOfRange(mid, array.size))

            return merge(left, right)
        }

        private fun merge(left: Array<Int>, right: Array<Int>): Array<Int> {
            var result = Array(left.size + right.size) { i -> i }
            var i = 0
            var j = 0
            for (index in result.indices) {
                when {
                    i >= left.size -> result[index] = right[j++]
                    j >= right.size -> result[index] = left[i++]
                    left[i] > right[j] -> result[index] = right[j++]
                    else -> result[index] = left[i++]
                }
            }
            return result

        }

        // 快速排序
        // 堆排序
        private var arraySize: Int = 0

        fun headSort(array: Array<Int>): Array<Int> {
            arraySize = array.size
            if (arraySize < 1) {
                return array
            }
            for (i in arraySize / 2 - 1 downTo 0) {
                adjustHeap(array, i)
            }
//            10 ,9 ,7 ,8 ,6 ,3 ,5 ,0 ,2 ,1 ,
//            6, 5, 7, 0, 9, 3, 8, 10, 2, 1

            while (arraySize > 0) {
                swap(array, 0, arraySize - 1)
                arraySize--
                adjustHeap(array, 0)
            }
            return array


        }


        fun adjustHeap(array: Array<Int>, i: Int) {
            var maxIndex = i
            if (i * 2 < arraySize && array[i * 2] > array[maxIndex]) {
                maxIndex = i * 2
            }
            if (i * 2 + 1 < arraySize && array[i * 2 + 1] > array[maxIndex]) {
                maxIndex = i * 2 + 1
            }

            if (maxIndex != i) {
                swap(array, maxIndex, i)
                adjustHeap(array, maxIndex)
            }
        }

        fun swap(array: Array<Int>, i: Int, j: Int) {
            var temp = array[i]
            array[i] = array[j]
            array[j] = temp
        }


        // 计数排序
        fun coutingSort(array: Array<Int>): Array<Int> {
            if (array.isEmpty()) {
                return array
            }
            var bias: Int
            var min = array[0]
            var max = array[0]
            for (i in array.indices) {
                if (array[i] > max) {
                    max = array[i]
                }
                if (array[i] < min) {
                    min = array[i]
                }
            }
            bias = 0 - min
            var bucket = Array(max - min + 1, { i -> i })
            bucket.fill(0)
            for (i in array.indices) {
                bucket[array[i] + bias]++
            }
            var index = 0
            var i = 0
            while (index < array.size) {
                if (bucket[i] != 0) {
                    array[index] = i - bias
                    bucket[i]--
                    index++
                } else {
                    i++
                }

            }
            return array

        }

        // 桶排序
        // 基数排序

    }


}

fun main(arg: Array<String>) {
    var array = arrayOf(6, 5, 7, 0, 9, 3, 8, 10, 2, 1)
//    3, 5, 7, 0, 1, 6, 8, 10, 2, 9
//    1, 0, 2, 5, 3, 6, 7, 9, 8, 10
//    0, 1, 2, 3, 5, 6, 7, 8, 9, 10,

    for (item in Sort.coutingSort(array)) {
        System.out.print("$item ,")
    }

}
