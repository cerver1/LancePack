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
    fun `split string into mutable list without any empty spaces_is true`(){
        val result = main.splitUserInput("( 1 + 2)")
        assertThat(result).doesNotContain(" ")
    }
}