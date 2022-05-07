package wooteco.subway.dto.request;

public class StationRequest {

    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StationRequest{" + "name='" + name + '\'' + '}';
    }
}
