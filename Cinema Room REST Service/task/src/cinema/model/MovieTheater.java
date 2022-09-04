package cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MovieTheater {
    private int totalRows = 9;
    private int totalColumns = 9;
    private List<Seat> availableSeats;

    @JsonIgnore
    private Map<String, Seat> purchasedSeats;

    public Map<String, Seat> getPurchasedSeats() {
        return purchasedSeats;
    }

    private void initialSeats() {
        if (availableSeats == null) {
            availableSeats = new ArrayList<>();
            for (int row = 1; row <= totalRows; ++row) {
                int price = row <= 4 ? 10: 8;
                for (int col = 1; col <= totalColumns; ++col) {
                    Seat seat = new Seat(row, col);
                    seat.setPrice(price);
                    seat.setToken();
                    availableSeats.add(seat);
                }
            }
        }

    }

    private void initialPurchasedSeats() {
        if (purchasedSeats == null) {
            purchasedSeats = new ConcurrentHashMap<>();
        }
    }

    public MovieTheater() {
        initialSeats();
        initialPurchasedSeats();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<Seat> availableSeats) {
        this.availableSeats = availableSeats;
    }
}
