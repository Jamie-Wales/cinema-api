package cinema;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.Objects;

@RestController
public class CinemaController {
    private final Cinema cinema = new Cinema(9, 9);

    @GetMapping("/seats")
    public Cinema getCinema() {
        return cinema;
    }




    @PostMapping("/purchase")
    public ResponseEntity<Object> postSeat(@RequestBody Seat info) {
        if (info.getColumn() > cinema.getTotalColumns()
                || info.getRow() > cinema.getTotalRows() || info.getColumn() <= 0 || info.getRow() <= 0) {
            Object error = Map.of("error", "The number of a row or a column is out of bounds!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } else {
            Seat seat = cinema.getSeat(info.getColumn(), info.getRow());
            if (seat.isPurchased()) {
                Object error = Map.of("error", "The ticket has been already purchased!");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            } else {
                seat.purchase();
                seat.setToken();
                cinema.addIncome(seat.getPrice());
                cinema.purchaseSeat();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Map<Object, Object> map = Map.of("token", seat.getToken(),
                "ticket", Map.of("row", seat.getRow(), "column", seat.getColumn(),
                                "price", seat.getPrice()));
                return new ResponseEntity<>(gson.toJson(map), HttpStatus.OK);
            }
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnSeat(@RequestBody Map<String, String> token) {
        Seat seat = cinema.getSeat(token.get("token"));
        seat.refund();
        if (seat.getPrice() != 0) {
            cinema.refundIncome(seat.getPrice());
            cinema.refundSeat();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<Object, Object> map = Map.of("returned_ticket", Map.of("row", seat.getRow(), "column",
                    seat.getColumn(), "price", seat.getPrice()));

            return new ResponseEntity<>(gson.toJson(map), HttpStatus.OK);

        } else {
            Object error = Map.of("error", "Wrong token!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(required = false) Map<String, String> map) {
        String str = map.toString();
        int equals = str.indexOf("=");
        str = str.substring(equals + 1, str.length() - 1);
        if (map.size() == 0) {
            HttpStatus httpStatus = HttpStatus.valueOf(401);
            Object error = Map.of("error", "The password is wrong!");
            return new ResponseEntity<>(error, httpStatus);

        } else if (!Objects.equals(str, "super_secret")) {
            HttpStatus httpStatus = HttpStatus.valueOf(401);
            Object error = Map.of("error", "The password is wrong!");
            return new ResponseEntity<>(error, httpStatus);
        } else {
            int currentIncome = cinema.getCurrentIncome();
            int numberOfSeats = cinema.getTotalColumns() * cinema.getTotalColumns() - cinema.getPurchasedSeats();
            int numberOfPurchasedTickets = cinema.getPurchasedSeats();
            Map<String, Integer> returnMap = Map.of("current_income", currentIncome,
                    "number_of_available_seats", numberOfSeats,
                    "number_of_purchased_tickets", numberOfPurchasedTickets);
            return new ResponseEntity<>(returnMap, HttpStatus.OK);
        }
    }


}
