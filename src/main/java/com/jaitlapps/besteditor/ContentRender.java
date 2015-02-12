package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.RecordEntry;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContentRender {
    protected static CommonPreferences preferences = CommonPreferences.getInstance();

    public String render(RecordEntry recordEntry, String content) {

        String contentWithImage = updateLinkToImages(content);

        String resultMarkDown = addTitleAndAuthor(recordEntry, contentWithImage);

        Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();

        String html = null;

        try {
            html = markdown4jProcessor.process(resultMarkDown);
        } catch (IOException e) {
            e.printStackTrace();
        }

        html = processHTML(html);

        return html;
    }

    private String updateLinkToImages(String content) {
        String pathToImageFolder = "](" + getLinkToImages();
        String contentWithImage = null;

        if(content != null && content.length() > 0)
            contentWithImage = content.replace("](images", pathToImageFolder).replace("\\", "/");

        return contentWithImage;
    }

    private String addTitleAndAuthor(RecordEntry recordEntry, String content) {
        String titleH1 = null;

        if(recordEntry.getTitle() != null && recordEntry.getTitle().length() > 0) {
            titleH1 = "#" + recordEntry.getTitle();
        }

        String authorLink = null;

        if(recordEntry.isAuthorExist()) {
            if (recordEntry.getAuthorName() != null && recordEntry.getAuthorName().length() > 0
                    && recordEntry.getAuthorURL() != null && recordEntry.getAuthorURL().length() > 0) {
                authorLink = "**Автор:** [" + recordEntry.getAuthorName() + "](" + recordEntry.getAuthorURL() + ")";
            }
        }

        String resultMarkDown = "";

        if(titleH1 != null)
            resultMarkDown += titleH1 + "\n\n";

        if(authorLink != null)
            resultMarkDown += authorLink + "\n\n";

        if(content != null)
            resultMarkDown += content;

        return resultMarkDown;
    }

    private String processHTML(String html) {
        String newHtml = html.replace("<br  />", "").replace("<a", "<a target=\"_blank\"");

        return getHeader() + newHtml + getFooter();
    }

    private String getHeader() {
        return "<html><head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + getLinkToCSS() + "\" />" +
                "<head><body>";
    }

    private String getFooter() {
        return "</body></html>";
    }

    private String getLinkToCSS() {
        Path pathToCSS = Paths.get(preferences.getWorkFolder(), "content", "css", "common_style.css");

        return "file:///" + pathToCSS;
    }

    private String getLinkToImages() {
        return "file:///" + preferences.getWorkFolder().replace(" ", "%20") + "/content/images";
    }
}
