package com.moyeo.backend.common.util;

import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDslSortUtil {

    public static OrderSpecifier<?>[] getOrderSpecifiers(
            Pageable pageable,
            Map<String, ComparableExpressionBase<?>> sortPaths
    ) {
        if (!pageable.getSort().isSorted()) {
            throw new CustomException(ErrorCode.INVALID_SORT_EXCEPTION);
        }

        List<OrderSpecifier<?>> specifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            ComparableExpressionBase<?> path = sortPaths.get(property);

            if (path == null) {
                throw new CustomException(ErrorCode.INVALID_SORT_EXCEPTION);
            }

            specifiers.add(new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    path
            ));
        }

        return specifiers.toArray(new OrderSpecifier[0]);
    }
}
