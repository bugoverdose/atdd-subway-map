package wooteco.subway.domain;

import wooteco.subway.exception.section.SectionSortedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    public Section section(int index) {
        return sections.get(index);
    }

    public Section updateSectionToOneLine(Long lineId) {
        final int updatedDistance = sections.stream().mapToInt(Section::getDistance).sum();
        final Long upStationId = sections.get(0).getUpStationId();
        final Long downStationId = sections.get(1).getDownStationId();
        return new Section(lineId, upStationId, downStationId, updatedDistance);
    }

    public List<StationId> sortSectionsByOrder() {
        final Map<Long, Long> stations = initialize();
        final Long firstUpStationId = findFirstUpStation(stations);
        return sortByOrder(stations, firstUpStationId);
    }

    private Map<Long, Long> initialize() {
        Map<Long, Long> stations = new LinkedHashMap<>();
        for (Section section : sections) {
            stations.put(section.getUpStationId(), section.getDownStationId());
        }
        return stations;
    }

    private Long findFirstUpStation(Map<Long, Long> stations) {
        return stations.keySet().stream()
                .filter(upStationId -> !stations.containsValue(upStationId))
                .findFirst()
                .orElseThrow(SectionSortedException::new);
    }

    private List<StationId> sortByOrder(Map<Long, Long> stations, Long firstUpStationId) {
        List<Long> sortedStations = new ArrayList<>();
        sortedStations.add(firstUpStationId);

        for (int i = 0; i < stations.size(); i++) {
            final Long now = sortedStations.get(i);
            final Long next = stations.get(now);
            sortedStations.add(next);
        }
        return wrapToSimpleStation(sortedStations);
    }

    private List<StationId> wrapToSimpleStation(List<Long> stationIds) {
        return stationIds.stream()
                .map(StationId::new)
                .collect(Collectors.toList());
    }
}