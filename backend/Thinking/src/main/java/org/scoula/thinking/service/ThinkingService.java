package org.scoula.thinking.service;

import org.scoula.thinking.dto.ThinkingCreateDTO;
import org.scoula.thinking.dto.ThinkingDTO;

import java.util.Date;
import java.util.List;

public interface ThinkingService {
    // 전체
    public List<ThinkingDTO> getThinking();

    // 강민주
    public List<ThinkingDTO> getByDate(Date date);
    public boolean updateLike(long id);
    // 복원준

    public List<ThinkingDTO> getByCategory(String category);
    // 이현서
    public List<ThinkingDTO> getByLike();
    // 이현주
    public ThinkingDTO getListOne(Long id);
    public ThinkingDTO create(ThinkingCreateDTO thinking);
}
