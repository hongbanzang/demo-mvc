package com.foo.myapp

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.springframework.ui.ModelMap

class MyControllerKotestTest : StringSpec({
    "requsetBodyJson should add result to model and return main" {
        val fixtureMonkey = FixtureMonkey.builder().build()
        val dto = fixtureMonkey.giveMeOne<TestDto>()
        val model = mockk<ModelMap>(relaxed = true)
        val controller = MyController()

        val viewName = controller.requsetBodyJson(dto, model)

        viewName shouldBe "main"
        verify { model.addAttribute("result", "Hello, ${dto.name}") }
    }
})
