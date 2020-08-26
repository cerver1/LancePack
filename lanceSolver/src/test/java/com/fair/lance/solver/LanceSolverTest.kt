package com.fair.lance.solver

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LanceSolverTest {


    private lateinit var main: LanceSolver

    @Before
    fun setUp() {
        main = LanceSolver()
    }


//    @Test
//    fun `returns solution string` () {
//        val result = LanceSolver().solver("(1+2)")
//        assertThat(result).is
//
//    }

    @Test
    fun `test to ensure that the value an equation goes in_operation with ones string value is returned`() {
        val result = main.solver("( 1 + 2)")
        assertThat(result).isEqualTo("3")
    }

    @Test
    fun `test to ensure that the value an equation goes in_operation with tens string value is returned`() {
        val result = main.solver("( 14 + 21)")
        assertThat(result).isEqualTo("35")
    }

    @Test
    fun `test to ensure that the value an equation goes in_operation with thousands string value is returned`() {
        val result = main.solver("( 141 + 291)")
        assertThat(result).isEqualTo("432")
    }

    @Test
    fun `test to ensure that the value an equation goes in_operation with multiple parenthesis_string value is returned`() {
        val result = main.solver("(1 +( 141 + 291) - 1)")
        assertThat(result).isEqualTo("432")
    }

    @Test
    fun `test to ensure that the value an equation goes in_operation with multiple parenthesis complex_string value is returned`() {
        val result = main.solver("(1 +( 141 + 291) - 1 + (1 * 6))")
        assertThat(result).isEqualTo("438")
    }

    @Test
    fun `split string into mutable list without any empty spaces_is true`() {
        val result = main.splitUserInput("( 1 + 2)")
        assertThat(result).doesNotContain(" ")
    }

    @Test
    fun `search string for special values is return a list of specials`() {
        val result = main.searchForSpecial("0*+-/")
        assertThat(result).containsExactlyElementsIn(mutableListOf(1,2,3,4)) //isEqualTo([0,1,2,3])
    }

    @Test
    fun `remove the first occurrence of a parenthesis`() {
        val result = main.sliceParenthesis("(1+2*(2-4)*1)",Pair(5,9))
        assertThat(result).isEqualTo("2-4")
    }
    @Test
    fun `remove the first occurrence of a parenthesis single parenthesis`() {
        val result = main.sliceParenthesis("(1+2)",Pair(0,4))
        assertThat(result).isEqualTo("1+2")
    }

    @Test
    fun `returns the preceding and succeeding numbers surrounding a single numbers`() {
        val result = main.sliceNumber(mutableListOf('1','+','2'), mutableListOf(1))
        assertThat(result).isEqualTo(Triple(1,2,'+'))
    }

    @Test
    fun `returns the preceding and succeeding numbers surrounding a single special value`() {
        val result = main.sliceNumber(mutableListOf('1','3','+','5','2'), mutableListOf(2))
        assertThat(result).isEqualTo(Triple(13,52,'+'))
    }

    @Test
    fun `returns the preceding and succeeding numbers surrounding a multiple special values`() {
        val result = main.sliceNumber(mutableListOf('1','+','1','2','-','3'), mutableListOf(1,4))
        assertThat(result).isEqualTo(Triple(1,12,'+'))
    }

    @Test
    fun `returns the first number and second number added`() {
        val result = main.operationAttempt(1, 12, '+')
        assertThat(result).isEqualTo("13")
    }

    @Test
    fun `returns the first number and second number multiplied`() {
        val result = main.operationAttempt(1, 12, '*')
        assertThat(result).isEqualTo("12")
    }

    @Test
    fun `returns the first number and second number divided`() {
        val result = main.operationAttempt(1, 12, '/')
        assertThat(result).isEqualTo("0.08333333333333333")
    }
    @Test
    fun `returns the first number and second number raised`() {
        val result = main.operationAttempt(1, 12, '^')
        assertThat(result).isEqualTo("1.0")
    }

    @Test
    fun `returns the first number and second number subtracted`() {
        val result = main.operationAttempt(1, 12, '-')
        assertThat(result).isEqualTo("-11")
    }

    @Test
    fun `return a new equation after replacing the problem snippet with a solution`() {
        val result = main.replaceEquation("1+3","4", "(1*(1+3))")
        assertThat(result).isEqualTo("(1*(4))")
    }

    @Test
    fun `return all positions of parenthesis within your string in_complex Equation`() {
        val result = main.trackBrackets("(1+(141+291)-1+(1*6))")
        assertThat(result).isEqualTo(mutableListOf(Pair(3,11),Pair(15,19),Pair(0,20)))
    }

    @Test
    fun `return all positions of parenthesis within your list in`() {
        val result = main.trackBrackets("(1(2)3)()")
        assertThat(result).isEqualTo(mutableListOf(Pair(2,4),Pair(0,6),Pair(7,8)))
    }

    @Test
    fun `insert multiplication sign if a special value isn't present between a ) or ( and a number`() {
        val result= main.endPointMultiplication("(1(1+2)4)")
        assertThat(result).isEqualTo("(1*(1+2)*4)")
    }


    // change name
    @Test
    fun `insert multiplication sign if a special value isn't present between a ) or this ( and a number`() {
        val result= main.endPointMultiplication("(1+(1+2)4)")
        assertThat(result).isEqualTo("(1+(1+2)*4)")
    }


    // change name
    @Test
    fun `insert multiplication sign if a special value isn't present between a ) or and ( and a number`() {
        val result= main.endPointMultiplication("(1+3(1+2)(4+2)1)")
        assertThat(result).isEqualTo("(1+3*(1+2)*(4+2)*1)")
    }
}