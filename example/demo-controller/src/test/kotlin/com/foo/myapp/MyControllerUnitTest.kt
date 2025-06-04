import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.springframework.ui.Model

class MyControllerUnitTest : StringSpec({
    val fixture = FixtureMonkey.builder().build()
    val controller = MyController()

    "requsetParam adds result and returns main" {
        val name = fixture.giveMeOne<String>()
        val model = mockk<Model>(relaxed = true)

        val view = controller.requsetParam(name, model)

        view shouldBe "main"
        verify { model.addAttribute("result", "Hello, $name") }
    }
})
