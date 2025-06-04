package com.foo.myapp

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(urlPatterns = ["/sayhello"])
class MyServlet : HttpServlet() {
    companion object {
        private const val serialVersionUID = 1L
    }

    @Throws(ServletException::class, IOException::class)
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html"
        resp.writer.println("<html><body><h2>Hello, Servlet!</h2></body></html>")
    }
}
