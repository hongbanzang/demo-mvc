package com.foo.myapp

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.text.DateFormat
import java.time.Instant
import java.util.Date
import java.util.HashMap
import java.util.Locale

@Controller
class HomeController {
    private val logger = LoggerFactory.getLogger(HomeController::class.java)

    @RequestMapping(value = ["/", "/home"], method = [RequestMethod.GET, RequestMethod.POST])
    fun home(locale: Locale, @AuthenticationPrincipal user: User, model: Model): String {
        logger.info("Welcome home! The client locale is {}.", locale)
        logger.info("{} logged in.", user.username)

        val date = Date.from(Instant.now())
        val df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale)
        val d = df.format(date)

        model.addAttribute("serverTime", d)

        val result = HashMap<String, String>()
        result["name"] = "Kate"
        result["age"] = "77"
        model.addAttribute("data", result)

        return "home"
    }

    @RequestMapping("/main.do")
    fun main(model: Model): String {
        model.addAttribute("name", "아소카")
        return "main"
    }
}
