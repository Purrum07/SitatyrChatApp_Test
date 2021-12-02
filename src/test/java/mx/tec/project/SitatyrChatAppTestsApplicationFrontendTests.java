package mx.tec.project;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SitatyrChatAppTestsApplicationFrontendTests {
	
	private static WebDriver driver;
	
	@BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\Trabajos\\7mo Semestre\\Desarrollo de Aplicaciones Web\\ProyectoFinalSitatyr\\AutomatedTests\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@AfterEach
	public void tearDown() {
		driver.quit();
	}
	
	@Test
	public void givenAUser_whenEnteringSitatyrChatApp_thenLoginPageIsDisplayed() throws Exception {
		// When
		driver.get("https://sitatyr-chat-app-fe.herokuapp.com/login");
		String title = driver.getTitle();
			
		// Then
		assertEquals("React App", title);
	}

	@Test
	public void givenAUser_whenSigningInAndClickingContactButton_thenContactsPageIsDisplayed() 
		throws Exception {
			// When
			driver.get("https://sitatyr-chat-app-fe.herokuapp.com/login");
			WebElement emailField = driver.findElement(By.id("username"));
			emailField.sendKeys("Tostador");
			WebElement passwordField = driver.findElement(By.id("outlined-basic"));
			passwordField.sendKeys("test123");
			WebElement submitButton = driver.findElement(By.className("button"));
			submitButton.click();			
			WebElement contactButton = driver.findElement(By.id("botonContactos"));
			contactButton.click();
			String url = driver.getCurrentUrl();
					
			// Then
			assertEquals("https://sitatyr-chat-app-fe.herokuapp.com/contactos", url);
	}
	
	@Test
	public void givenAUser_whenSigningIn_thenChatsPageIsDisplayed() 
		throws Exception {
			// When
			driver.get("https://sitatyr-chat-app-fe.herokuapp.com/login");
			WebElement emailField = driver.findElement(By.id("username"));
			emailField.sendKeys("Tostador");
			WebElement passwordField = driver.findElement(By.id("outlined-basic"));
			passwordField.sendKeys("test123");
			WebElement submitButton = driver.findElement(By.className("button"));
			submitButton.click();
			
			WebElement contactButton = driver.findElement(By.id("botonChat"));
			contactButton.click();
			String url = driver.getCurrentUrl();
			
			// Then
			assertEquals("https://sitatyr-chat-app-fe.herokuapp.com/chat", url);
	}
}
