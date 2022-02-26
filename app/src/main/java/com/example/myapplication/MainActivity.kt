package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_zero.setOnClickListener { setTextFields("0") }
        button_one.setOnClickListener { setTextFields("1") }
        button_two.setOnClickListener { setTextFields("2") }
        button_three.setOnClickListener { setTextFields("3") }
        button_four.setOnClickListener { setTextFields("4") }
        button_five.setOnClickListener { setTextFields("5") }
        button_six.setOnClickListener { setTextFields("6") }
        button_seven.setOnClickListener { setTextFields("7") }
        button_eight.setOnClickListener { setTextFields("8") }
        button_nine.setOnClickListener { setTextFields("9") }
        button_dot.setOnClickListener { setTextFields(".") }

        button_minus.setOnClickListener { setTextFields("-") }
        button_plus.setOnClickListener { setTextFields("+") }
        button_multiply.setOnClickListener { setTextFields("×") }
        button_division.setOnClickListener { setTextFields("÷") }
        button_procent.setOnClickListener { setTextFields("%") }
        button_plus_minus.setOnClickListener { setTextFields("±") }
        button_AllClean.setOnClickListener {
            input_id.text = ""
            output_id.text = ""
        }
        button_back.setOnClickListener {
            val length = input_id.length()
            if (length > 0)
                input_id.text = input_id.text.subSequence(0, length - 1)
        }
        button_equal.setOnClickListener {
            output_id.text = calculateResults()
            input_id.text =output_id.text
        }

    }

    fun setTextFields(str: String) {
        input_id.append(str)
    }

    fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in input_id.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())


        return list
    }

    fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    '×' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    '%' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('×') || list.contains('÷') || list.contains('%')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
                if (operator == '±')
                    result = result+nextDigit*(-1)

            }
        }

        return result
    }


    fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

}
