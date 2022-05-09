package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;

    public Line(Long id, String name, String color) {
        validateName(name);
        validateColor(color);
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선의 이름 정보가 입력되지 않았습니다.");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("노선의 색상 정보가 입력되지 않았습니다.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
