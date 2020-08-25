package com.fair.lance.solver

import kotlin.math.min
import kotlin.math.pow

class LanceSolver {

    fun solver(problem: String): String {
        return problem
    }

    fun stringEquationSolver(entry: String) : String {

        var equation: String = entry
        var special = searchForSpecial(equation)
        var oGScope: String
        var scope: String
        var firstN: Int
        var secondN: Int
        var operator: String
        var solution = 0
        var problem: String
        var parenthesis = trackBrackets(entry)

        // auto solved an equation
        while (special.isNotEmpty()) {
            // scopes into the

            oGScope = sliceParenthesis(equation,parenthesis[0])
            scope = oGScope
            println("Scope: $scope")

            while(searchForSpecial(scope).isNotEmpty()){

                firstN = extractValue(sliceNumber(splitUserInput(scope), searchForSpecial(scope)).first).toInt()
                println("First Number: $firstN")
                secondN = extractValue(sliceNumber(splitUserInput(scope), searchForSpecial(scope)).second).toInt()
                println("Second Number: $secondN")
                operator = sliceNumber(splitUserInput(scope), searchForSpecial(scope)).third.second
                println("Operator: $operator")
                problem = sliceNumber(splitUserInput(scope), searchForSpecial(scope)).third.first
                println("Problem: $problem")
                solution = operationAttempt(firstN,secondN, operator)
                println("Solution: $solution")
                scope = replaceEquation(problem, solution.toString(), scope)
                println("Updated Scope: $scope")
                special = searchForSpecial(scope)
                println("New Scope Specials: $special")
            }

            equation = replaceEquation("($oGScope)", scope, equation)
            parenthesis = trackBrackets(equation)
            println("Updated Equation: $equation")
            special = searchForSpecial(equation)
            println("New Equation Specials: $special")
        }

        return solution.toString()

    }
    //removes all white space and returning a simple tight list.
    fun splitUserInput(userInput: String): MutableList<String> {

        val trim = userInput.replace("\\s".toRegex(), "")
        return trim.split("").toMutableList()
    }
    //finds the special operators within the list
    private fun searchForSpecial(equation: String) : MutableList<Int> {
        val specialList = arrayListOf("^","*","/","+","-")
        val specialParenthesis = arrayListOf('^','*','/','+','-')
        val confirmedSpecial = mutableListOf<Int>()

        val result = equation.toCharArray().intersect(specialParenthesis).map { x -> min(equation.count {it == x}, specialParenthesis.count {it == x}) }.sum()

        return if(result == 0) {
            confirmedSpecial
        } else {
            equation.forEachIndexed { index, c ->
                if (c.toString() in specialList) {
                    confirmedSpecial.add(index)
                }
            }
            confirmedSpecial
        }

    }
    //separates by parenthesis
    private fun sliceParenthesis(userEquation: String, special: Pair<Int, Int> ): String {

        val startSlice = special.first
        val endSlice = special.second

        return userEquation.slice(startSlice+1 until endSlice)
    }
    //pulls the first preceding and succeeding values of
    private fun sliceNumber(userEquation: MutableList<String>, special: MutableList<Int>): Triple<MutableList<String>, MutableList<String>, Pair<String, String>>{
        var firstNumber = mutableListOf<String>()
        var secondNumber = mutableListOf<String>()
        val problemScope : String
        return if (special.size > 1) {
            firstNumber = userEquation.slice(0 until special.first()).toMutableList()
            secondNumber = userEquation.slice(special[0] +1 until special[1]).toMutableList()
            problemScope = extractValue(userEquation.slice(0 until special[1]).toMutableList())
            Triple(firstNumber,secondNumber,Pair(problemScope,userEquation[special.first()]))

        } else {
            firstNumber = userEquation.slice(0 until special.first()).toMutableList()
            secondNumber = userEquation.slice(special.first() + 1 until userEquation.size).toMutableList()
            problemScope = extractValue(userEquation.slice(0 until userEquation.size).toMutableList())
            Triple(firstNumber,secondNumber,Pair(problemScope,userEquation[special.first()]))

        }
    }
    //turns a list of string numbers into a an single int value
    private fun extractValue(listedNumber:MutableList<String>): String {
        val number = StringBuilder()
        listedNumber.forEach {
            number.append(it)
        }

        return number.toString()
    }
    //run calculations on first and second number based on the received operator
    private fun operationAttempt(firstN: Int, secondN: Int, operator: String): Int{
        return when(operator) {
            "^" -> firstN.toFloat().pow(secondN).toInt()
            "*" -> firstN.times(secondN)
            "/" -> firstN.div(secondN)
            "+" -> firstN.plus(secondN)
            "-" -> firstN.minus(secondN)
            else -> 0
        }
    }
    private fun replaceEquation(problem: String, solution: String, equation: String): String {
        var newEquation = equation.replace(problem, solution)
        when(endPointMultiplication(newEquation, solution)){
            "start" -> newEquation = equation.replace(problem, "*$solution")
            "end" -> newEquation = equation.replace(problem, "$solution*")
            "null" -> newEquation
        }
        return newEquation
    }
    private fun trackBrackets(string: String): MutableList<Pair<Int,Int>> {

        val stack = mutableListOf<Int>()
        val groupStack = mutableListOf<Pair<Int,Int>>()
        string.forEachIndexed { index, c ->

            when(c){
                '(' -> stack.add(index)
                ')' -> {
                    groupStack.add(Pair(stack.last(), index))
                    stack.removeAt(stack.size -1)
                }
            }
        }

        return groupStack
    }
    private fun endPointMultiplication(equation: String, solution: String): String {
        val solTrackingStart = equation.indexOf(solution)
        val solTrackingEnd = equation.reversed().indexOf((solution).reversed())
        val solStart: Char
        val solEnd : Char

        solStart = try {
            equation[solTrackingStart - 1]
        } catch (e: IndexOutOfBoundsException) {
            'e'
        }

        solEnd = try {
            equation[equation.length - solTrackingEnd]
        } catch (e: IndexOutOfBoundsException) {
            'e'
        }

        println(solStart)
        println(solEnd)
        return when {
            solStart == ')' -> "start"
            solEnd == '(' -> "end"
            else -> "null"
        }


    }

}