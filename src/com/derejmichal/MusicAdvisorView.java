package com.derejmichal;

import java.util.ArrayList;
import java.util.List;

public class MusicAdvisorView {

    private List<String> name = new ArrayList<>();
    private List<String> author = new ArrayList<>();
    private List<String> link = new ArrayList<>();
    private int resultPage;

    public void setResultPage(int resultPage) {
        this.resultPage = resultPage;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public void setLink(List<String> link) {
        this.link = link;
    }

    public int calculateSize() {
        if (resultPage == 0) {
            return 0;
        } else {
            return name.size() % resultPage == 0 ?
                    name.size() / resultPage : (name.size() / resultPage) + 1;
        }
    }

    public void display(int currentPage, String type) {

        if (calculateSize() == 0) {

            currentPage = 0;
        } else {

            int currentTemp = currentPage;
            int iterationStart = 0;
            while (currentTemp > 1) {
                currentTemp--;
                iterationStart += resultPage;
            }

            System.out.println();

            for (int i = iterationStart; i < iterationStart + resultPage && i < name.size(); i++) {

                System.out.println(name.get(i));

                if (type.equals("new")) {
                    System.out.println(author.get(i));
                }

                if (type.equals("new") || type.equals("featured") || type.equals("playlist")) {
                    System.out.println(link.get(i));
                    System.out.println();
                }
            }
            System.out.println();
        }
        System.out.println("---PAGE " + currentPage + " OF " + calculateSize() + "---");
    }
}
