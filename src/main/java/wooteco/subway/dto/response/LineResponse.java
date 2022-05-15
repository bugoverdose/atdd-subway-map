package wooteco.subway.dto.response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Line2;
import wooteco.subway.domain.station.Station;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Long id,
                        String name,
                        String color,
                        List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line2 line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),  toStationResponse(line));
    }

    private static List<StationResponse> toStationResponse(Line2 line) {
        return line.getStations()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public static LineResponse of(Line line, List<Station> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),  toStationResponse(stations));
    }

    private static List<StationResponse> toStationResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(color, that.color)
                && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }

    @Override
    public String toString() {
        return "LineResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                '}';
    }
}
