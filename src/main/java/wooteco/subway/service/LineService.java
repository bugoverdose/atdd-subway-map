package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.line.Line;

import java.util.List;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line createLine(String name, String color) {
        Line line = new Line(name, color);
        long id = lineDao.save(line);
        return lineDao.findById(id);
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    public Line findById(long id) {
        return lineDao.findById(id);
    }

    public void editLine(long id, String name, String color) {
        lineDao.update(id, name, color);
    }

    public void deleteLine(long id) {
        lineDao.deleteById(id);
    }
}