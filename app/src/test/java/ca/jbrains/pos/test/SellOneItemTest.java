package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SellOneItemTest {
    @Test
    void productFound() {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<>() {{
            put("12345", "$7.95");
        }});

        sale.onBarcode("12345");

        Assertions.assertEquals("$7.95", display.getText());
    }

    @Test
    void anotherProductFound() {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<>() {{
            put("23456", "$12.50");
        }});

        sale.onBarcode("23456");

        Assertions.assertEquals("$12.50", display.getText());
    }

    @Test
    void productNotFound() {
        final Display display = new Display();
        final Sale sale = new Sale(display, new HashMap<>());

        sale.onBarcode("99999");

        Assertions.assertEquals("Product not found: 99999", display.getText());
    }

    @Test
    void emptyBarcode() {
        final Display display = new Display();
        final Sale sale = new Sale(display, null);

        sale.onBarcode("");

        Assertions.assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Sale {
        private final Display display;
        private final Map<String, String> pricesByBarcode;

        public Sale(Display display, Map<String, String> pricesByBarcode) {
            this.display = display;
            this.pricesByBarcode = pricesByBarcode;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode))
                display.setText("Scanning error: empty barcode");
            else if (pricesByBarcode.containsKey(barcode))
                display.setText(pricesByBarcode.get(barcode));
            else
                display.setText(String.format("Product not found: %s", barcode));
        }
    }

    public static class Display {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
