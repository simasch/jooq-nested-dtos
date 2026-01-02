package ch.martinelli.demo.nested.api;

import ch.martinelli.demo.nested.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPurchaseOrders_returnsOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].orderDate").exists())
                .andExpect(jsonPath("$[0].customer").exists())
                .andExpect(jsonPath("$[0].customer.id").exists())
                .andExpect(jsonPath("$[0].customer.firstName").exists())
                .andExpect(jsonPath("$[0].customer.lastName").exists())
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items[0].id").exists())
                .andExpect(jsonPath("$[0].items[0].quantity").exists())
                .andExpect(jsonPath("$[0].items[0].product").exists())
                .andExpect(jsonPath("$[0].items[0].product.name").exists())
                .andExpect(jsonPath("$[0].items[0].product.price").exists());
    }
}
