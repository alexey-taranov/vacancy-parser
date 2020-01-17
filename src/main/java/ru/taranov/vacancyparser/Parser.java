package ru.taranov.vacancyparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private String url;
    private static final Logger LOG = LogManager.getLogger(Parser.class.getName());


    public Parser(String url) {
        this.url = url;
    }

    private Document getPage(String url) throws IOException {
        return Jsoup.parse(new URL(url), 300000);
    }

    public List<Vacancy> parse() {
        List<Vacancy> vacanciesList = new ArrayList<>();
        LocalDate localDateYearLate = LocalDate.now().minusYears(1);
        int numberAllPage;
        try {
            numberAllPage = getNumAllPage(this.url);
            int i = 1;
            do {
                Document pageIn = getPage("https://www.sql.ru/forum/job-offers/".concat(String.valueOf(i)));
                Elements tableJobs = pageIn.select("tr");
                for (Element element : tableJobs) {
                    String jobName = element.select("td[class=postslisttopic]").select("a").text();
                    if (!jobName.contains("Javascript")
                            & !jobName.contains("JavaScript")
                            & !jobName.contains("Java script")
                            & !jobName.contains("Java-script")
                            & (jobName.contains("Java") || jobName.contains("java"))) {
                        String date = element.select("td[class=altCol]td[style=text-align:center]").text();
                        LocalDate dateVacancy = DateConverter.convertToDate(date);
                        if (dateVacancy.isBefore(localDateYearLate)) {
                            break;
                        }
                        String linkOnVacancy = element.select("td[class=postslisttopic]").select("a").attr("href");
                        vacanciesList.add(new Vacancy(jobName, getInfoVacancy(linkOnVacancy), linkOnVacancy, dateVacancy));
                    }
                }
                i++;
            } while (i <= numberAllPage);
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return vacanciesList;
    }

    private String getInfoVacancy(String link) {
        String info = null;
        try {
            Document page = getPage(link);
            Elements elements = page.select("td[class=msgBody]");
            info = elements.get(1).text();
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return info;
    }

    private Integer getNumAllPage(String url) {
        Integer n = 0;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements sortOptions = doc.getElementsByClass("sort_options");
            Element lastPage = sortOptions.last().getElementsByTag("a").last();
            n = Integer.valueOf(lastPage.text());
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
        return n;
    }
}
