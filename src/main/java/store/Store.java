package store;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<Item> items = new ArrayList<>();

    public Store() {
        // Инициализация каталога товаров
        items.add(new Item("Книга", 500.0));
        items.add(new Item("Телефон", 10000.0));
        items.add(new Item("Ноутбук", 20000.0));
        items.add(new Item("Наушники", 1500.0));
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int index) {
        if(index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Неверный индекс товара.");
        }
        return items.get(index);
    }
}
