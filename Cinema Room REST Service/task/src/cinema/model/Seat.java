package cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Seat {
    @JsonIgnore
    private UUID token;
    private int row;
    private int column;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Seat() {
    }

    public UUID getToken() {
        return token;
    }

    public void setToken() {
        this.token = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Seat{" +
                "token=" + token +
                ", row=" + row +
                ", column=" + column +
                ", price=" + price +
                '}';
    }
}
