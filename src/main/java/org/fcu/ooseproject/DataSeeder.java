package org.fcu.ooseproject;

import java.util.Calendar;
import java.util.Date;

import org.fcu.ooseproject.entity.Issue;
import org.fcu.ooseproject.repository.IssueRepository;
import org.fcu.ooseproject.entity.type.IssueType;
import org.fcu.ooseproject.entity.type.StatusType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final IssueRepository issueRepository;

    public DataSeeder(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    private Date getDeadline(int daysFromNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysFromNow);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    @Override
    public void run(String... args) {
        if (issueRepository.count() == 0) {
            // 1. 緊急的每日任務
            issueRepository.save(new Issue(
                "完成程式作業",
                "軟體框架期末專案需要完成 RESTful API 的實作",
                IssueType.Daily,
                StatusType.TODO,
                getDeadline(1)));

            // 2. 進行中的讀書計畫
            issueRepository.save(new Issue(
                "準備演算法考試",
                "複習 Dynamic Programming 和 Graph Theory 章節",
                IssueType.Schedule,
                StatusType.InProgress,
                getDeadline(5)));

            // 3. 已完成的每日任務
            issueRepository.save(new Issue(
                "LeetCode 練習",
                "完成了二分搜尋樹相關的三道題目",
                IssueType.Daily,
                StatusType.Finished,
                getDeadline(0)));

            // 4. 長期讀書計畫
            issueRepository.save(new Issue(
                "Java 認證準備",
                "準備 Oracle Java 認證考試，需要複習 Java 8 到 17 的新特性",
                IssueType.Schedule,
                StatusType.TODO,
                getDeadline(30)));

            // 5. 進行中的每日任務
            issueRepository.save(new Issue(
                "寫技術部落格",
                "撰寫關於 Spring Boot 專案開發經驗的文章",
                IssueType.Daily,
                StatusType.InProgress,
                getDeadline(2)));

            // 6. 即將到期的讀書計畫
            issueRepository.save(new Issue(
                "資料庫期末報告",
                "完成 NoSQL 與關聯式資料庫的比較分析報告",
                IssueType.Schedule,
                StatusType.InProgress,
                getDeadline(3)));

            // 7. 已完成的讀書計畫
            issueRepository.save(new Issue(
                "Git 工作坊",
                "完成 Git 版本控制工作坊的簡報製作",
                IssueType.Schedule,
                StatusType.Finished,
                getDeadline(-1)));

            // 8. 待處理的每日任務
            issueRepository.save(new Issue(
                "更新個人作品集",
                "在 GitHub 上更新專案文件和 README",
                IssueType.Daily,
                StatusType.TODO,
                getDeadline(4)));

            // 9. 遠期讀書計畫
            issueRepository.save(new Issue(
                "雲端架構研究",
                "研究 AWS 和 Azure 的架構差異，準備雲端架構師認證",
                IssueType.Schedule,
                StatusType.TODO,
                getDeadline(60)));

            // 10. 進行中的重要任務
            issueRepository.save(new Issue(
                "程式碼重構",
                "重構專案中的舊程式碼，加入單元測試",
                IssueType.Daily,
                StatusType.InProgress,
                getDeadline(7)));
        }
    }
}