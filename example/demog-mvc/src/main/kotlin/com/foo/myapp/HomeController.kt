package com.foo.myapp

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.text.DateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@Controller
class HomeController {
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @RequestMapping(value = ["/home"], method = [RequestMethod.GET])
    fun home(locale: Locale, model: Model): String {
        logger.info("Welcome home! The client locale is {}.", locale)

        val date = Date.from(Instant.now())
        val df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale)
        val d = df.format(date)

        model.addAttribute("serverTime", d)

        return "home"
    }

    @RequestMapping(value = ["/main.do"])
    fun mainPage(model: Model): String {
        model.addAttribute("name", "아소카")
        return "main"
    }
}
