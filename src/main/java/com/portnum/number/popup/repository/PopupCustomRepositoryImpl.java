package com.portnum.number.popup.repository;

import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.portnum.number.popup.entity.QPopup.*;

@Repository
@RequiredArgsConstructor
public class PopupCustomRepositoryImpl implements PopupCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Popup> findPopups(Long adminId, Pageable pageable, PopupSearchCondition searchCondition) {

        // Load Data
        List<Popup> popups = jpaQueryFactory
                .selectFrom(popup)
                .where(allSearch(adminId, searchCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count data
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(popup.count())
                .from(popup)
                .where(allSearch(adminId, searchCondition));

        return PageableExecutionUtils.getPage(popups, pageable, countQuery::fetchOne);
    }

    private BooleanExpression allSearch(Long adminId, PopupSearchCondition condition) {
        BooleanExpression result = popup.deleted.eq(Boolean.FALSE)
                .and(popup.admin.id.eq(adminId));  // 기본 조건 설정

        // 각 조건을 체인에 추가 (null을 무시)
        if (StringUtils.hasText(condition.getName())) {
            result = result.and(popup.name.like("%" + condition.getName() + "%"));
        }
        if (condition.getCategory() != null) {
            result = result.and(categoryEq(condition.getCategory()));
        }
        if (condition.getStat() != null) {
            result = result.and(statEq(condition.getStat()));
        }
        if (condition.getStartDate() != null) {
            result = result.and(endDateGoe(condition.getStartDate()));
        }
        if (condition.getEndDate() != null) {
            result = result.and(startDateLoe(condition.getEndDate()));
        }

        return result;
    }

    private BooleanExpression endDateGoe(LocalDateTime endDate) {
        return popup.endDate.goe(endDate);
    }

    private BooleanExpression startDateLoe(LocalDateTime startDate) {
        return popup.startDate.loe(startDate);
    }


    private BooleanExpression statEq(PopupStatus stat) {
        return popup.stat.eq(stat);
    }

    private BooleanExpression categoryEq(PopupCategory category) {
        return popup.category.eq(category);
    }
}
