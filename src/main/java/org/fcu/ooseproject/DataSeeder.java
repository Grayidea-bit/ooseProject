package org.fcu.ooseproject;

import java.util.Calendar;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final IssueRepository issueRepository;

    public DataSeeder(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public void run(String... args) {
        if (issueRepository.count() == 0) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 22);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date deadline = calendar.getTime();

            issueRepository.save(new Issue(
                    "練習 LeetCode",
                    "完成兩題 LeetCode Medium 難度的演算法題。",
                    IssueType.Daily,
                    StatusType.InProgress,
                    deadline));

            issueRepository.save(new Issue(
                    "預習作業系統",
                    "閱讀《Operating System Concepts》第 5 章：CPU 排程。",
                    IssueType.Daily,
                    StatusType.InProgress,
                    deadline));

            issueRepository.save(new Issue(
                    "寫每日筆記",
                    "整理今天學習的內容，更新 Notion 筆記。",
                    IssueType.Daily,
                    StatusType.InProgress,
                    deadline));

            issueRepository.save(new Issue(
                    "練習 Git",
                    "使用 Git 完成一次 commit、push 並建立 pull request。",
                    IssueType.Daily,
                    StatusType.InProgress,
                    deadline));

            issueRepository.save(new Issue(
                    "參加學習社群討論",
                    "在 Discord 的 CS 學習群回答一個問題，或分享學習心得。",
                    IssueType.Daily,
                    StatusType.InProgress,
                    deadline));
        }
    }
}