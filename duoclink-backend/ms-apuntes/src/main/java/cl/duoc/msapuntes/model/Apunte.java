package cl.duoc.msapuntes.model;

import java.util.List;

public class Apunte {
    private String id;
    private String type; // "text", "media", "link", "document"
    private String title;
    private String body; // Used for text content
    private String link; // Used for URL if type != text
    private List<String> tags;
    private String createdAt;
    private String userId;

    public Apunte() {
    }

    public Apunte(String id, String type, String title, String body, String link, List<String> tags, String createdAt, String userId) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.link = link;
        this.tags = tags;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
