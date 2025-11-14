package com.jyh000223.poligon_backend.service;

import com.jyh000223.poligon_backend.dto.CalendarResponseDTO;
import com.jyh000223.poligon_backend.dto.DayResultDTO;
import com.jyh000223.poligon_backend.entities.Checklist;
import com.jyh000223.poligon_backend.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final ChecklistRepository checklistRepository;

    public CalendarResponseDTO getCalendarForUser(String socialId, int year, int month) {

        // 1. 날짜 범위 만들기
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

        // 2. DB에서 해당 유저의 이번달 체크리스트 조회
        List<Checklist> list =
                checklistRepository.findBySocialIdAndCheckDateBetween(socialId, start, end);

        Map<String, DayResultDTO> days = new HashMap<>();

        // 3. 날짜별 통계 계산
        for (Checklist c : list) {
            String date = c.getCheckDate().toString(); // YYYY-MM-DD

            days.putIfAbsent(date, new DayResultDTO());
            DayResultDTO d = days.get(date);

            d.setTotal(d.getTotal() + 1);

            if (c.isClear()) {
                d.setCleared(d.getCleared() + 1);
                d.getClearedGoals().add(c.getGoalId());
            } else {
                d.getPendingGoals().add(c.getGoalId());
            }
        }

        // 4. 완성된 결과 리턴
        return new CalendarResponseDTO(year, month, days);
    }
}
