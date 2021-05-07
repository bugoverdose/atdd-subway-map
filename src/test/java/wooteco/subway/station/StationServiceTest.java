package wooteco.subway.station;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.station.StationDaoCache;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationServiceDto;
import wooteco.subway.service.StationService;

class StationServiceTest {

    @DisplayName("서비스에서 저장 테스트")
    @Test
    void save() {
        // given
        StationServiceDto requestStationServiceDto = new StationServiceDto("스타벅스 선정릉역");
        Station savedStation = new Station((long) 2, "스타벅스 선정릉역");
        Station station = new Station(requestStationServiceDto.getName());

        StationDaoCache mockDao = mock(StationDaoCache.class);
        when(mockDao.save(any())).thenReturn(savedStation);

        StationService stationService = new StationService(mockDao);

        // when
        StationServiceDto savedStationServiceDto = stationService.save(requestStationServiceDto);

        // then
        assertThat(savedStationServiceDto.getId()).isEqualTo(savedStation.getId());
        assertThat(savedStationServiceDto.getName()).isEqualTo(savedStation.getName());
    }

    @DisplayName("서비스에서 전체 역 호출")
    @Test
    void load() {
        // given
        List<Station> stations = Arrays.asList(
            new Station((long) 1, "성서공단역"),
            new Station((long) 2, "이곡역"),
            new Station((long) 3, "용산역")
        );

        StationDaoCache mockDao = mock(StationDaoCache.class);
        when(mockDao.showAll()).thenReturn(stations);
        StationService stationService = new StationService(mockDao);

        List<StationServiceDto> expectedDtos = Arrays.asList(
            new StationServiceDto((long) 1, "성서공단역"),
            new StationServiceDto((long) 2, "이곡역"),
            new StationServiceDto((long) 3, "용산역")
        );

        // when
        List<StationServiceDto> requestedDtos = stationService.showStations();

        // then
        assertThat(requestedDtos.get(0).getId()).isEqualTo(expectedDtos.get(0).getId());
        assertThat(requestedDtos.get(0).getName()).isEqualTo(expectedDtos.get(0).getName());
        assertThat(requestedDtos.get(1).getId()).isEqualTo(expectedDtos.get(1).getId());
        assertThat(requestedDtos.get(1).getName()).isEqualTo(expectedDtos.get(1).getName());
        assertThat(requestedDtos.get(2).getId()).isEqualTo(expectedDtos.get(2).getId());
        assertThat(requestedDtos.get(2).getName()).isEqualTo(expectedDtos.get(2).getName());
    }


}