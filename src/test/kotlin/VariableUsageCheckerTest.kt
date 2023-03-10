import org.example.PPALineNumberCheckVisitor
import org.example.PPAVariableUsageCheckVisitor
import org.example.parseProgram
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class VariableUsageCheckerTest {
    @Test
    fun `no any usage without assign`() {
        val code = "10 t:=sum(1,2,3);\n20 x:=random_choice(4,5,6);\n30 k:=sum(t,x);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.isEmpty() }
    }

    @Test
    fun `variable used without assign`() {
        val code = "10 t:=sum(a,1,2);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 1 }
    }

    @Test
    fun `variable used without assign twice with one error`() {
        val code = "10 t:=sum(a,1,2,a);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 1 }
    }

    @Test
    fun `multiple variables used on the same line`() {
        val code = "10 t:=sum(a,b,c,d);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 4 }
    }

    @Test
    fun `same variable accessed without assign twice on separate liens`() {
        val code = "10 t:=sum(a,1,2);\n20 t:=sum(a,3,4);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 2 }
    }

    @Test
    fun `same variable before assign`() {
        val code = "10 t:=sum(a,1,2);\n20 t:=sum(a,3,4);\n30 a:=sum(1,2);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 2 }
    }

    @Test
    fun `assign and usage on the same line`() {
        val code = "10 a:=sum(a,1,2);"

        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        assertTrue { t.size == 1 }
    }

    @Test
    fun `use before assign with mixed line order`() {
        val code = "10 y:=3;\n5 x:=y;"
        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        print(t.size)
        assertTrue { t.size != 0 }
    }

    @Test
    fun `correct usage with mixed line order`() {
        val code = "100 y:=x;\n1 x:=3;"
        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        print(t.size)
        assertTrue { t.size == 0 }
    }

    @Test
    fun `variable assigned and used on the same line`() {
        val code = "100 y:=sum(y,1,2);"
        val parsedProgram = parseProgram(code)
        val visitor = PPAVariableUsageCheckVisitor()
        val t = visitor.visit(parsedProgram.program())
        print(t.size)
        assertTrue { t.size != 0 }
    }

}