package org.example

import PPABaseVisitor
import org.antlr.v4.runtime.tree.ParseTree

class PPAVariableUsageCheckVisitor : PPABaseVisitor<Array<SyntaxError>>() {
    private val assignedVariables = mutableSetOf<String>()
    private var errors = arrayOf<SyntaxError>()
    private var errorSupression = mutableSetOf<Pair<Int, String>>() // { lineNumber, variableName}

    override fun visitCommand(ctx: PPAParser.CommandContext?): Array<SyntaxError> {
        super.visitCommand(ctx)

        val name = ctx!!.assign_ident().VAR().text
        assignedVariables += name

        super.visitCommand(ctx)
        return errors
    }

    override fun visitIdent(ctx: PPAParser.IdentContext?): Array<SyntaxError> {
        val name = ctx!!.VAR().text
        val codeLine = ctx.start?.line ?: 0

        if (name !in assignedVariables && Pair(codeLine, name) !in errorSupression){
            errors += SyntaxError(String.format("Variable %s is not assigned", name), codeLine)
            errorSupression += Pair(codeLine, name)
        }

        super.visitIdent(ctx)
        return errors
    }

    override fun visit(tree: ParseTree?): Array<SyntaxError> {
        super.visit(tree)
        return errors
    }
}