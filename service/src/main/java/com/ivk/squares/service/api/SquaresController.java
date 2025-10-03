package com.ivk.squares.service.api;

import com.ivk.squares.engine.*;
import com.ivk.squares.service.api.dto.BoardDto;
import com.ivk.squares.service.api.dto.GameStatusDto;
import com.ivk.squares.service.api.dto.SimpleMoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SquaresController {

    private final AiPlayer ai = new AiPlayer();

    @PostMapping("/{rules}/nextMove")
    public ResponseEntity<SimpleMoveDto> nextMove(@PathVariable("rules") String rules,
                                                  @RequestBody BoardDto req) {
        Board b = req.toBoard();
        Color toMove = req.nextColor();

        Rules r = new Rules(b.size(), toMove);
        for (Point p : b.pointsOf(Color.W)) r.board().place(new Move(p.x(), p.y(), Color.W));
        for (Point p : b.pointsOf(Color.B)) r.board().place(new Move(p.x(), p.y(), Color.B));

        if (r.status() != GameStatus.IN_PROGRESS) return ResponseEntity.ok(null);

        Optional<Point> p = ai.choose(r.board(), toMove);
        if (p.isEmpty()) return ResponseEntity.ok(null);

        Point m = p.get();
        return ResponseEntity.ok(new SimpleMoveDto(m.x(), m.y(), String.valueOf(toMove.toLowerChar())));
    }

    @PostMapping("/{rules}/gameStatus")
    public ResponseEntity<GameStatusDto> gameStatus(@PathVariable("rules") String rules,
                                                    @RequestBody BoardDto req) {
        Board b = req.toBoard();

        if (SquareDetector.hasSquare(b.pointsOf(Color.W))) return ResponseEntity.ok(GameStatusDto.win("w"));
        if (SquareDetector.hasSquare(b.pointsOf(Color.B))) return ResponseEntity.ok(GameStatusDto.win("b"));
        if (b.isFull()) return ResponseEntity.ok(GameStatusDto.draw());

        String next = String.valueOf(req.nextColor().toLowerChar());
        return ResponseEntity.ok(GameStatusDto.ok(next));
    }
}
