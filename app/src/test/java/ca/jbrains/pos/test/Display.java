package ca.jbrains.pos.test;

public interface Display {
    void displayPrice(int priceInCents);

    void displayProductNotFound(String barcode);
}
