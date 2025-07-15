import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> devContainer = new GenericContainer<>("my-app")
            .withExposedPorts(8080) // Указываем, какой порт внутри контейнера мы хотим использовать
            .withEnv("SERVER_PORT", "8080") // Передаем переменные окружения
            .withEnv("NETOLOGY_PROFILE_DEV", "true");

    @Container
    private static final GenericContainer<?> prodContainer = new GenericContainer<>("my-app")
            .withExposedPorts(8081)
            .withEnv("SERVER_PORT", "8081")
            .withEnv("NETOLOGY_PROFILE_DEV", "false");

    @Test
    void test_devProfile() {
        String url = String.format("http://localhost:%d/profile", devContainer.getMappedPort(8080));

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Assertions.assertEquals("Current profile is dev", response.getBody());
        System.out.println("DEV response: " + response.getBody());
    }

    @Test
    void test_prodProfile() {
        String url = String.format("http://localhost:%d/profile", prodContainer.getMappedPort(8081));

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Assertions.assertEquals("Current profile is production", response.getBody());
        System.out.println("PROD response: " + response.getBody());
    }
}