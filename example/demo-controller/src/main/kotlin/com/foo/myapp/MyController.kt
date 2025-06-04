package com.foo.myapp

import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException
import javax.servlet.ServletOutputStream
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/test")
@SessionAttributes("myControllerSession")
class MyController {

    @RequestMapping(value = ["/requsetParam.do"], method = [RequestMethod.GET, RequestMethod.POST])
    fun requsetParam(@RequestParam(name = "name", required = false) v: String?, model: Model): String {
        val result = "Hello, $v"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/requsetBodyFormData.do")
    fun requsetBodyFormData(@RequestBody(required = false) data: MultiValueMap<String, String>?, model: ModelMap): String {
        val result = HashMap<String, String>()
        result["result"] = "Hello, ${data?.getFirst("name")}"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/requsetBodyJson.do")
    fun requsetBodyJson(@RequestBody(required = false) data: TestDto?, model: ModelMap): String {
        val result = "Hello, ${data?.name}"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/httpEntity.do")
    fun httpEntity(data: HttpEntity<TestDto>, model: ModelMap): String {
        val result = "Hello, ${data.body.name}"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/redirectAttributes.do")
    fun redirectAttributes(data: HttpEntity<TestDto>, redirectAttrs: RedirectAttributes, model: Model): String {
        val result = "Hello, ${data.body.name}"
        model.addAttribute("result", result)
        return "redirect:/test/sub.do"
    }

    @PostMapping("/modelAttribute.do")
    fun modelAttribute(@ModelAttribute data: TestDto, model: Model): String {
        val result = "Hello, ${data.name}"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/user.do")
    fun user(@AuthenticationPrincipal user: User, model: Model): String {
        val result = "Hello, ${user.username}"
        model.addAttribute("result", result)
        return "main"
    }

    @PostMapping("/sessionAttribute.do")
    fun sessionAttribute(@SessionAttribute(name = "myAttr") myAttr: String?, model: Model): String {
        val result = "SessionAttribute=$myAttr"
        model.addAttribute("result", result)
        model.addAttribute("myControllerSession", "This is from SESSIONATTRIBUTES")
        return "main"
    }

    @PostMapping("/sessionAttributes.do")
    fun sessionAttributes(@ModelAttribute("myControllerSession") g: String?): String {
        return "main"
    }

    @PostMapping("/uploadWithFormData")
    fun upload(@RequestParam("name") name: String, @RequestPart("upfile") file: MultipartFile, response: HttpServletResponse) {
        println(name)
        println(file.originalFilename + ":" + file.size)
        response.status = HttpServletResponse.SC_OK
    }

    @PostMapping("/uploadWithJson")
    fun uploadWithJson(
        @RequestPart("upfile") file: MultipartFile,
        response: HttpServletResponse
    ) {
        response.status = HttpServletResponse.SC_OK
    }

    @GetMapping("/responseBody.do")
    @ResponseBody
    fun responseBody(@RequestParam(name = "name", required = false) v: String?, model: Model): TestDto {
        val data = TestDto()
        data.name = v
        data.age = 66
        return data
    }

    @GetMapping("/responseEntity.do")
    fun responseEntity(@RequestParam(name = "name", required = false) v: String?): ResponseEntity<TestDto> {
        val data = TestDto()
        data.name = v
        data.age = 66
        return ResponseEntity.ok().body(data)
    }

    @GetMapping("/modelAttributeAtMethod.do")
    fun modelAttributeAtMethod(@RequestParam(name = "name", required = false) v: String?, model: Model): String {
        return "main"
    }

    @GetMapping("/modelAndView.do")
    fun modelAndView(@RequestParam(name = "name", required = false) v: String?): ModelAndView {
        val data = TestDto()
        data.name = v
        data.age = 1000

        val mav = ModelAndView()
        mav.viewName = "main"
        mav.addObject("data", data)
        mav.status = HttpStatus.OK
        return mav
    }

    @GetMapping("/void.do")
    fun voidMethid(@RequestParam(name = "name", required = false) v: String?, response: ServletResponse) {
        try {
            response.characterEncoding = "UTF-8"
            response.contentType = "text/html"
            val out: ServletOutputStream = response.outputStream
            out.print("<h2>Hello, $v</h2>")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @ModelAttribute
    fun addAttributes(model: Model) {
        model.addAttribute("name", "Taylor")
    }
}
