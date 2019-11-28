package com.example.example.sort


class Sort {

    companion object {
        // 冒泡排序
        fun bubbleSort(array: Array<Int>): Array<Int> {

            if (array.isEmpty()) {
                return array
            }

            for (i in array.indices) {
                for (j in 0..array.size - 1 - i) {
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
            for (i in 0 until array.size) {
                current = array[i + 1]
                var preIndex = i
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
            val len = array.size / 2
            var temp = len / 2
            var gap = temp
            while (gap > 0) {
                for (i in gap until len) {
                    temp = array[i]
                    var preIndex = i - gap
                    while (preIndex >= 0 && array[preIndex] > temp) {
                        preIndex -= gap
                    }
                    array[preIndex+gap] = temp
                }
                gap/=2
            }
            return  array

        }

    }

}
