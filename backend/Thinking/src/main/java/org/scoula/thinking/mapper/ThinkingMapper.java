package org.scoula.thinking.mapper;

import org.scoula.thinking.domain.ThinkingVO;

import java.util.List;

public interface ThinkingMapper {
    public List<ThinkingVO> getList();
    public int updateLike(long id);
}
