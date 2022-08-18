package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



import java.util.UUID;

public class Seat {
    @JsonProperty("row")
    private int row;
    @JsonProperty("column")
    private int column;
    @JsonProperty("price")
    private int price = 0;
    @JsonIgnore
    private Boolean purchased = false;
    @JsonIgnore
    private int id;
    @JsonIgnore
    private String token = "";

    Seat() {
    }

    Seat(int row, int column) {
        this.row = row;
        this.column = column;

        if (row <= 4) {
            price = 10;
        } else {
            price = 8;
        }
    }


    public boolean isPurchased() {
        return purchased;
    }

    public void purchase() {
        purchased = true;
    }

    public void setId(int id) { this.id = id; }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setToken() {
        this.token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return this.token.toString();
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void refund() {
        purchased = false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("row: ").append(row).append(", column: ").append(column).append(", price: ").append(price).append
                (", purchased: ").append(purchased).append(", id: ").append(id).append(", token: ").append(token);
        return str.toString();
    }
}
