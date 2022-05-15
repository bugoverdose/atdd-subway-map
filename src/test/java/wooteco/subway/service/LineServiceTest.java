package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.NotFoundException;

@SuppressWarnings("NonAsciiCharacters")
class LineServiceTest extends ServiceTest {

    private static final StationResponse STATION_RESPONSE_1 = new StationResponse(1L, "이미 존재하는 역 이름");
    private static final StationResponse STATION_RESPONSE_2 = new StationResponse(2L, "선릉역");
    private static final StationResponse STATION_RESPONSE_3 = new StationResponse(3L, "잠실역");

    @Autowired
    private LineService service;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao dao;

    @BeforeEach
    void cleanseAndSetUp() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("service_test_fixture.sql"));
        }
    }

    @Test
    void findAll_메서드는_모든_데이터를_노선의_id_순서대로_조회() {
        List<LineResponse> actual = service.findAll();

        List<LineResponse> expected = List.of(
                new LineResponse(1L, "이미 존재하는 노선 이름", "노란색",
                        List.of(STATION_RESPONSE_1, STATION_RESPONSE_3)),
                new LineResponse(2L, "신분당선", "빨간색",
                        List.of(STATION_RESPONSE_1, STATION_RESPONSE_2, STATION_RESPONSE_3)),
                new LineResponse(3L, "2호선", "초록색",
                        List.of(STATION_RESPONSE_1, STATION_RESPONSE_3)));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("find 메서드는 특정 id에 해당되는 데이터를 조회한다")
    @Nested
    class FindTest {

        @Test
        void 구간_정보를_포함한_노선의_모든_지하철역_정보를_정렬하여_조회() {
            LineResponse actual = service.find(2L);

            LineResponse expected = new LineResponse(2L, "신분당선", "빨간색",
                    List.of(STATION_RESPONSE_1, STATION_RESPONSE_2, STATION_RESPONSE_3));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_노선인_경우_예외_발생() {
            assertThatThrownBy(() -> service.find(99999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        private static final String VALID_LINE_NAME = "새로운 노선";
        private static final String COLOR = "노란색";
        private static final long VALID_UP_STATION_ID = 1L;
        private static final long VALID_DOWN_STATION_ID = 2L;
        private static final int DISTANCE = 10;
        private static final long INVALID_ID = 999999L;

        @Test
        void 유효한_입력인_경우_성공() {
            LineResponse actual = service.save(new CreateLineRequest(
                    VALID_LINE_NAME, COLOR, 1L, 2L, DISTANCE));

            LineResponse expected = new LineResponse(4L, VALID_LINE_NAME, COLOR,
                    List.of(STATION_RESPONSE_1, STATION_RESPONSE_2));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_노선명인_경우_예외발생() {
            CreateLineRequest duplicateLineNameRequest = new CreateLineRequest(
                    "이미 존재하는 노선 이름", COLOR, VALID_UP_STATION_ID, VALID_DOWN_STATION_ID, DISTANCE);
            assertThatThrownBy(() -> service.save(duplicateLineNameRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상행역을_입력한_경우_예외발생() {
            CreateLineRequest noneExistingUpStationRequest = new CreateLineRequest(
                    VALID_LINE_NAME, COLOR, INVALID_ID, VALID_DOWN_STATION_ID, DISTANCE);
            assertThatThrownBy(() -> service.save(noneExistingUpStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 존재하지_않는_하행역을_입력한_경우_예외발생() {
            CreateLineRequest noneExistingUpStationRequest = new CreateLineRequest(
                    VALID_LINE_NAME, COLOR, VALID_UP_STATION_ID, INVALID_ID, DISTANCE);
            assertThatThrownBy(() -> service.save(noneExistingUpStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 거리가_0인_경우_예외발생() {
            CreateLineRequest nullDistanceRequest = new CreateLineRequest(
                    VALID_LINE_NAME, COLOR, VALID_UP_STATION_ID, VALID_DOWN_STATION_ID, 0);
            assertThatThrownBy(() -> service.save(nullDistanceRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("delete 메서드는 노선과 모든 구간 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void 존재하는_데이터의_id가_입력된_경우_삭제성공() {
            service.delete(1L);

            boolean lineNotFound = lineDao.findById(1L).isEmpty();
            List<?> sectionsConnectedToLine = dao.findAllByLineId(1L);

            assertThat(lineNotFound).isTrue();
            assertThat(sectionsConnectedToLine).isEmpty();
        }

        @Test
        void 존재하지_않는_데이터의_id가_입력된_경우_예외발생() {
            assertThatThrownBy(() -> service.delete(99999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
