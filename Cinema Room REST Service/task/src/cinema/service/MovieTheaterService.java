package cinema.service;

import cinema.model.MovieTheater;
import cinema.model.Seat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MovieTheaterService {
    @Autowired
    private MovieTheater service;

    @JsonIgnore
    public Map<String, Seat> getPurchasedSeats() {
        return service.getPurchasedSeats();
    }

    public Seat findSeat(int row, int column) {
        for (Seat seat : service.getAvailableSeats()) {
            if (row == seat.getRow() && column == seat.getColumn()) {
                return seat;
            }
        }
        return null;
    }

    public int getTotalRows() {
        return service.getTotalRows();
    }

    public int getTotalColumns() {
        return service.getTotalColumns();
    }

    public List<Seat> getAvailableSeats() {
        return service.getAvailableSeats();
    }

    public Seat findSeatByToken(String token) {
        return service.getPurchasedSeats().get(token);
    }

    @JsonIgnore
    public Map<String, Integer> getStatistic() {
        int totalCost = 0;
        for (Map.Entry<String, Seat> entry: getPurchasedSeats().entrySet()) {
            totalCost += entry.getValue().getPrice();
        }
        return Map.of("current_income", totalCost, "number_of_available_seats", getAvailableSeats().size(),
                      "number_of_purchased_tickets", getPurchasedSeats().size());
    }
}
