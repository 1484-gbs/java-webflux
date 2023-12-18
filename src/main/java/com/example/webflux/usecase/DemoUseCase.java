package com.example.webflux.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@AllArgsConstructor
public class DemoUseCase {

    public Mono<List<Map<Discount.CustomerType, Integer>>> execute(ServerRequest req) {
        val now = LocalDate.now();

        val discounts = List.of(
                new Discount(Discount.CustomerType.NORMAL, now, now.plusMonths(1).plusDays(1), 20),
                new Discount(Discount.CustomerType.NORMAL, now.minusDays(1), now.plusMonths(1), 5),
                new Discount(Discount.CustomerType.NORMAL, now, now.plusMonths(1), 150),
                new Discount(Discount.CustomerType.NORMAL, now, now.plusMonths(1), 160),
                new Discount(Discount.CustomerType.NORMAL, now.minusDays(1), now, 50),
                new Discount(Discount.CustomerType.NORMAL, now.minusDays(1), now.plusDays(1), 40),
                new Discount(Discount.CustomerType.NORMAL, now.minusMonths(1), now, 50),

                new Discount(Discount.CustomerType.SILVER, now, now.plusMonths(1).plusDays(1), 100),
                new Discount(Discount.CustomerType.SILVER, now.plusDays(1), now.plusMonths(1).minusDays(1), 120), // 対象外（未来）
                new Discount(Discount.CustomerType.SILVER, now.minusDays(1), now.plusMonths(1), 50),

                new Discount(Discount.CustomerType.GOLD, now.minusDays(1), now.minusDays(1), 10050) // 対象外（過去）
        );

        // 対象データにCustomerTypeが存在しない場合はMapにDefault値を保持するパターン
        val discountMap = Arrays.stream(Discount.CustomerType.values())
                .map(r -> discounts.stream()
                        .filter(d -> d.customerType() == r &&
                                !(d.start.isAfter(now) || d.end.isBefore(now))
                        )
                        // startがnowに一番近い かつ endがnowに一番近い かつ amountが一番高い
                        .min(comparing(Discount::start).reversed()
                                .thenComparing(Discount::end)
                                .thenComparing(comparing(Discount::amount).reversed())
                        )
                        .orElse(Discount.ofDefault(r)))
                .collect(Collectors.toMap(Discount::customerType, Discount::amount));

        // 対象データにCustomerTypeが存在しない場合はMapに何も保持しないパターン
        val discountMap2 = discounts.stream()
                .filter(d -> !(d.start.isAfter(now) || d.end.isBefore(now)))
                .collect(
                        Collectors.groupingBy(
                                Discount::customerType,
                                Collectors.collectingAndThen(
                                        Collectors.minBy(
                                                comparing(Discount::start).reversed()
                                                        .thenComparing(Discount::end)
                                                        .thenComparing(comparing(Discount::amount).reversed())
                                        ),
                                        x -> x.map(Discount::amount).orElse(null)
                                )
                        )
                );
        System.out.println(discountMap2);
        System.out.println("NORMAL: " + discountMap2.get(Discount.CustomerType.NORMAL));
        System.out.println("SILVER: " + discountMap2.get(Discount.CustomerType.SILVER));
        System.out.println("GOLD: " + discountMap2.get(Discount.CustomerType.GOLD));
        System.out.println("OTHER: " + discountMap2.get(Discount.CustomerType.OTHER));

        return Mono.just(Arrays.asList(discountMap, discountMap2));
    }

    record Discount(
            CustomerType customerType,
            LocalDate start,
            LocalDate end,
            int amount
    ) {
        static Discount ofDefault(CustomerType customerType) {
            return new Discount(customerType, null, null, customerType.defaultDiscountAmount);
        }

        @AllArgsConstructor
        enum CustomerType {
            NORMAL(0),
            SILVER(148),
            GOLD(1484),
            OTHER(0);

            @Getter
            private final int defaultDiscountAmount;
        }
    }
}

