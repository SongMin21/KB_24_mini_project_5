package org.scoula.thinking.mapper;

import org.scoula.thinking.domain.ThinkingVO;

import java.util.Date;
import java.util.List;

public interface ThinkingMapper {
    public List<ThinkingVO> getList();
    public int updateLike(long id);
    public List<ThinkingVO> getByDate(Date date);
    public int delete(long id);
}
