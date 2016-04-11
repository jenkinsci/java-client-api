package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.HourMinSec10;
import com.offbytwo.jenkins.model.LoadStatistics;

public class JenkinsLoadStatisticsExample {

    @Test
    public void getLoadStatistics() throws IOException {
        // JenkinsServer js = new
        // JenkinsServer(URI.create("http://localhost:10090/"));
        JenkinsServer js = new JenkinsServer(URI.create("http://ci.soebes.de:8080/"));

        Map<String, Computer> computers2 = js.getComputers();
        for (Entry<String, Computer> computerItem : computers2.entrySet()) {

            System.out.println("Computer: " + computerItem.getKey());
            Computer value = computerItem.getValue();
            ComputerWithDetails details = value.details();

            LoadStatistics loadStatistics = details.getLoadStatistics();

            System.out.println("busyExecutors");
            HourMinSec10 busyExecutors = loadStatistics.getBusyExecutors();
            List<Double> busyExecutorsHistory = busyExecutors.getHour().getHistory();
            for (Double item : busyExecutorsHistory) {
                System.out.println(" " + item);
            }
            System.out.println(" latest: " + busyExecutors.getHour().getLatest());

            System.out.println("queueLength");
            HourMinSec10 queueLength = loadStatistics.getQueueLength();
            List<Double> queueLengthHistory = queueLength.getHour().getHistory();
            for (Double item : queueLengthHistory) {
                System.out.println(" " + item);
            }
            System.out.println(" latest: " + queueLength.getHour().getLatest());

            System.out.println("totalExecutors");
            HourMinSec10 totalExecutors = loadStatistics.getTotalExecutors();
            List<Double> totalExecutorsHistory = totalExecutors.getHour().getHistory();
            for (Double item : totalExecutorsHistory) {
                System.out.println(" " + item);
            }
            System.out.println(" latest: " + totalExecutors.getHour().getLatest());

        }

    }
}
