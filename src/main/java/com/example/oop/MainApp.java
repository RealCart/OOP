package com.example.oop;

import com.example.oop.authentication.AuthenticationService;
import com.example.oop.authentication.AuthenticationServiceImpl;
import com.example.oop.models.Customer;
import com.example.oop.models.User;
import com.example.oop.store.Item;
import com.example.oop.store.Store;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    private final AuthenticationService authService = new AuthenticationServiceImpl();
    private final Store store = new Store();
    private final ChatService chatService = new ChatService();
    private Scene topbarScene;
    private Scene loginScene;
    private Scene registrationScene;
    private User currentUser;
    private Cart cart;

    @Override
    public void init() {
        DBManager.initDatabase();
    }

    @Override
    public void start(Stage primaryStage) {
        VBox topbarLayout = new VBox(10);
        topbarLayout.setAlignment(Pos.TOP_CENTER);
        topbarLayout.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Добро пожаловать в приложение!");
        Button loginButton = new Button("Логин");
        loginButton.setOnAction(e -> showLogin(primaryStage));
        Button registerButton = new Button("Регистрация");
        registerButton.setOnAction(e -> showRegistration(primaryStage));

        HBox buttonBox = new HBox(10, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        topbarLayout.getChildren().addAll(welcomeLabel, buttonBox);

        topbarScene = new Scene(topbarLayout, 400, 300);
        primaryStage.setScene(topbarScene);
        primaryStage.show();
    }

    private void showLogin(Stage primaryStage) {
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Войти");
        Label loginMessage = new Label();

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> primaryStage.setScene(topbarScene));

        VBox loginLayout = new VBox(10, backButton, emailLabel, emailField, passwordLabel, passwordField, loginButton, loginMessage);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));
        loginScene = new Scene(loginLayout, 400, 300);

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            try {
                currentUser = authService.login(email, password);
                cart = new Cart();
                showMainScreen(primaryStage);
            } catch (Exception ex) {
                loginMessage.setText("Ошибка: " + ex.getMessage());
            }
        });

        primaryStage.setScene(loginScene);
    }

    private void showRegistration(Stage primaryStage) {
        Label nameLabel = new Label("Имя:");
        TextField nameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Зарегистрироваться");
        Label registrationMessage = new Label();

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> primaryStage.setScene(topbarScene));

        VBox registrationLayout = new VBox(10, backButton, nameLabel, nameField, emailLabel, emailField, passwordLabel, passwordField, registerButton, registrationMessage);
        registrationLayout.setAlignment(Pos.CENTER);
        registrationLayout.setPadding(new Insets(20));
        registrationScene = new Scene(registrationLayout, 400, 300);

        registerButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                registrationMessage.setText("Заполните все поля!");
                return;
            }

            try {
                User newUser = new Customer(name, email, password);
                authService.register(newUser);
                registrationMessage.setText("Регистрация успешна! Теперь вы можете войти.");
            } catch (Exception ex) {
                registrationMessage.setText("Ошибка: " + ex.getMessage());
            }
        });

        primaryStage.setScene(registrationScene);
    }

    private void showMainScreen(Stage primaryStage) {
        Label welcomeLabel = new Label("Добро пожаловать, " + currentUser.getName());
        Label balanceLabel = new Label("Баланс: " + currentUser.getBalance() + " тг.");
        ListView<Item> itemListView = new ListView<>();
        itemListView.getItems().addAll(store.getItems());

        Button addToCartButton = new Button("Добавить в корзину");
        addToCartButton.setOnAction(e -> {
            Item selectedItem = itemListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cart.addItem(selectedItem);
                showAlert("Информация", "Товар добавлен в корзину");
            }
        });

        Button showCartButton = new Button("Корзина");
        showCartButton.setOnAction(e -> showCartDialog(primaryStage));

        Button chatButton = new Button("Чат с техподдержкой");
        chatButton.setOnAction(e -> showChatDialog(primaryStage));

        Button logoutButton = new Button("Выйти");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            primaryStage.setScene(topbarScene);
        });

        HBox topBox = new HBox(10, welcomeLabel, balanceLabel, logoutButton);
        topBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10, topBox, itemListView, addToCartButton, showCartButton, chatButton);
        mainLayout.setPadding(new Insets(15));
        Scene mainScene = new Scene(mainLayout, 400, 400);

        primaryStage.setScene(mainScene);
    }

    private void showCartDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.setTitle("Корзина");
        ListView<Item> cartListView = new ListView<>();
        cartListView.getItems().addAll(cart.getItems());

        Label totalLabel = new Label("Сумма: " + cart.getTotal() + " тг.");

        Button purchaseButton = new Button("Оформить покупку");
        purchaseButton.setOnAction(e -> {
            double total = cart.getTotal();
            if (currentUser.getBalance() < total) {
                showAlert("Ошибка", "Недостаточно средств!");
            } else {
                currentUser.deductBalance(total);
                cart.clear();
                cartListView.getItems().clear();
                totalLabel.setText("Сумма: 0 тг.");
                showAlert("Успех", "Покупка успешно оформлена!");
            }
        });

        VBox dialogLayout = new VBox(10, cartListView, totalLabel, purchaseButton);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setPadding(new Insets(15));
        Scene dialogScene = new Scene(dialogLayout, 300, 300);

        dialog.setScene(dialogScene);
        dialog.initOwner(owner);
        dialog.show();
    }

    private void showChatDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.setTitle("Чат с техподдержкой");

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setText(String.join("\n", chatService.getConversation(currentUser.getEmail(), "admin@example.com")));

        TextField messageField = new TextField();
        messageField.setPromptText("Введите сообщение...");

        Button sendButton = new Button("Отправить");
        sendButton.setOnAction(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                chatService.sendMessage(currentUser.getEmail(), "admin@example.com", message);
                chatArea.setText(String.join("\n", chatService.getConversation(currentUser.getEmail(), "admin@example.com")));
                messageField.clear();
            }
        });

        VBox chatLayout = new VBox(10, chatArea, messageField, sendButton);
        chatLayout.setPadding(new Insets(15));
        chatLayout.setAlignment(Pos.CENTER);

        Scene chatScene = new Scene(chatLayout, 400, 400);
        dialog.setScene(chatScene);
        dialog.initOwner(owner);
        dialog.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

