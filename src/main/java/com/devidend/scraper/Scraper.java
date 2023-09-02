package com.devidend.scraper;

import com.devidend.model.Company;
import com.devidend.model.ScrapedResult;

public interface Scraper {
    ScrapedResult scrap(Company company);
    Company scrapCompanyByTicker(String ticker);
}
