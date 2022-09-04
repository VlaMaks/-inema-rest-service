package cinema.controller;

import cinema.exception.PurchaseException;
import cinema.exception.SeatNotFoundException;
import cinema.exception.TokenNotFoundException;
import cinema.exception.WrongPasswordException;
import cinema.model.Seat;
import cinema.service.MovieTheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MainController {

    @Autowired
    private MovieTheaterService movieTheater;


    @GetMapping("/seats")
    public ResponseEntity<MovieTheaterService> getSeat() {
        return new ResponseEntity<>(movieTheater, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    //@ResponseBody
    public ResponseEntity<Map<String, Object>> bookSeat(@RequestBody Seat seat) {
        if (seat.getRow() < 1 || seat.getRow() > movieTheater.getTotalRows() || seat.getColumn() < 1 || seat.getColumn() > movieTheater.getTotalColumns()) {
            throw new SeatNotFoundException("The number of a row or a column is out of bounds!");
        }

        Seat removeSeat = movieTheater.findSeat(seat.getRow(), seat.getColumn());

        if (removeSeat != null) {
            movieTheater.getPurchasedSeats().put(removeSeat.getToken().toString(), removeSeat);
            movieTheater.getAvailableSeats().remove(removeSeat);
            System.out.println(" removeSeat " + removeSeat.getToken());
            return new ResponseEntity<>(Map.of( "ticket", Map.of("row", removeSeat.getRow(), "column", removeSeat.getColumn(), "price", removeSeat.getPrice()), "token", removeSeat.getToken()), HttpStatus.OK);
        }

        throw new PurchaseException("The ticket has been already purchased!");
    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, Object>> returnSeat(@RequestBody String token) {
        Seat returnSeat;

        try {
            String tok = token.strip().split(":\"")[1].split("\"")[0];
            System.out.println(tok);
            returnSeat = movieTheater.findSeatByToken(tok);
            System.out.println("returnSeat " + returnSeat);
        } catch (Exception e) {
            throw new TokenNotFoundException("Wrong token!");
        }

        if (returnSeat != null) {
            movieTheater.getAvailableSeats().add(movieTheater.getPurchasedSeats().remove(returnSeat.getToken().toString()));
            return new ResponseEntity<>(Map.of("returned_ticket", Map.of("row", returnSeat.getRow(), "column", returnSeat.getColumn(), "price", returnSeat.getPrice())), HttpStatus.OK);
        }

        throw new TokenNotFoundException("Wrong token!");
    }

    @PostMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getStat(@RequestParam(value = "password", required = false) String password) {
        if ("super_secret".equals(password)) {
            return new ResponseEntity<>(movieTheater.getStatistic(), HttpStatus.OK);
        }

        throw new WrongPasswordException("The password is wrong!");
    }


    @ExceptionHandler
    public ResponseEntity<ConcurrentHashMap<String, String>> handleAllException(Exception e){
        ConcurrentHashMap<String, String> response = new ConcurrentHashMap<>();
        String message = "";

        switch (e.getClass().getSimpleName()) {
            case "SeatNotFoundException":
            case "TokenNotFoundException":
            case "PurchaseException":
            case "WrongPasswordException":
                message = e.getMessage();
                break;
        }

        response.put("error", message);

        if ("The password is wrong!".equals(message)) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
