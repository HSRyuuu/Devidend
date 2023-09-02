package com.devidend.scheduler;

import com.devidend.model.Company;
import com.devidend.model.ScrapedResult;
import com.devidend.persist.CompanyRepository;
import com.devidend.persist.DividendRepository;
import com.devidend.persist.entity.CompanyEntity;
import com.devidend.persist.entity.DividendEntity;
import com.devidend.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper scraper;

    //@Scheduled(fixedDelay = 1000)
    public void test1() throws InterruptedException {
        Thread.sleep(10000);//10초
        System.out.println(Thread.currentThread().getName() + " -> 테스트 1 : " + LocalDateTime.now());
    }
    //@Scheduled(fixedDelay = 1000)
    public void test2() throws InterruptedException {
        Thread.sleep(1000);//1초
        System.out.println(Thread.currentThread().getName() + " -> 테스트 2 : " + LocalDateTime.now());
    }

    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 스크래핑
        for (CompanyEntity company : companies) {
            ScrapedResult scrapedResult = scraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker()).build());
            // 스크래핑한 정보 중 데이터베이스에 없는 값만 저장
            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) {
                            dividendRepository.save(e);
                        }
                    });

            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupted();
            }
        }
    }
}
