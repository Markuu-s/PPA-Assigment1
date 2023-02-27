package org.example

import PPABaseVisitor
import java.lang.Integer.parseInt

class PPALineNumberCheckVisitor : PPABaseVisitor<Array<SyntaxError>>() {
    private val existingLines = mutableSetOf<Int>()

    override fun visitNumber(ctx: PPAParser.NumberContext?): Array<SyntaxError> {
        val lineNum = parseInt(ctx?.INT()?.text)

        if (lineNum in existingLines) {
            val codeLine = ctx?.start?.line ?: 0
            return arrayOf(SyntaxError(String.format("Repeated line number %d", lineNum), codeLine))
        }

        existingLines += lineNum
        return arrayOf()
    }

    override fun aggregateResult(aggregate: Array<SyntaxError>?, nextResult: Array<SyntaxError>?): Array<SyntaxError> {
        var ret = arrayOf<SyntaxError>()
        if (aggregate != null)
            ret += aggregate
        if (nextResult != null)
            ret += nextResult

        return ret
    }

}