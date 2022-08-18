package cinema;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    @JsonProperty("total_rows")
    private final int totalRows;
    @JsonProperty("total_columns")
    private final int totalColumns;
    @JsonProperty("available_seats")
    public List<Seat> availableSeats;
    @JsonIgnore
    private int purchasedSeats;

    @JsonIgnore
    int currentIncome;

    Cinema(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        availableSeats = new ArrayList<>();
        int id = 0;

        for (int i = 1; i <= totalRows; i++) {
            for (int j = 1; j <= totalColumns; j++) {
                Seat seat = new Seat(j, i);
                seat.setId(id);
                availableSeats.add(seat);
                id++;
            }
        }
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public Seat getSeat(int id) {
        for (Seat seat : availableSeats) {
            if (seat.getId() == id) {
                return seat;
            }
        }
        return new Seat();
    }

    public Seat getSeat(String token) {
        for (Seat seat : availableSeats) {
            if (seat.getToken().equals(token)) {
                return seat;
            }

        }
        return new Seat();
    }

    public Seat getSeat(int columns, int rows) {
        for (Seat seat : availableSeats) {
            if (seat.getColumn() == columns && seat.getRow() == rows) {
                return seat;
            }
        }
        return new Seat();
    }

    public void purchaseSeat() {
        purchasedSeats++;
    }

    public void refundSeat() {
        purchasedSeats--;
    }

    public void addIncome(int income) {
        currentIncome += income;
    }

    public void refundIncome(int income) {
        currentIncome -= income;
    }

    public int getPurchasedSeats() {
        return purchasedSeats;
    }

    public int getCurrentIncome() {
        return currentIncome;
    }
}


