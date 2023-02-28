import org.example.PPALineNumberCheckVisitor
import org.example.parseProgram
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class LineNumberCheckerTest {
    @Test
    fun `a program without repeated line number`() {
        val code = "10 t:=sum(1,2,3);\n20 x:=random_choice(4,5,6);\n30 k:=sum(t,x);"

        val parsedProgram = parseProgram(code)
        val visitor = PPALineNumberCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.isEmpty() }
    }

    @Test
    fun `a program with repeated line number`() {
        val code = "10 t:=sum(1,2,random_choice(3,4));\n20 x:=random_choice(4,5,6);\n20 k:=sum(t,x);"

        val parsedProgram = parseProgram(code)
        val visitor = PPALineNumberCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.isNotEmpty() }
        assertTrue { t.size == 1 }
    }

    @Test
    fun `a program with repeated line number multiple times`() {
        val code = "10 t:=sum(1,2,random_choice(3,4));\n20 x:=random_choice(4,5,6);\n20 k:=sum(t,x);\n20 k:=sum(t,x);"

        val parsedProgram = parseProgram(code)
        val visitor = PPALineNumberCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.isNotEmpty() }
        assertTrue { t.size == 2 }
    }

    @Test
    fun `a program with multiple repeated line number`() {
        val code = "10 t:=sum(1,2,random_choice(3,4));\n20 x:=random_choice(4,5,6);\n20 k:=sum(t,x);\n10 x:=sum(x,t);"

        val parsedProgram = parseProgram(code)
        val visitor = PPALineNumberCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.isNotEmpty() }
        assertTrue { t.size == 2 }
    }
}