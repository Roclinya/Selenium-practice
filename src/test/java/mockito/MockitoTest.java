package mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import selenium.util.WebDriverFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MockitoTest {

    @Autowired
    private ObjectFactory<WebDriver> mockFactory;

    @Test
    public void testCreateDriver() {
        // Arrange
//        ObjectFactory<WebDriver> mockFactory = mock(ObjectFactory.class);
        WebDriver mockDriver = mock(WebDriver.class);
        when(mockFactory.getObject()).thenReturn(mockDriver);

        WebDriverFactory factory = new WebDriverFactory(mockFactory);

        // Act
        WebDriver driver = factory.createDriver();

        // Assert
        assertNotNull(driver);
        verify(mockFactory).getObject();
    }
}
