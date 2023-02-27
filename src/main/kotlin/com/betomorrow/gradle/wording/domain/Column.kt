package com.betomorrow.gradle.wording.domain

class Column(val name: String) {

    val index: Int

    init {
        assert(name.matches(pattern))
        index = name.uppercase()
            .reversed()
            .map { (it - 'A' + 1) }
            .reduceIndexed { i, acc, c -> acc + c * pow26(i) } - 1
    }

    private fun pow26(power: Int): Int {
        return Math.pow(26.0, power.toDouble()).toInt()
    }

    companion object {
        val pattern = Regex("^[a-zA-Z]*$")
    }
}
