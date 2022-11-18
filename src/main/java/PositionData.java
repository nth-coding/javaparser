import com.github.javaparser.Position;
public class PositionData {
    Position start;
    Position end;

    PositionData(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }
}