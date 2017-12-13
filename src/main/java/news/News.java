package news;

class News {
    private String source;
    private String title;
    private String description;

    News(String source, String title, String description) {
        this.source = source;
        this.title = title;
        this.description = description;
    }

    String getSource() {
        return source;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }
}
