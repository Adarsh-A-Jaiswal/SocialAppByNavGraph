package com.example.socialappbynavgraph.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialappbynavgraph.R

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        rec(1)
        val t = arrayListOf(12, 13, 1, 10, 34, 1)
        main(2,t)
    }

    fun Int.oddOrEven(): String {
        return if (this % 2 == 0) "Even" else "Odd"
    }

    fun rec(number: Int) {
        val arr = ArrayList<Int>()
        val lis = mutableListOf(1, 2, 3, 4, 5)
        arr[0] = 33
        var newNum = number
        if (newNum < 100) {
            print(newNum)
            newNum++
            rec(newNum)
        }
    }

    fun main(small: Int, arr: ArrayList<Int>) {
        val n = arr.size
        arr.toSet().sorted()
        var smallest = Int.MAX_VALUE
        for (i in 0 until n) {
            if (arr[i] < smallest) {
                smallest = arr[i]
            }
        }
        println(
            "smallest element is: "
                    + smallest
        )
        var secondSmallest = small

        for (i in 0 until n) {
            if ((arr[i] < secondSmallest && arr[i] > smallest)) {
                secondSmallest = arr[i]
            }
        }
        println(
            ("second smallest element is: "
                    + secondSmallest)
        )
    }
}