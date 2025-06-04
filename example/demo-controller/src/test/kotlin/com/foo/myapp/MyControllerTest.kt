import com.foo.config.AppConfig
import com.foo.config.SecurityConfig
import com.foo.config.WebConfig
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.mock.web.MockMultipartFile
import org.springframework.mock.web.MockServletContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer
import org.springframework.web.context.WebApplicationContext
import java.io.InputStream
import javax.servlet.ServletContext

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [AppConfig::class, SecurityConfig::class, WebConfig::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyControllerTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc
    private lateinit var mockSession: MockHttpSession
    private val USER_ID = "scott"
    private val USER_PASS = "5555"
    private val FILE_NAME = "logo.png"

    @BeforeAll
    fun setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .apply(SharedHttpSessionConfigurer.sharedHttpSession())
            .build()

        mockSession = MockHttpSession()
        mockSession.setAttribute("myAttr", "This is from HttpSession")
    }

    @Test
    @Order(1)
    @DisplayName("setup")
    fun setup() {
        val servletContext: ServletContext = webApplicationContext.servletContext
        Assertions.assertNotNull(servletContext)
        Assertions.assertTrue(servletContext is MockServletContext)
        Assertions.assertNotNull(webApplicationContext.getBean("myController"))
    }

    @Test
    @Order(2)
    @DisplayName("testRequestParam")
    fun testRequestParam() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/requsetParam.do")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Kate")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .session(mockSession)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("result", "Hello, Kate"))
    }

    @Test
    @Order(3)
    @DisplayName("testRequestBodyFormData")
    fun testRequestBodyFormData() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/requsetBodyFormData.do")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("name=Kate&age=35")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attributeExists("result"))
    }

    @Test
    @Order(4)
    @DisplayName("testRequestBodyJson")
    fun testRequestBodyJson() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/requsetBodyJson.do")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"Kate\", \"age\": \"77\"}")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("result", "Hello, Kate"))
    }

    @Test
    @Order(5)
    @DisplayName("testHttpEntity")
    fun testHttpEntity() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/httpEntity.do")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"Kate\", \"age\": \"77\"}")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("result", "Hello, Kate"))
    }

    @Test
    @Order(6)
    @DisplayName("testRedirectAttributes")
    fun testRedirectAttributes() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/redirectAttributes.do")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"Kate\", \"age\": \"77\"}")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/test/sub.do"))
    }

    @Test
    @Order(7)
    @DisplayName("testModelAttribute")
    fun testModelAttribute() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/modelAttribute.do")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Kate")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("result", "Hello, Kate"))
    }

    @Test
    @Order(8)
    @DisplayName("testUser")
    fun testUser() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/user.do")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("result", "Hello, scott"))
    }

    @Test
    @Order(9)
    @DisplayName("testSessionAttribute")
    fun testSessionAttribute() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/test/sessionAttribute.do")
                .session(mockSession)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USER_ID, USER_PASS))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(model().attribute("myControllerSession", equalTo("This is from SESSIONATTRIBUTES")))
    }

    @Test
    @Order(10)
    @DisplayName("testUploadWithFormData")
    @WithMockUser(username = "scott")
    fun testUploadWithFormData() {
        val inputStream: InputStream = javaClass.getResourceAsStream("/${FILE_NAME}")
        val mockFile = MockMultipartFile("upfile", FILE_NAME, MediaType.IMAGE_PNG_VALUE, inputStream)
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/test/uploadWithFormData")
                .file(mockFile)
                .param("name", "scott")
                .with(csrf())
        )
            .andExpect(status().isOk)
    }
}
