package ch.martinelli.demo.nested.api;

import ch.martinelli.demo.nested.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void getPurchaseOrders_returnsOrders() {
        RestTestClient client = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].id").exists()
                .jsonPath("$[0].orderDate").exists()
                .jsonPath("$[0].customer").exists()
                .jsonPath("$[0].customer.id").exists()
                .jsonPath("$[0].customer.firstName").exists()
                .jsonPath("$[0].customer.lastName").exists()
                .jsonPath("$[0].items").isArray()
                .jsonPath("$[0].items[0].id").exists()
                .jsonPath("$[0].items[0].quantity").exists()
                .jsonPath("$[0].items[0].product").exists()
                .jsonPath("$[0].items[0].product.name").exists()
                .jsonPath("$[0].items[0].product.price").exists();
    }
}
