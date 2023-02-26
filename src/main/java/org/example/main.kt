package org.example

import PPALexer
import PPAParser
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val inp = BufferedReader(InputStreamReader(System.`in`))
    var raw = ""
    while ((inp.readLine().also { raw += it ?: "" }) != null);

    val analyzers = listOf(PPALineNumberCheckVisitor(), PPAVariableUsageCheckVisitor())

    analyzers.forEach {
        val parsedProgram = parseProgram(raw)
        val ppaProgramContext = parsedProgram.program()
        val ret = it.visit(ppaProgramContext)
        if (ret.isNotEmpty()) {
            ret.forEach(::println)
            return@forEach
        }
    }
}

fun parseProgram(input: String): PPAParser {
    val charStream = CharStreams.fromString(input);
    val lexer = PPALexer(charStream);
    val tokens = CommonTokenStream(lexer)
    return PPAParser(tokens)
}