package ca.jbrains.pos.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SellOneItemControllerTest {
    @Test
    void productFound() {
        final Catalog catalog = new FixedPriceCatalog(795);
        final InMemoryDisplay inMemoryDisplay = new InMemoryDisplay();

        final SellOneItemController controller = new SellOneItemController(catalog, inMemoryDisplay);
        controller.onBarcode("12345");

        Assertions.assertEquals(795, inMemoryDisplay.displayedPriceInCents);
    }

    @Test
    void productNotFound() {
        final Catalog catalog = new CatalogWithoutPriceForBarcode("99999");
        final InMemoryDisplay inMemoryDisplay = new InMemoryDisplay();

        final SellOneItemController controller = new SellOneItemController(catalog, inMemoryDisplay);
        controller.onBarcode("99999");

        Assertions.assertEquals("99999", inMemoryDisplay.barcodeWithNoPrice);
    }

    public static class SellOneItemController {
        private Catalog catalog;
        private Display display;

        public SellOneItemController(Catalog catalog, Display display) {
            this.catalog = catalog;
            this.display = display;
        }

        public void onBarcode(String barcode) {
            final Integer price = catalog.findPrice(barcode);
            if (price == null)
                display.displayProductNotFound(barcode);
            else
                display.displayPrice(price);
        }
    }

    public static class InMemoryDisplay implements Display {
        public Integer displayedPriceInCents = null;
        public String barcodeWithNoPrice = null;

        @Override
        public void displayPrice(int priceInCents) {
            this.displayedPriceInCents = priceInCents;
        }

        @Override
        public void displayProductNotFound(String barcode) {
            this.barcodeWithNoPrice = barcode;
        }
    }

    public interface Catalog {
        Integer findPrice(String barcode);
    }

    private static class FixedPriceCatalog implements Catalog {

        public FixedPriceCatalog(int price) {
            this.price = price;
        }

        private int price;

        @Override
        public Integer findPrice(String barcode) {
            return price;
        }
    }

    private static class CatalogWithoutPriceForBarcode implements Catalog {
        private String barcodeWithoutPrice;

        public CatalogWithoutPriceForBarcode(String barcodeWithoutPrice) {
            this.barcodeWithoutPrice = barcodeWithoutPrice;
        }

        @Override
        public Integer findPrice(String barcode) {
            if (barcodeWithoutPrice.equals(barcode))
                return null;
            else
                throw new RuntimeException("A test is broken.");
        }
    }
}
