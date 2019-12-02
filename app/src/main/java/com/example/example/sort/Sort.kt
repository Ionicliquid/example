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

        fun quickSort(array: Array<Int>, start: Int, end: Int): Array<Int> {
            if (array.isEmpty() || start < 0 || end >= array.size || start > end) {
                return array
            }
            var smallIndex = partition(array, start, end)
            if (smallIndex > start) {
                quickSort(array, start, smallIndex - 1)

            }
            if (smallIndex < end) {
                quickSort(array, smallIndex + 1, end)
            }
            return array

        }

        private fun partition(array: Array<Int>, start: Int, end: Int): Int {
            var pivot: Int = ((start + Math.random() * (end - start + 1)).toInt())
            var smallIndex = start - 1
            swap(array, pivot, end)
            for (i in start..end) {
                if (array[i] <= array[end]) {
                    smallIndex++
                    if (i > smallIndex) {
                        swap(array, i, smallIndex)
                    }
                }

            }
            System.out.print("pivot:$pivot smallIndex:$smallIndex,\n")
            return smallIndex
        }

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


        private fun adjustHeap(array: Array<Int>, i: Int) {
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

        private fun swap(array: Array<Int>, i: Int, j: Int) {
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
        fun bucketSort(array: ArrayList<Int>, bucketSize: Int): ArrayList<Int> {
            if (array == null || array.size < 2) {
                return array
            }
            var tempBucketSize = bucketSize
            var max = array[0]
            var min = array[0]
            for (i in array.indices) {
                if ((array[i] > max)) {
                    max = array[i]
                }
                if (array[i] < min) {
                    min = array[i]
                }
            }
            var bucketCount = (max - min) / tempBucketSize + 1
            var bucketArray = ArrayList<ArrayList<Int>>(bucketCount)
            var resultArr = ArrayList<Int>()
            for (i in 0 until bucketCount) {
                bucketArray.add(ArrayList())
            }
            for (i in array.indices) {
                bucketArray[(array[i] - min) / tempBucketSize].add(array[i])
            }
            for (i in 0 until bucketCount) {
                if (tempBucketSize == 1) {
                    for (j in 0 until bucketArray[i].size) {
                        resultArr.add(bucketArray[i][j])
                    }
                } else {
                    if (bucketCount == 1) {
                        tempBucketSize--
                    }
                    var temp = bucketSort(bucketArray[i], tempBucketSize)
                    for (j in temp.indices) {
                        resultArr.add(temp[j])
                    }
                }
            }
            return resultArr

        }

        // 基数排序
        fun radixSort(array: Array<Int>): Array<Int> {
            if (array == null || array.size < 2) {
                return array
            }
            var max = array[0]
            for (i in array.indices) {
                max = Math.max(max, array[i])
            }
            var maxDigit = 0
            while (max != 0) {
                max /= 10
                maxDigit++
            }
            var mod = 10
            var div = 1
            var bucketList = ArrayList<ArrayList<Int>>()
            for (i in 0 until 10) {
                bucketList.add(ArrayList())
            }
            for (i in 0 until maxDigit) {
                if (i != 0) {
                    mod *= 10
                    div *= 10
                }
                for (j in array.indices) {
                    val num = (array[j] % mod) / div
                    bucketList[num].add(array[j])
                }
                var index = 0
                for (j in bucketList.indices) {
                    for (k in bucketList[j].indices) {
                        array[index++] = bucketList[j][k]
                    }
                    bucketList[j].clear()
                }
            }
            return array

        }

    }


}

fun main(arg: Array<String>) {
    var array = arrayOf(6, 5, 7, 0, 9, 3, 8, 10, 2, 1)
//    3, 5, 7, 0, 1, 6, 8, 10, 2, 9
//    1, 0, 2, 5, 3, 6, 7, 9, 8, 10
//    0, 1, 2, 3, 5, 6, 7, 8, 9, 10,

    var arrayList: ArrayList<Int> = ArrayList()
    arrayList.addAll(array.toList())
//    for (i in Sort.bucketSort(arrayList, 2)) {
//        System.out.print("$i, ")
//    }

    for (i in Sort.radixSort(array)) {
        System.out.print("$i, ")

    }

}
