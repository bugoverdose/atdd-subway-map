package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import wooteco.subway.entity.SectionEntity;

@SuppressWarnings("NonAsciiCharacters")
class SectionDaoTest extends DaoTest {

    @Autowired
    private SectionDao dao;

    @Test
    void findAll_메서드는_모든_구간_데이터를_조회() {
        List<SectionEntity> actual = dao.findAll();

        List<SectionEntity> expected = List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 10),
                new SectionEntity(2L, 1L, 2L, 3L, 5),
                new SectionEntity(3L, 2L, 1L, 3L, 10));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllByLineId_메서드는_lineId에_해당하는_모든_구간_데이터를_조회() {
        List<SectionEntity> actual = dao.findAllByLineId(1L);

        List<SectionEntity> expected = List.of(
                new SectionEntity(1L, 1L, 1L, 2L, 10),
                new SectionEntity(2L, 1L, 2L, 3L, 5)
        );

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 중복되지_않는_정보인_경우_데이터_생성() {
            dao.save(new SectionEntity(3L, 3L, 1L, 10));

            boolean created = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section WHERE "
                    + "id = 4 AND line_id = 3 AND up_station_id = 3 AND down_station_id = 1 AND distance = 10", Integer.class) > 0;

            assertThat(created).isTrue();
        }

        @Test
        void 중복되는_정보로_생성하려는_경우_예외발생() {
            SectionEntity existingSection = new SectionEntity(1L, 1L, 2L, 10);

            assertThatThrownBy(() -> dao.save(existingSection))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @DisplayName("deleteXXX 메서드들은 해당되는 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void deleteAllByLineId_메서드는_노선에_해당되는_모든_구간_데이터를_삭제() {
            dao.deleteAllByLineId(1L);

            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE line_id = 1", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void deleteByLineIdAndUpStationId_메서드는_노선에서_상행역에_해당되는_데이터를_삭제() {
            dao.deleteByLineIdAndUpStationId(1L, 1L);

            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE id = 1", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void deleteByLineIdAndDownStationId_메서드는_노선에서_하행역에_해당되는_데이터를_삭제() {
            dao.deleteByLineIdAndDownStationId(1L, 3L);

            boolean exists = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section "
                    + "WHERE line_id = 1 AND id = 3", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 존재하지_않는_상행역의_id가_입력되더라도_결과는_동일하므로_예외_미발생() {
            assertThatNoException()
                    .isThrownBy(() -> dao.deleteByLineIdAndUpStationId(999L, 999L));
        }

        @Test
        void 존재하지_않는_하행역의_id가_입력되더라도_결과는_동일하므로_예외_미발생() {
            assertThatNoException()
                    .isThrownBy(() -> dao.deleteByLineIdAndDownStationId(999L, 999L));
        }
    }
}
