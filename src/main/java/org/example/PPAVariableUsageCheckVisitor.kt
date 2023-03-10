package org.example

import PPABaseVisitor
import org.antlr.v4.runtime.tree.ParseTree
import java.lang.Integer.parseInt

class PPAVariableUsageCheckVisitor : PPABaseVisitor<Array<SyntaxError>>() {
    private val assignedVariables = mutableSetOf<String>()
    private val varAssignLine = mutableMapOf<String, Int>()
    private var errors = arrayOf<SyntaxError>()
    private var errorSupression = mutableSetOf<Pair<Int, String>>() // { lineNumber, variableName}

    override fun visitProgram(ctx: PPAParser.ProgramContext?): Array<SyntaxError> {
        var res = super.visitProgram(ctx)

        lines.sortBy { parseInt(it.getChild(0).text) }
        lines.forEach { res = aggregateResult(res, super.visitLine(it)) }
        return res
    }

    private val lines = mutableListOf<PPAParser.LineContext>()
    override fun visitLine(ctx: PPAParser.LineContext?): Array<SyntaxError> {
        if (ctx != null) {
            lines += ctx
        }
        return errors
    }

    override fun visitCommand(ctx: PPAParser.CommandContext?): Array<SyntaxError> {
        super.visitCommand(ctx)

        val name = ctx!!.assign_ident().VAR().text
        val codeLine = ctx.start?.line ?: 0
        assignedVariables += name
        varAssignLine[name] = codeLine

        super.visitCommand(ctx)
        return errors
    }

    override fun visitIdent(ctx: PPAParser.IdentContext?): Array<SyntaxError> {
        val name = ctx!!.VAR().text
        val codeLine = ctx.start?.line ?: 0

        val isVarNotAssigned = name !in assignedVariables || varAssignLine[name] == codeLine
        if (isVarNotAssigned && Pair(codeLine, name) !in errorSupression){
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