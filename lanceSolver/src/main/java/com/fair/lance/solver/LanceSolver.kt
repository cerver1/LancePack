package com.fair.lance.solver


import kotlin.math.min
import kotlin.math.pow

class LanceSolver {

    fun solver(originalProblem: String): String {
        var equation = splitUserInput(originalProblem)
        var special = searchForSpecial(equation)
        var originalScope: String
        var scope: String
        var firstNumber: Int
        var secondNumber: Int
        var operator: Char
        var solution = ""
        var snippet: String
        var parenthesis = trackBrackets(equation)

        while (special.isNotEmpty()) {
            originalScope = sliceParenthesis(equation, parenthesis[0])
            scope = originalScope
            special = searchForSpecial(scope)

            while (special.isNotEmpty()) {
                val it = sliceNumber(scope.toMutableList(),special)
                firstNumber = it.first
                secondNumber = it.second
                operator = it.third
                snippet = "$firstNumber$operator$secondNumber"
                solution = operationAttempt(firstNumber, secondNumber, operator)
                scope = replaceEquation(snippet, solution, scope)
                special = searchForSpecial(scope)
            }

            equation = replaceEquation("($originalScope)", scope, equation)
            parenthesis = trackBrackets(equation)
            special = searchForSpecial(equation)


        }

        return solution
    }

    /**
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
     */
    // removes all white space and returning a simple tight list.
    // tested
    fun splitUserInput(userInput: String): String {
        return userInput.replace("\\s".toRegex(), "")
    }

    // finds the special operators within the list
    // tested
    fun searchForSpecial(equation: String): MutableList<Int> {
        val specialList = arrayListOf("^", "*", "/", "+", "-")
        val specialParenthesis = arrayListOf('^', '*', '/', '+', '-')
        val confirmedSpecial = mutableListOf<Int>()

        val result = equation.toCharArray().intersect(specialParenthesis)
            .map { x -> min(equation.count { it == x }, specialParenthesis.count { it == x }) }
            .sum()

        return if (result == 0) {
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

    // separates by parenthesis
    // tested
    fun sliceParenthesis(userEquation: String, special: Pair<Int, Int>): String {

        val startSlice = special.first
        val endSlice = special.second

        return userEquation.slice(startSlice + 1 until endSlice)
    }

    // returns the first preceding and succeeding values of a special value
    // tested
    fun sliceNumber(userEquation: MutableList<Char>, special: MutableList<Int>): Triple<Int, Int, Char> {
        val firstNumber: Int
        val secondNumber: Int
        //val equationScope: String

        return if (special.size > 1) {
            firstNumber =
                userEquation.slice(0 until special.first()).joinToString(separator = "").toInt()
            secondNumber = userEquation.slice(special.first() + 1 until special[1])
                .joinToString(separator = "").toInt()
            Triple(firstNumber, secondNumber, userEquation[special.first()])
        } else {
            firstNumber = userEquation.slice(0 until special.first())
                .joinToString(separator = "").toInt()
            secondNumber = userEquation.slice(special.first() + 1 until userEquation.size)
                .joinToString(separator = "").toInt()
            Triple(firstNumber, secondNumber, userEquation[special.first()])
        }

    }

    //run calculations on first and second number based on the received operator
    // tested
    fun operationAttempt(firstN: Int, secondN: Int, operator: Char): String {
        return when (operator) {
            '^' -> firstN.toDouble().pow(secondN).toString()
            '*' -> firstN.times(secondN).toString()
            '/' -> firstN.div(secondN.toDouble()).toString()
            '+' -> firstN.plus(secondN).toString()
            '-' -> firstN.minus(secondN).toString()
            else -> "null"
        }
    }

    // replace the problem snippet with the solution within the original problem to return a new equation
    // tested
    fun replaceEquation(problem: String, solution: String, equation: String): String {
        var newEquation = equation.replace(problem, solution)
        when (endPointMultiplication(newEquation)) {
            "start" -> newEquation = equation.replace(problem, "*$solution")
            "end" -> newEquation = equation.replace(problem, "$solution*")
            "null" -> newEquation
        }
        return newEquation
    }

    // for extracting the correct order of brackets within a list
    // tested
    fun trackBrackets(string: String): MutableList<Pair<Int, Int>> {

        val stack = mutableListOf<Int>()
        val groupStack = mutableListOf<Pair<Int, Int>>()
        string.forEachIndexed { index, c ->

            when (c) {
                '(' -> stack.add(index)
                ')' -> {
                    groupStack.add(Pair(stack.last(), index))
                    stack.removeAt(stack.size - 1)
                }
            }
        }

        return groupStack
    }

    // failing
    fun endPointMultiplication(equation: String): String {

        var parenthesis = trackBrackets(equation)
        val specialList = arrayListOf('^', '*', '/', '+', '-')
        val stringNumbers = arrayListOf('1','2','3','4','5','6','7','8','9','0')
        val newEquation = StringBuilder(equation)

        parenthesis.forEach { bracket ->
            parenthesis = trackBrackets(newEquation.toString())
            val prefix = (bracket.first)
            val precede = try {
                newEquation[prefix - 1]
            } catch (e: IndexOutOfBoundsException) {
                'n'
            }

            if (!specialList.contains(precede) && stringNumbers.contains(precede)) {
                newEquation.insert(prefix, "*")
            }
            //parenthesis = trackBrackets(newEquation.toString())
        }

        parenthesis = trackBrackets(newEquation.toString())

        parenthesis.forEach { bracket ->

            val suffix = (bracket.second + 1)
            val succeed = try {
                newEquation[suffix]
            } catch (e: IndexOutOfBoundsException) {
                'n'
            }

            if (!specialList.contains(succeed) && stringNumbers.contains(succeed)) {
                newEquation.insert(suffix, "*")
            }
            parenthesis = trackBrackets(newEquation.toString())
        }

        return newEquation.toString()

    }

}
