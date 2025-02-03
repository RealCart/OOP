package com.example.oop;

import authentication.AuthenticationService;
import authentication.AuthenticationServiceImpl;
import exceptions.AuthenticationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Admin;
import models.Customer;
import models.User;
import store.Item;
import store.Store;

public class MainApp extends Application {

    private final AuthenticationService authService = new AuthenticationServiceImpl();
    private final Store store = new Store();
    private User currentUser;

    @Override
    public void init() {
        authService.register(new Admin("Иван", "ivan@example.com", "admin123"));
        authService.register(new Customer("Мария", "maria@example.com", "customer123"));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система входа");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Войти");
        Label loginMessage = new Label();

        VBox loginLayout = new VBox(10, emailLabel, emailField, passwordLabel, passwordField, loginButton, loginMessage);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));

        Scene loginScene = new Scene(loginLayout, 300, 250);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                currentUser = authService.login(email, password);
                showMainScreen(primaryStage);
            } catch (AuthenticationException ex) {
                loginMessage.setText("Ошибка: " + ex.getMessage());
            }
        });
    }

    private void showMainScreen(Stage stage) {
        stage.setTitle("Магазин - Добро пожаловать, " + currentUser.getName());

        Label welcomeLabel = new Label("Добро пожаловать, " + currentUser.getName());
        Label balanceLabel = new Label("Баланс: " + currentUser.getBalance() + " руб.");

        ListView<Item> itemListView = new ListView<>();
        itemListView.getItems().addAll(store.getItems());

        Button purchaseButton = new Button("Купить выбранный товар");
        purchaseButton.setOnAction(event -> {
            Item selectedItem = itemListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                showPurchaseDialog(selectedItem, stage, balanceLabel);
            }
        });

        VBox mainLayout = new VBox(10, welcomeLabel, balanceLabel, itemListView, purchaseButton);
        mainLayout.setPadding(new Insets(15));
        Scene mainScene = new Scene(mainLayout, 400, 350);
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * Отображает диалог покупки для выбранного товара.
     *
     * @param item         выбранный товар
     * @param owner        родительское окно
     * @param balanceLabel метка для обновления баланса
     */
    private void showPurchaseDialog(Item item, Stage owner, Label balanceLabel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Покупка: " + item.getName());

        Label priceLabel = new Label("Цена: " + item.getPrice() + " руб.");

        // Выбор способа оплаты с помощью переключателей
        Label paymentLabel = new Label("Выберите способ оплаты:");
        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton creditCardRadio = new RadioButton("Кредитная карта");
        creditCardRadio.setToggleGroup(paymentGroup);
        RadioButton paypalRadio = new RadioButton("PayPal");
        paypalRadio.setToggleGroup(paymentGroup);
        creditCardRadio.setSelected(true); // по умолчанию выбран способ "Кредитная карта"

        Button confirmButton = new Button("Подтвердить покупку");
        Label messageLabel = new Label();

        VBox dialogLayout = new VBox(10, priceLabel, paymentLabel, creditCardRadio, paypalRadio, confirmButton, messageLabel);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setPadding(new Insets(15));

        Scene dialogScene = new Scene(dialogLayout, 300, 250);
        dialog.setScene(dialogScene);
        dialog.show();

        confirmButton.setOnAction(event -> {
            // Простейшая проверка баланса и обработка покупки
            if (currentUser.getBalance() < item.getPrice()) {
                messageLabel.setText("Недостаточно средств!");
            } else {
                // Здесь можно добавить вызов метода processPayment у соответствующей реализации Payment.
                // Для простоты примера мы просто вычтем сумму из баланса.
                currentUser.deductBalance(item.getPrice());
                balanceLabel.setText("Баланс: " + currentUser.getBalance() + " руб.");
                messageLabel.setText("Покупка успешна!");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
