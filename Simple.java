import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Simple {
    public static void printHelloWorld() {
        System.out.println("Hello World");
    }

    // Reverses the input string and returns the reversed result
    public static String reverseString(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }

    // Generates an HTML login page for the Employee Management System
    public static String createLoginPageHtml(String title) {
        String pageTitle = (title == null || title.isEmpty()) ? "Gyalsung Employment Management System - Login" : title;
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <title>" + pageTitle + "</title>\n"
                + "  <style>\n"
                + "    :root{--bg:#ffffff;--card:#ffffff;--text:#111827;--muted:#6b7280;--primary:#2563eb;}\n"
                + "    *{box-sizing:border-box}body{margin:0;font-family:system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif;background:var(--bg);color:var(--text);min-height:100vh;display:flex;align-items:center;justify-content:center;padding:24px}\n"
                + "    .card{width:100%;max-width:420px;background:var(--card);border:1px solid rgba(0,0,0,0.08);border-radius:12px;box-shadow:0 10px 20px rgba(0,0,0,0.06);padding:24px}\n"
                + "    h1{font-size:22px;margin:0 0 16px;color:#111827;text-align:center}\n"
                + "    .field{margin:12px 0}\n"
                + "    label{display:block;font-size:14px;color:var(--muted);margin-bottom:6px}\n"
                + "    input{width:100%;padding:12px 14px;border-radius:10px;border:1px solid #d1d5db;background:#ffffff;color:#111827;outline:none}input:focus{border-color:var(--primary);box-shadow:0 0 0 3px rgba(37,99,235,0.2)}\n"
                + "    .row{display:flex;align-items:center;justify-content:space-between;margin-top:8px;font-size:14px;color:var(--muted)}\n"
                + "    button{width:100%;margin-top:16px;padding:12px;border:none;border-radius:10px;background:var(--primary);color:#fff;font-weight:600;cursor:pointer}button:disabled{opacity:.6;cursor:not-allowed}\n"
                + "    .error{color:#fca5a5;font-size:13px;height:18px;margin-top:8px}\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"card\" role=\"form\" aria-labelledby=\"title\">\n"
                + "    <h1 id=\"title\">" + pageTitle + "</h1>\n"
                + "    <div class=\"field\">\n"
                + "      <label for=\"email\">Email</label>\n"
                + "      <input type=\"email\" id=\"email\" autocomplete=\"username\" required placeholder=\"you@example.com\">\n"
                + "    </div>\n"
                + "    <div class=\"field\">\n"
                + "      <label for=\"password\">Password</label>\n"
                + "      <input type=\"password\" id=\"password\" autocomplete=\"current-password\" required placeholder=\"••••••••\">\n"
                + "    </div>\n"
                + "    <div class=\"row\">\n"
                + "      <label><input type=\"checkbox\" id=\"remember\"> Remember me</label>\n"
                + "      <a href=\"#\" style=\"color:var(--primary);text-decoration:none\">Forgot?</a>\n"
                + "    </div>\n"
                + "    <button id=\"loginBtn\" disabled>Sign In</button>\n"
                + "    <div id=\"error\" class=\"error\" aria-live=\"polite\"></div>\n"
                + "  </div>\n"
                + "  <script>\n"
                + "    const email=document.getElementById('email');\n"
                + "    const password=document.getElementById('password');\n"
                + "    const remember=document.getElementById('remember');\n"
                + "    const btn=document.getElementById('loginBtn');\n"
                + "    const err=document.getElementById('error');\n"
                + "    function validate(){\n"
                + "      const ok= email.value.includes('@') && password.value.length>=6;\n"
                + "      btn.disabled=!ok;\n"
                + "      err.textContent = ok? '' : 'Enter a valid email and a password with at least 6 characters.';\n"
                + "    }\n"
                + "    email.addEventListener('input', validate);\n"
                + "    password.addEventListener('input', validate);\n"
                + "    btn.addEventListener('click',()=>{\n"
                + "      alert('Login submitted: '+email.value+' | remember='+remember.checked);\n"
                + "    });\n"
                + "    validate();\n"
                + "  </script>\n"
                + "</body>\n"
                + "</html>\n";
    }

    public static void writeLoginPage(String filePath, String title) throws IOException {
        String html = createLoginPageHtml(title);
        Files.write(Paths.get(filePath), html.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        if (args != null && args.length > 0 && "gen-login".equalsIgnoreCase(args[0])) {
            String filePath = (args.length > 1) ? args[1] : "login.html";
            String title = (args.length > 2) ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "Gyalsung Employment Management System - Login";
            try {
                writeLoginPage(filePath, title);
                System.out.println("Login page generated at: " + filePath);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            printHelloWorld();
        }
    }
}

class LoginPage extends Application {

    @Override

    @Override
    public void start(Stage stage) {
        Label title = new Label("Login");
        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button loginBtn = new Button("Login");

        VBox root = new VBox(12, title, username, password, loginBtn);
        root.setStyle("-fx-background-color: #CC5500; -fx-padding: 40;"); 
        // #CC5500 = dark orange

        title.setStyle("-fx-text-fill: white; -fx-font-size: 24;");
        username.setStyle("-fx-background-color: white;");
        password.setStyle("-fx-background-color: white;");
        loginBtn.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: white;");

        stage.setScene(new Scene(root, 350, 250));
        stage.setTitle("Dark Orange Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
